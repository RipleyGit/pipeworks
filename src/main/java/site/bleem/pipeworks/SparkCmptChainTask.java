package site.bleem.pipeworks;

import lombok.Data;
import org.apache.spark.sql.SparkSession;
import site.bleem.pipeworks.framework.IBaseCmpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 链式组合任务，运行参数为数组格式
 */
@Data
public class SparkCmptChainTask extends AbsCmptSparkBootStrap {

    private String cmptKey = "chain_cmpt";

    private List<IBaseCmpt> cmpts;

    public void add(IBaseCmpt cpmt) {
        if (cmpts == null) {
            cmpts = new ArrayList<>();
        }
        cmpts.add(cpmt);
    }

    public static SparkCmptChainTask builder() {
        return new SparkCmptChainTask();
    }

    public static SparkCmptChainTask builder(IBaseCmpt... cmpts) {
        SparkCmptChainTask sparkCmptChainTask = new SparkCmptChainTask();
        for (IBaseCmpt cmpt : cmpts) {
            sparkCmptChainTask.add(cmpt);
        }
        return sparkCmptChainTask;
    }

    @Override
    public Object run(SparkSession sparkSession) {
        for (IBaseCmpt absBaseCmpt : cmpts) {
            Object run = absBaseCmpt.run(sparkSession);
        }
        return null;
    }

    /**
     * 内部校验参数是否正常
     *
     * @return
     */
    @Override
    public Boolean validate() {
        for (IBaseCmpt iBaseCmpt : getCmpts()) {
            if (!iBaseCmpt.validate()) {
                return false;
            }
        }
        return true;
    }
}
