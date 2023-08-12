package site.bleem.pipeworks.framework;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.spark.sql.SparkSession;

/**
 * 自动匹配数据种类的基类
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "cmptKey", visible = true)
public interface IBaseCmpt {

    String getCmptKey();

    /**
     * Spark运行主入口
     *
     * @param sparkSession
     * @return
     */
    Object run(SparkSession sparkSession);

    /**
     * 校验参数正确性
     *
     * @return
     */
    default Boolean validate() {
        return true;
    }
}
