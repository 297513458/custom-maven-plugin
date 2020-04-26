package cn.s2b.maven.plugin;
import cn.s2b.maven.plugin.doc.reflect.CustomMethod;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Execute(phase = LifecyclePhase.PACKAGE)
@Mojo(name = "doc", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class Doc extends AbstractMojo {
    /**
     * @parameter expression="${project}"
     * @readonly
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    /**
     * @parameter property="scanPackage" default-value="com"
     */
    @Parameter(required = false, defaultValue = "${scanPackage}")
    private String scanPackage;
    private String classPath;
    @Parameter(required = false, defaultValue = "${savePath}")
    public String savePath;
    private Set<Object> dicInformation = new HashSet<Object>();
    private List<Class<?>> classes = new ArrayList<>();


    public void save(String className, List<CustomMethod> list) throws Exception {
        if (className != null && className.length() > 0 && list != null && !list.isEmpty()) {
            StringBuilder filename = new StringBuilder();
            filename.append(savePath);
            File file = new File(filename.toString());
            if (!file.exists()) {
                file.mkdir();
            }
            filename.append(File.separator).append(className);
            this.getLog().info("save file " + filename.toString());
            String sb = JSON.toJSONString(list, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse);
            this.getLog().info(sb);
            FileOutputStream os = new FileOutputStream(new File(filename.toString()));
            os.write(sb.getBytes("utf-8"));
            os.close();
        }
    }

    @Override
    public void execute() throws MojoExecutionException {
        if (project != null) {
            Build build = project.getBuild();
            classPath = build.getOutputDirectory();
        }
        this.getLog().info("savePath:" + savePath);
        scanPackage();
    }

    public String scanPackage() {
        if (scanPackage == null) {
            scanPackage = "com";
        }
        this.getLog().info("scanPackage is =>" + scanPackage);
        getClasses(scanPackage);
        //
        for (Class c : classes) {
            List<CustomMethod> list = Parse.parse(c);
            if (list != null && !list.isEmpty()) {
                try {
                    if (savePath != null && savePath.length() > 0) {
                        save(c.getTypeName(), list);
                    } else
                        System.out.println(JSON.toJSONString(list));
                } catch (Exception e) {
                    this.getLog().warn("load class faild", e);
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public void getClasses(String pack) {
        boolean recursive = true;
        String packageName = pack;
        String packageSxcName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            URL[] urls = new URL[1];
            urls[0] = new URL("file:" + classPath);
            URLClassLoader urlClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            dirs = urlClassLoader.getResources(packageSxcName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("jar".equals(protocol)) {
                    packageName = scanJarFile(classes, recursive, packageName, packageSxcName, url);
                }
            }
            List<String> list = ClassScanner.scanFilesWithExt(classPath, "class");
            for (String s : list) {
                scanClassFile(s, classPath, scanPackage, classes, urlClassLoader);
            }
        } catch (IOException e) {
            getLog().warn("getClasses failed ", e);
        }
    }

    private String scanClassFile(String fullClassPath, String classPath, String scanPackage, List<Class<?>> classes, URLClassLoader urlClassLoader) {
        try {
            String className = fullClassPath.replace(classPath, "");
            if (className.charAt(0) == '/') {
                className = className.substring(1);
            }
            if (className.endsWith(".class")) {
                className = className.replace("/", ".").substring(0, className.length() - 6);
                if (scanPackage != null && className.startsWith(scanPackage)) {
                    Class<?> cls = null;
                    try {
                        cls = CustomLoad.getClassLoad(project).loadClass(className);
                        classes.add(cls);
                    } catch (Exception e) {
                        this.getLog().warn("load class faild", e);
                    }
                }
            }
        } catch (Exception e) {
            this.getLog().info("scan faild", e);
        }
        return classPath;
    }

    /**
     * load jar
     */
    private String scanJarFile(List<Class<?>> classes, boolean recursive, String packageName, String packageSxcName, URL url) {
        JarFile jar;
        try {
            // 获取jar
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            // 从此jar包 得到一个枚举类
            Enumeration<JarEntry> entries = jar.entries();
            // 同样的进行循环迭代
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以/开头的
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }
                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageSxcName)) {
                    int idx = name.lastIndexOf('/');
                    // 如果以"/"结尾 是一个包
                    if (idx != -1) {
                        // 获取包名 把"/"替换成"."
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    // 如果可以迭代下去 并且是一个包
                    if ((idx != -1) || recursive) {
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            // 去掉后面的".class" 获取真正的类名
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            try {
                                // 添加到classes
                                classes.add(Class.forName(packageName + '.' + className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            getLog().error("scan jar failed ", e);
        }
        return packageName;
    }
}