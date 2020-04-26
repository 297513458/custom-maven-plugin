package cn.s2b.maven.plugin;

import org.apache.maven.project.MavenProject;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class CustomLoad {
    static URLClassLoader newLoader;

    public static ClassLoader getClassLoad() {
        return newLoader;
    }

    public static ClassLoader getClassLoad(MavenProject project) {
        if (project == null)
            return null;
        try {
            if (newLoader == null) {
                List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
                URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
                for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                    String element = runtimeClasspathElements.get(i);
                    runtimeUrls[i] = new File(element).toURI().toURL();
                }
                newLoader = new URLClassLoader(runtimeUrls,
                        Thread.currentThread().getContextClassLoader());
            }
            return newLoader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newLoader;
    }
}