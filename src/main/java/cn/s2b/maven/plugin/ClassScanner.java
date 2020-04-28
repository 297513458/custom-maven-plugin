package cn.s2b.maven.plugin;

import cn.s2b.maven.plugin.doc.annotation.ApiDoc;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    static List<String> scanFiles = new ArrayList<String>();

    /**
     * TODO:递归扫描指定文件夹下面的指定文件
     */
    @ApiDoc("/gw/api")
    public static List<String> scanFilesWithExt(String folderPath, String ext) {
        List<String> dirctorys = new ArrayList<String>();
        File directory = new File(folderPath);
        if (directory.isDirectory()) {
            File[] filelist = directory.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    dirctorys.add(filelist[i].getAbsolutePath());
                    scanFilesWithExt(filelist[i].getAbsolutePath(), ext);
                } else {
                    if (filelist[i] != null && filelist[i].getName().endsWith(ext))
                        scanFiles.add(filelist[i].getAbsolutePath());
                }
            }
        }
        return scanFiles;
    }


}