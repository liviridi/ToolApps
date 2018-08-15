package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dom4j.DocumentException;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;

public class ElAnlsMain {

    // private static CommonLogger logger = CommonLogger.getLogger(ElAnlsMain.class);

    /**
     * ログ
     * 
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException {

        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        String resStorePath = AnalyseUtil.getWorkPath("artf220743");

        File resultF = new File(resStorePath + "findResult.txt");

        String pathCtnt = AnalyseUtil.doIntercept(resultF, AnalyseUtil.ANALYSE_UNIT_LINE, "C:", ".jsp", "SJIS");
        String[] paths = pathCtnt.split(FileUtil.LBRK_CRLF);
        Set<String> pathSet = new LinkedHashSet<String>();

        StringBuffer pathBuf = new StringBuffer();
        for (String path : paths) {
            if (!pathSet.contains(path)) {
                pathSet.add(path);
                pathBuf.append(path);
                pathBuf.append(FileUtil.LBRK_CRLF);
            }
        }

        FileUtil.writeFile(pathBuf.toString(), resStorePath + "jspPathResult.txt");

        // String pathCtnt = FileUtil.readFileContent(new File("/Users/yunli/work/test/NGAIA/jspResult.txt"), "UTF-8");

        StringBuffer result = new StringBuffer();
        StringBuffer pathWithBeanWrt = new StringBuffer();
        StringBuffer resultWithBeanWrt = new StringBuffer();

        for (String path : pathSet) {
            result.append(path + "  ---- start ----" + FileUtil.LBRK_CRLF);
            String res = AnalyseUtil.doIntercept(new File(path), AnalyseUtil.ANALYSE_UNIT_FILE, "<html:text ",
                    "</html:text>", "UTF-8");
            // if (FileUtil.LBRK_CRLF.equals(res)) {
            // res = AnalyseUtil.doAnalyse(new File(path), AnalyseUtil.ANALYSE_UNIT_FILE, "<html:text ",
            // "/>", "UTF-8");
            // }
            result.append(res);
            // result.append(AnalyseUtil.doXmlAnalyse(new File(path), "html:text"));
            // result.append(path + " ---- end ---- ↑↑↑↑ ----" + FileUtil.LBRK_CRLF);
            result.append(FileUtil.LBRK_CRLF);
            if (res.contains("<bean:write ")) {
                pathWithBeanWrt.append(path);
                pathWithBeanWrt.append(FileUtil.LBRK_CRLF);
                resultWithBeanWrt.append(path + "  ---- start ----" + FileUtil.LBRK_CRLF);
                resultWithBeanWrt.append(res);
                resultWithBeanWrt.append(FileUtil.LBRK_CRLF);
            }
        }
        FileUtil.writeFile(result.toString(), resStorePath + "jspReadResult.txt");
        FileUtil.writeFile(pathWithBeanWrt.toString(), resStorePath + "jspPathBeanWrite.txt");
        FileUtil.writeFile(resultWithBeanWrt.toString(), resStorePath + "jspReadResultBeanWrite.txt");

    }

}
