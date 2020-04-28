package cn.s2b.maven.plugin;

import com.alibaba.fastjson.serializer.BeforeFilter;

import java.lang.reflect.Field;

public class CustomBeforeFilter extends BeforeFilter {
    @Override
    public void writeBefore(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!TypeHelps.isJavaLangType(field.getType().getName())) {
                    Class cls = Parse.loadClass(field.getType().getTypeName());
                    field.set(o, Parse.loadObject(cls));
                }
            } catch (Exception e) {
            }
        }
    }
}