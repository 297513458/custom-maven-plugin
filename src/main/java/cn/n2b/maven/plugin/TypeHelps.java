package cn.n2b.maven.plugin;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeHelps {

    static private List<String> JAVA_LANG_TYPE = Lists.newArrayList(
            "String", String.class.getTypeName(),
            "Integer", Integer.class.getTypeName(),
            "int",
            "Long", Long.class.getTypeName(),
            "long",
            "Double", Double.class.getTypeName(),
            "double",
            "Float", Float.class.getTypeName(),
            "float",
            "Character",Character.class.getTypeName(),
            "char",
            "Short",Short.class.getTypeName(),
            "short",
            "Boolean", Boolean.class.getTypeName(),
            "boolean",
            "Byte",Byte.class.getTypeName(),
            "byte",
            "Object", Object.class.getTypeName()
    );

    public static boolean isJavaLangType(String type) {
        return JAVA_LANG_TYPE.contains(type);
    }

    static private String annotationValueTrim(String val) {
        if (val.startsWith("\"") && val.endsWith("\"")) {
            return val.substring(1, val.length() - 1);
        }

        return val;
    }


    /**
     * 寻找泛型参数
     *
     * @param val
     * @return
     */
    static public String findGenericType(String val) {
        String reg = "<(.+)>";
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(val);
        String findType = val;
        while (m.find()) {
            String matchStr = m.group(0).substring(1, m.group(0).length() - 1);
            if (matchStr.contains("<")) {
                findType = findGenericType(matchStr);
            } else if (matchStr.contains(",")) {
                findType = findGenericType(matchStr.split(",")[1].trim());
            } else {
                findType = matchStr;
            }
        }
        return findType;
    }
}