package cn.n2b.maven.plugin;


import cn.n2b.maven.plugin.doc.annotation.ApiDoc;

import java.util.ArrayList;
import java.util.List;

public class TestDoc {

    /**
     * 测试用的
     */
    @ApiDoc("/gw/api")
    public List<String> scanFilesWithExt(String folderPath, String ext) {
        return new ArrayList<>();
    }
}
