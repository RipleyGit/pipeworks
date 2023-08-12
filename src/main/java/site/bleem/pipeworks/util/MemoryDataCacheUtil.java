package site.bleem.pipeworks.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MemoryDataCacheUtil {

    private static LoadingCache<String, Dataset> dataCache = null;

    @SneakyThrows
    public Dataset getDataCache(String keyId) {
        return dataCache.get(keyId);
    }

    public static void setDataCache(String keyId, Dataset dataset) {
        dataCache.put(keyId, dataset);
    }

    /**
     * 私有构造函数，仅内部类第一次调用时才会创建
     */
    private MemoryDataCacheUtil() {
        if (dataCache != null) {
            return;
        }
        dataCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.DAYS)
                .expireAfterWrite(10, TimeUnit.DAYS)
                .removalListener(
                        (RemovalListener<String, Dataset>) rn ->
                                log.debug("keyId=" + rn.getKey() + ", cause=" + rn.getCause().name())
                )
                .build(
                        new CacheLoader<String, Dataset>() {
                            @Override
                            public Dataset load(String keyId) {
                                throw new RuntimeException("无需使用 load 方法");
                            }
                        });
    }

    /**
     * 私有内部类，保障线程安全
     */
    private static class MemoryDataCacheHolder {
        private static MemoryDataCacheUtil instance = new MemoryDataCacheUtil();
    }

    private static MemoryDataCacheUtil getInstance() {
        return MemoryDataCacheHolder.instance;
    }

    /**
     * 开放给外部使用接口
     *
     * @param keyId
     * @return
     */
    public static Dataset get(String keyId) {
        MemoryDataCacheUtil instance = MemoryDataCacheUtil.getInstance();
        return instance.getDataCache(keyId);
    }

    /**
     * 开放给外部使用接口
     *
     * @param keyId
     * @param dataset
     */
    public static void put(String keyId, Dataset dataset) {
        MemoryDataCacheUtil instance = MemoryDataCacheUtil.getInstance();
        instance.setDataCache(keyId, dataset);
    }
}
