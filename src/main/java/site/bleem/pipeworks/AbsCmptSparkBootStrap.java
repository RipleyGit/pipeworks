package site.bleem.pipeworks;


import lombok.extern.slf4j.Slf4j;
import site.bleem.pipeworks.framework.IBaseCmpt;
import site.bleem.pipeworks.strap.SparkBootStrap;
import site.bleem.pipeworks.util.ConvertUtil;
import site.bleem.pipeworks.util.BleemJacksonUtil;

import lombok.Data;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

@Data
@Slf4j
public abstract class AbsCmptSparkBootStrap extends SparkBootStrap implements Serializable, IBaseCmpt {

    public Boolean isDebug = false;

    public Boolean isDebug() {
        if (isDebug) {
            return true;
        } else {
            return false;
        }
    }

    //    @Override
    public Object run(String[] args) {
        try {
            String version = null;
            try {
                InputStream is = getClass().getResourceAsStream("/META-INF/maven/cn.com.bsfit/bsfit-decision-task/pom.properties");
                Properties prop = new Properties();
                prop.load(is);
                version = prop.getProperty("version");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            log.info("当前任务版本为:" + version);
            for (int i = 0; i < args.length; i++) {
                log.info("args[" + i + "]:" + args[i]);
            }

            SparkSession.Builder builder = SparkSession
                    .builder().config("spark.sql.legacy.allowUntypedScalaUDF", "true");

            String argType = args[0].trim();
            String argParam = args[1].trim();
            String argsJsonStr = null;
            switch (argType) {
                case "-pop": {
                    File file = new File(argParam);
                    if (!file.exists()) {
                        throw new RuntimeException("Configuration file does not exist:" + argParam);
                    }
                    log.info("properties is exist:" + argType);
                    try (FileInputStream fileIn = new FileInputStream(argParam)) {
                        Properties properties = new Properties();
                        properties.load(fileIn);
                        argsJsonStr = ConvertUtil.prop2Json(properties).toJSONString();
                    } catch (Exception e) {
                        throw new RuntimeException("The configuration file does not comply with the Properties specification!");
                    }
                    file.deleteOnExit();
                }
                break;

                case "-json": {
                    argsJsonStr = argParam;
                }
                break;

                case "-hdfs": {
                    SparkContext sparkContext = new SparkContext();
                    Configuration configuration = sparkContext.hadoopConfiguration();
                    FileSystem fs = null;
                    FSDataInputStream is = null;
                    byte[] buffer;
                    try {
                        fs = FileSystem.get(configuration);
                        Path path = new Path(argParam);
                        if (fs == null || !fs.exists(path)) {
                            throw new IOException(path + " is not exist.");
                        }
                        is = fs.open(path);
                        FileStatus stat = fs.getFileStatus(path);
                        buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
                        is.readFully(0, buffer);
                    } catch (Exception e) {
                        throw new RuntimeException("HDFS json file fail:" + argParam, e);
                    } finally {
                        if (is != null) {
                            IOUtils.closeStream(is);
                        }
                        if (fs != null) {
                            IOUtils.closeStream(fs);
                        }
                    }
                    argsJsonStr = new String(buffer, StandardCharsets.UTF_8);
                    builder.sparkContext(sparkContext);
                }
                break;
                default: {
                    throw new RuntimeException("The input parameter type is not in the restricted range：-f,-json");
                }
            }
            return run(argsJsonStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载run方法，支持自定义sparksession
     *
     * @param jsonStr
     * @return
     */
    Object run(String jsonStr) {
        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        return run(sparkSession, jsonStr);
    }

    public Object run(SparkSession sparkSession, String jsonStr) {
        SparkContext context = sparkSession.sparkContext();
        SparkConf sparkConf = context.conf();
        if (sparkConf.contains("spark.yarn.tags") && sparkConf.get("spark.yarn.tags").toLowerCase(Locale.ROOT).contains("debug")) {
            log.debug("DEBUG模式运行");
            this.isDebug =  true;
        }
        IBaseCmpt baseCmpt = BleemJacksonUtil.getDynamicCmpt(jsonStr);
        return baseCmpt.run(sparkSession);
    }
}
