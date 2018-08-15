package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;

public class WrtencdAnlsMain {

    private static CommonLogger logger = CommonLogger.getLogger(WrtencdAnlsMain.class);

    /**
     * ファイル読み込んだ後書き込むとき、encoding問題
     *
     * @param args
     * @throws IOException
     *             ファイル読込エラー
     */
    public static void main(String[] args) throws IOException {


        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        String resStorePath = AnalyseUtil.getWorkPath("artf221112");

        //File findResFl = new File(resStorePath + "insFindResult.txt");
        File findResFl = new File(resStorePath + "wrtFindResult.txt");

        String resCtnt = FileUtil.readFileContent(findResFl, "SJIS");

        String[] resLines = resCtnt.split(FileUtil.LBRK_CRLF);

        int LineNo = 0;
        Map<String, String> methodCtnt = new LinkedHashMap<String, String>();
        for (String resLine : resLines) {
            LineNo++;
            String path = AnalyseUtil.interceptCtnt(resLine, "C:", ".java").get(0).getContent();
            String lineCtnt = resLine.replace(AnalyseUtil.interceptCtnt(resLine, "\"C:", "):").get(0).getContent(), "");

            logger.debug(path + "開始 lineCtnt:" + lineCtnt);
            if (lineCtnt.contains("import ")) {
                continue;
            }
            String lineFileCtnt = FileUtil.readFileContent(new File(path), "UTF-8");

            List<Integer> linePoss = new ArrayList<Integer>();
            int lnPos = lineFileCtnt.indexOf(lineCtnt);
            do {
                if (lnPos < 0) {
                    break;
                }
                linePoss.add(lnPos);
            } while ((lnPos = lineFileCtnt.indexOf(lineCtnt, lnPos + 1)) > 0);
            if (linePoss.isEmpty()) {
                System.out.println(LineNo + "行目のファイルエラーあり");
                continue;
            }

            char[] lineFileCtnts = lineFileCtnt.toCharArray();

            for (int linePos : linePoss) {
                //methodの開始終了位置を確定
                int s,e;
                for (s = linePos; s > 0; s--) {
                    if (AnalyseUtil.isMatched(lineFileCtnts, s, "public ", true)
                            || AnalyseUtil.isMatched(lineFileCtnts, s, "private ", true)
                            || AnalyseUtil.isMatched(lineFileCtnts, s, "protected ", true)) {
                        break;
                    }
                }
                for (e = linePos; e < lineFileCtnts.length; e++) {
                    if (AnalyseUtil.isMatched(lineFileCtnts, e, "public ", true)
                            || AnalyseUtil.isMatched(lineFileCtnts, e, "private ", true)
                            || AnalyseUtil.isMatched(lineFileCtnts, e, "protected ", true)) {
                        break;
                    }
                }
                int ln = getLineNoByPos(s, lineFileCtnts);
                String ctntKey = path + ":" + ln + "行目";
                if (methodCtnt.containsKey(ctntKey)) {
                    logger.debug(path + "の" + ln + "行目のメソッドが既に保存されていました。");
                    continue;
                }
                methodCtnt.put(ctntKey, lineFileCtnt.substring(s, e));
            }
        }
        logger.debug("検索できた内容数：" + LineNo);
        logger.debug("メソッド数：" + methodCtnt.size());

        StringBuffer allMthdOp = new StringBuffer();
        StringBuffer MthdWithInsOp = new StringBuffer();
        int wrtInsCnt = 0;
        for (String key : methodCtnt.keySet()) {
            String mthdCtnt = methodCtnt.get(key);
            allMthdOp.append(key + FileUtil.LBRK_CRLF);
            allMthdOp.append(mthdCtnt + FileUtil.LBRK_CRLF);
            allMthdOp.append(FileUtil.LBRK_CRLF);
            //if (mthdCtnt.contains(".write(")) {
            if (mthdCtnt.contains("InputStream")) {
                wrtInsCnt++;
                MthdWithInsOp.append(key + FileUtil.LBRK_CRLF);
                MthdWithInsOp.append(mthdCtnt + FileUtil.LBRK_CRLF);
                MthdWithInsOp.append(FileUtil.LBRK_CRLF);
            }
        }
        System.out.println("検索できた内容数：" + LineNo);
        System.out.println("method count:" + methodCtnt.size());
        System.out.println("InputStream write count:" + wrtInsCnt);
        FileUtil.writeFile(allMthdOp.toString(), resStorePath + "wrtMethodResult.txt");
        FileUtil.writeFile(MthdWithInsOp.toString(), resStorePath + "mthdWithInsResult.txt");

    }

    private static int getLineNoByPos(int pos, char[] ctnt) {
        int lnCnt = 1;
        for (int i = 0; i < pos; i++) {
            if (ctnt[i] == '\n') {
                lnCnt++;
            }
        }
        return lnCnt;
    }

}
