package site.bleem.pipeworks.framework;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * 自动匹配数据种类的基类
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "dataKey", visible = true)
public interface IDataSource extends Serializable {

    /**
     * 数据类型标识
     *
     * @return
     */
    String getDataKey();

    /**
     * 校验参数正确性
     *
     * @return
     */
    default Boolean validate(){return true;}
}
