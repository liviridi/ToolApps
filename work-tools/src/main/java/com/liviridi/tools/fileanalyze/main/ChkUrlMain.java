package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.IOException;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;

public class ChkUrlMain {

    private static CommonLogger logger = CommonLogger.getLogger(WrtencdAnlsMain.class);

    /**
     * new URL新旧比較
     *
     * @param args
     * @throws IOException
     *             ファイル読込エラー
     */
    public static void main(String[] args) throws IOException {

        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        String resStorePath = AnalyseUtil.getWorkPath("artf221275");

        //File findResFl = new File(resStorePath + "insFindResult.txt");
        File findResFl = new File(resStorePath + "findResult.txt");

        String resCtnt = FileUtil.readFileContent(findResFl, "SJIS");

        String[] resLines = resCtnt.split(FileUtil.LBRK_CRLF);

        int LineNo = 0;
        for (String resLine : resLines) {
            LineNo++;
            String path = AnalyseUtil.interceptCtnt(resLine, "C:", ".java").get(0).getContent();
            String lineCtnt = resLine.replace(AnalyseUtil.interceptCtnt(resLine, "\"C:", "):").get(0).getContent(), "");

            logger.debug(path + "開始 lineCtnt:" + lineCtnt);
        }
    }

}
