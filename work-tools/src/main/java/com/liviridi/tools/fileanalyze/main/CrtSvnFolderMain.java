package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.IOException;

public class CrtSvnFolderMain {

    public static void main(String[] args) throws IOException {
        String path = "/Users/yunli/work/proj/ngn/.svn/pristine/";
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                String iH = Integer.toHexString(i);
                String jH = Integer.toHexString(j);
                File svnFile = new File(path + iH + jH);
                svnFile.mkdir();
            }
        }
    }
}
