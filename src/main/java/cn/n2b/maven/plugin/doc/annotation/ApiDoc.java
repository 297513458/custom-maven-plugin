package cn.n2b.maven.plugin.doc.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiDoc {
    /**
     * api的地址
     *
     * @return
     */
    String value();
}