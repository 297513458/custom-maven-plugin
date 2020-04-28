package cn.s2b.maven.plugin.doc.reflect;

import java.util.List;

public class CustomMethod {
    private List<TypeParam> params;
    private TypeParam type;
    private String className;
    private String api;
    private List<String> interfaces;
    private String methodName;

    public List<TypeParam> getParams() {
        return params;
    }

    public void setParams(List<TypeParam> params) {
        this.params = params;
    }

    public TypeParam getType() {
        return type;
    }

    public void setType(TypeParam type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
