# custom-maven-plugin
# 扫描api接口的maven插件
主要用途:可以在某个方法上面写上注解cn.s2b.maven.plugin.doc.annotation.ApiDoc("网关的url"),即可通过运行maven custom:doc去扫描获取到类名,方法名,参数类型,返回类型的json字符串

maven需加上插件
<pre>
        &lt;plugin&gt;
                &lt;groupId&gt;cn.s2b&lt;/groupId&gt;
                &lt;artifactId&gt;custom-maven-plugin&lt;/artifactId&gt;
                &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
            &lt;/plugin&gt;
</pre>
