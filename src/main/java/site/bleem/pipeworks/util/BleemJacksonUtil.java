package site.bleem.pipeworks.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import site.bleem.pipeworks.framework.IBaseCmpt;
import site.bleem.pipeworks.framework.IDataSource;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BleemJacksonUtil {

    /**
     * 将json字符串凡序列化成动态组件
     *
     * @param jsonStr
     * @return
     */
    public static IBaseCmpt getDynamicCmpt(String jsonStr) {

        List<NamedType> subTypes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Reflections reflections = new Reflections(new SubTypesScanner());
        //扫描具体组件实现类
        Set<Class<? extends IBaseCmpt>> subClassSet = reflections.getSubTypesOf(IBaseCmpt.class);

        for (Class<? extends IBaseCmpt> dict : subClassSet) {
            if (Modifier.isAbstract(dict.getModifiers())) {
                continue;
            }
            try {
                IBaseCmpt absBaseCmpt = dict.newInstance();
                NamedType namedType = new NamedType(dict, absBaseCmpt.getCmptKey());
                subTypes.add(namedType);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        //扫描数据具体实现类
        Set<Class<? extends IDataSource>> subClassDataSet = reflections.getSubTypesOf(IDataSource.class);
        for (Class<? extends IDataSource> dict : subClassDataSet) {
            if (Modifier.isAbstract(dict.getModifiers())) {
                continue;
            }
            try {
                IDataSource absBaseCmpt = dict.newInstance();
                NamedType namedType = new NamedType(dict, absBaseCmpt.getDataKey());
                subTypes.add(namedType);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        mapper.registerSubtypes(subTypes.toArray(new NamedType[0]));
        IBaseCmpt baseCmpt = null;
        try {
            baseCmpt = mapper.readValue(jsonStr, IBaseCmpt.class);
        } catch (IOException e) {
            throw new RuntimeException(" load script error", e);
        }
        return baseCmpt;
    }

}
