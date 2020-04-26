package cn.s2b.maven.plugin.doc.reflect;

public class TypeParam {
    public String typeName;
    public String name;
    public Object value;
    public boolean javaType;

    public TypeParam() {
    }

    public TypeParam(String typeName, String name, Object value, boolean javaType) {
        this.javaType = javaType;
        this.typeName = typeName;
        this.name = name;
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public TypeParam(String typeName, Object value, boolean javaType) {
        this.typeName = typeName;
        this.javaType = javaType;
        this.value = value;
    }

    public boolean isJavaType() {
        return javaType;
    }

    public void setJavaType(boolean javaType) {
        this.javaType = javaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}