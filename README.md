# 扫描api接口的maven插件
主要用途:扫描注解上面的获取到类名,方法名,参数类型,返回类型的json字符串
#
###使用步骤:
###1:maven需加上插件#
<pre>
        **&lt;plugin&gt;
                &lt;groupId&gt;cn.s2b&lt;/groupId&gt;
                &lt;artifactId&gt;custom-maven-plugin&lt;/artifactId&gt;
                &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
            &lt;/plugin&gt;**
</pre>
###2:在某个方法上面写上注解**@cn.s2b.maven.plugin.doc.annotation.ApiDoc("对外的url")**
###3:运行**maven custom:doc**