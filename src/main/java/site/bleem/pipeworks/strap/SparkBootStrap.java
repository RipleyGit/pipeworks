package site.bleem.pipeworks.strap;


import lombok.extern.slf4j.Slf4j;
import site.bleem.pipeworks.exception.SparkBootStrapException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class SparkBootStrap implements Serializable {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("main function parameter:").append(System.lineSeparator()).append(System.lineSeparator());
        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(args).forEach(s -> sb.append(i.getAndIncrement()).append("=").append(s).append(System.lineSeparator()));
        sb.append(System.lineSeparator());
        log.info(sb.toString());
        Calendar startTime = Calendar.getInstance();
        try {
            String userClass = null;
            Properties pros = System.getProperties();
            String[] sparkSubmitArgs = pros.getProperty("sun.java.command").split("\\ ");
            for (String command : sparkSubmitArgs) {
                log.info("sun.java.command IS :" + command);
                if (command.contains("cn.com.bsfit.")) {
                    userClass = command;
                    log.info("USER_CLASS IS :" + userClass);
                    break;
                }
            }

            if (userClass == null) {
                throw new SparkBootStrapException("配置找不到可以的userClass配置类！");
            }

            SparkBootStrap instance = null;
            try {
                Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(userClass);
                instance = (SparkBootStrap) cls.newInstance();
            } catch (Exception e) {
                throw new SparkBootStrapException(e);
            }

            instance.run(args);

        } catch (RuntimeException e) {
            log.error(SparkBootStrap.class.getSimpleName() + " : ", e);
            throw new SparkBootStrapException(e);
        } finally {
            Calendar endTime = Calendar.getInstance();
            log.info("组件运行耗时: " + (float) Math.round((((float) (endTime.getTimeInMillis() - startTime.getTimeInMillis())) / 1000) * 10) / 10 + " seconds");
        }
    }



    /**
     * 组件运行逻辑
     *
     * @param args the args
     * @return the object
     */
    public abstract Object run(String[] args);

}