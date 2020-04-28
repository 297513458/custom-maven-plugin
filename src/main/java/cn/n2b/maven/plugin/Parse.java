package cn.n2b.maven.plugin;

import cn.n2b.maven.plugin.doc.annotation.ApiDoc;
import cn.n2b.maven.plugin.doc.reflect.CustomMethod;
import cn.n2b.maven.plugin.doc.reflect.TypeParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parse {
    private static Map<String, Class> classMap = new HashMap<String, Class>();
    private static Map<String, Object> objectMap = new HashMap<String, Object>();

    public static List<CustomMethod> parse(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<CustomMethod> methodList = new ArrayList<>();
        List<String> interfaceList = null;
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            interfaceList = new ArrayList<>(interfaces.length);
            for (Class ints : interfaces) {
                interfaceList.add(ints.getTypeName());
            }
        }
        for (Method m : methods) {
            Annotation[] ans = m.getAnnotations();
            for (Annotation a : ans) {
                if (ApiDoc.class.getName().equals(a.annotationType().getName())) {
                    ApiDoc sxc = (ApiDoc) a;
                    if (sxc == null)
                        continue;
                    CustomMethod customMethod = new CustomMethod();
                    customMethod.setInterfaces(interfaceList);
                    customMethod.setMethodName(m.getName());
                    customMethod.setClassName(clazz.getTypeName());
                    customMethod.setApi(sxc.value());
                    List<TypeParam> params = new ArrayList<>();
                    Class<?> returnType = m.getReturnType();
                    customMethod.setType(new TypeParam(returnType.getTypeName(), loadValue(returnType), TypeHelps.isJavaLangType(returnType.getName())));
                    java.lang.reflect.Parameter[] parameters = m.getParameters();
                    for (java.lang.reflect.Parameter pm : parameters) {
                        try {
                            TypeParam param = new TypeParam();
                            param.setJavaType(TypeHelps.isJavaLangType(pm.getType().getName()));
                            param.setName(pm.getName());
                            param.setTypeName(pm.getType().getTypeName());
                            param.setValue(loadValue(pm.getType()));
                            params.add(param);
                        } catch (Exception e) {
                        }
                    }
                    customMethod.setParams(params);
                    methodList.add(customMethod);
                }
            }
        }
        return methodList;
    }

    public static Class loadClass(String classname) {
        try {
            if (classname == null) {
                return null;
            }
            Class cls = classMap.get(classname);
            if (cls == null) {
                cls = CustomLoad.getClassLoad().loadClass(classname);
                classMap.put(classname, cls);
            }
            return cls;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 导出object
     *
     * @param cls
     * @return
     */
    public static Object loadObject(Class cls) {
        try {
            if (cls == null) {
                return null;
            }
            String name = cls.getName();
            if (!TypeHelps.isJavaLangType(name)) {
                Object ob = objectMap.get(name);
                if (ob == null) {
                    ob = cls.getDeclaredConstructor().newInstance();
                    objectMap.put(name, ob);
                }
                return ob;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static Object loadValue(Class cls) {
        try {
            if (cls != null) {
                String name = cls.getName();
                Object ob = loadObject(cls);
                if (!TypeHelps.isJavaLangType(cls.getTypeName())) {
                    String value = JSON.toJSONString(ob, new CustomBeforeFilter(), SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse);
                    return value;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}