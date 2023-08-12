//package cn.com.bsfit.spark.task;
//
//import site.bleem.pipeworks.SparkCmptChainTask;
//import cn.com.bsfit.spark.framework.DataMigrationCmptTask;
//import cn.com.bsfit.spark.framework.data.KafkaDataSet;
//import cn.com.bsfit.spark.framework.data.ParquetTableDataSet;
//import com.alibaba.fastjson.JSONObject;
//import lombok.SneakyThrows;
//import org.apache.spark.sql.SparkSession;
//import org.junit.Test;
//
//public class DataMigrationCmptTaskTest {
//
//    /**
//     * 数据迁移组件使用示例
//     */
//    @Test
//    public void test(){
//
//    }
////
////    /**
////     * 数据迁移组件使用示例
////     */
////    @SneakyThrows
////    @Test
////    public void dataMigrationCmptTaskTest(){
////        SparkSession.clearActiveSession();
////        SparkSession.Builder builder = SparkSession.builder();
////        builder = builder.master("local[*]");
////        SparkSession sparkSession = builder.enableHiveSupport().getOrCreate();
////        String inputdata="src/test/resources/data/input/kafka/9ff4093006634957ac7e374757e9a933/3qe7r7erj2gg/data";
////        DataMigrationCmptTask dataMigrationCmptTask = new DataMigrationCmptTask();
////        ParquetTableDataSet parquetTableDataSet = new ParquetTableDataSet();
////        parquetTableDataSet.setPath(inputdata);
////        dataMigrationCmptTask.setInputTableDataSet(parquetTableDataSet);
////        KafkaDataSet kafkaDataSet = new KafkaDataSet();
////        kafkaDataSet.setKafkaServers("10.100.2.161:9092");
////        kafkaDataSet.setKafkaTopic("InternalcontrolRiskArchiveQueue_offline110");
////        dataMigrationCmptTask.setOutputDataSet(kafkaDataSet);
////        dataMigrationCmptTask.run(sparkSession);
//////        System.setProperty("sun.java.command","cn.com.bsfit.spark.framework.DataMigrationCmptTask");
//////        String jsonString = JSONObject.toJSONString(dataMigrationCmptTask);
//////        DataMigrationCmptTask.main(new String[]{"-json",jsonString});
////    }
//
////    /**
////     * 组合链组件使用示例
////     */
////    @SneakyThrows
////    @Test
////    public void sparkCmptChainTaskTest(){
////        SparkSession.clearActiveSession();
////        SparkSession.Builder builder = SparkSession.builder();
////        builder = builder.master("local[*]");
////        SparkSession sparkSession = builder.enableHiveSupport().getOrCreate();
////        String inputdata="src/test/resources/data/input/kafka/9ff4093006634957ac7e374757e9a933/3qe7r7erj2gg/data";
////        DataMigrationCmptTask dataMigrationCmptTask = new DataMigrationCmptTask();
////        ParquetTableDataSet parquetTableDataSet = new ParquetTableDataSet();
////        parquetTableDataSet.setPath(inputdata);
////        dataMigrationCmptTask.setInputTableDataSet(parquetTableDataSet);
////        KafkaDataSet kafkaDataSet = new KafkaDataSet();
////        kafkaDataSet.setKafkaServers("10.100.2.161:9092");
////        kafkaDataSet.setKafkaTopic("InternalcontrolRiskArchiveQueue_offline110");
////        dataMigrationCmptTask.setOutputDataSet(kafkaDataSet);
////        SparkCmptChainTask chainTask = SparkCmptChainTask.builder(dataMigrationCmptTask);
////        chainTask.run(sparkSession);
//////        String chainTaskStr = JSONObject.toJSONString(chainTask);
//////        chainTask.run(sparkSession,chainTaskStr);
////    }
////}