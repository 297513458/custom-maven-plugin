# custom-maven-plugin
# 扫描api接口的maven插件
主要用途:可以在某个方法上面写上注解cn.s2b.maven.plugin.doc.annotation.ApiDoc("网关的url"),即可通过运行maven custom:doc去扫描获取到类名,方法名,参数类型,返回类型的json字符串
maven需加上插件
        <plugin>
                <groupId>cn.s2b</groupId>
                <artifactId>custom-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
            </plugin>
