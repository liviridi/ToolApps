package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;
import com.liviridi.tools.fileanalyze.model.AnalyseInfo;
import com.liviridi.tools.fileanalyze.model.NodeInfo;

public class FindSetStrArrMain {

    private static CommonLogger logger = CommonLogger.getLogger(FindSetStrArrMain.class);

    /**
     * find str array in set method
     *
     * @param args
     * @throws IOException
     *             ファイル読込エラー
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {

        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        String resStorePath = AnalyseUtil.getWorkPath("artf221579");

        File findResFl = new File(resStorePath + "findResult.txt");

        String resCtnt = FileUtil.readFileContent(findResFl, "SJIS");

        String[] resLines = resCtnt.split(FileUtil.LBRK_CRLF);

        int LineNo = 0;
        Map<String, String> methodCtnt = new LinkedHashMap<String, String>();
        List<NodeInfo> wrtXlsInfos = new ArrayList<NodeInfo>();
        for (String resLine : resLines) {
            LineNo++;
            String path = AnalyseUtil.interceptCtnt(resLine, "C:", ".java").get(0).getContent();
            String lineCtnt = resLine.replace(AnalyseUtil.interceptCtnt(resLine, "\"C:", "):").get(0).getContent(), "");

            logger.debug(path + "開始 lineCtnt:" + lineCtnt);
            if (lineCtnt.contains("import ")) {
                continue;
            }
            String lineFileCtnt = FileUtil.readFileContent(new File(path), "UTF-8");
            System.out.println(path + "開始");

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
                s = linePos;
//                for (s = linePos; s > 0; s--) {
//                    if (AnalyseUtil.isMatched(lineFileCtnts, s, "public ", true)
//                            || AnalyseUtil.isMatched(lineFileCtnts, s, "private ", true)
//                            || AnalyseUtil.isMatched(lineFileCtnts, s, "protected ", true)) {
//                        break;
//                    }
//                }
                for (e = linePos + 5; e < lineFileCtnts.length; e++) {
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
                String mapctnt = lineFileCtnt.substring(s, e);
                methodCtnt.put(ctntKey, mapctnt.substring(0, mapctnt.lastIndexOf("}") + 1) + "\n");
                NodeInfo wrtInfo = new NodeInfo();
                wrtInfo.setPath(ctntKey);
                wrtInfo.setValue(methodCtnt.get(ctntKey));

                List<AnalyseInfo> params = AnalyseUtil.interceptCtnt(lineCtnt, "(String[] ", ")");
                if (params.size() != 1) {
                    logger.debug("今の[" + lineCtnt + "]はmethod じゃない");
                } else {
                    String mthdParam = params.get(0).getContent();
                    String paramName = mthdParam.replace("(String[] ", "");
                    paramName = paramName.substring(0, paramName.length() - 1);
                    wrtInfo.setName(findSrcParam(lineFileCtnt, paramName));
                }
                wrtXlsInfos.add(wrtInfo);
            }
        }
        logger.debug("検索できた内容数：" + LineNo);
        logger.debug("メソッド数：" + methodCtnt.size());

        StringBuffer allMthdOp = new StringBuffer();
//        StringBuffer MthdWithInsOp = new StringBuffer();
//        int wrtInsCnt = 0;
        for (String key : methodCtnt.keySet()) {
            String mthdCtnt = methodCtnt.get(key);
            allMthdOp.append(key + FileUtil.LBRK_CRLF);
            allMthdOp.append(mthdCtnt + FileUtil.LBRK_CRLF);
            allMthdOp.append(FileUtil.LBRK_CRLF);

            //if (mthdCtnt.contains(".write(")) {
//            if (mthdCtnt.contains("InputStream")) {
//                wrtInsCnt++;
//                MthdWithInsOp.append(key + FileUtil.LBRK_CRLF);
//                MthdWithInsOp.append(mthdCtnt + FileUtil.LBRK_CRLF);
//                MthdWithInsOp.append(FileUtil.LBRK_CRLF);
//            }
        }
        System.out.println("検索できた内容数：" + LineNo);
        System.out.println("method count:" + methodCtnt.size());
//        System.out.println("InputStream write count:" + wrtInsCnt);
        FileUtil.writeFile(allMthdOp.toString(), resStorePath + "wrtMethodResult.txt");
        writeXls(wrtXlsInfos);
//        FileUtil.writeFile(MthdWithInsOp.toString(), resStorePath + "mthdWithInsResult.txt");

    }

    private static String findSrcParam(String lineFileCtnt, String paramName) {
        String[] lines = lineFileCtnt.split("\n");
        int lnNo = 0;
        String resLine = null;
        for (String line : lines) {
            lnNo++;
            System.out.println(lnNo + "行：");
            if (line.length() < "private ".length()) {
                continue;
            }
            List<AnalyseInfo> params = AnalyseUtil.interceptCtnt(line, "private ", paramName + ";");
            if (params.size() == 1) {
                resLine = line;
                break;
            }
        }
        return lnNo + "行目：" + resLine;
    }

    private static void writeXls(List<NodeInfo> wrtXlsInfos) throws IOException {
        XSSFWorkbook resBook = null;
        FileInputStream fis = null;
        // シート内容書き込み
        OutputStream os = null;

        try {
            fis = new FileInputStream(new File("/Users/yunli/work/test/NGAIA/水平展開実施結果(artf221579).xlsx"));
            resBook = new XSSFWorkbook(fis);
            Sheet sheet = resBook.getSheet("artf221579");
            
            int rowIdx = 13;
            for (NodeInfo wrtInfo : wrtXlsInfos) {
                Row ctntRow = sheet.getRow(rowIdx);
                if (ctntRow == null) {
                    ctntRow = sheet.createRow(rowIdx);
                }
                Cell nameCell = ctntRow.getCell(1);
                if (nameCell == null) {
                    nameCell = ctntRow.createCell(1);
                }
                Cell valueCell = ctntRow.getCell(2);
                if (valueCell == null) {
                    valueCell = ctntRow.createCell(2);
                }
                Cell paramCell = ctntRow.getCell(3);
                if (paramCell == null) {
                    paramCell = ctntRow.createCell(3);
                }
                nameCell.setCellStyle(sheet.getRow(12).getCell(1).getCellStyle());
                valueCell.setCellStyle(sheet.getRow(12).getCell(1).getCellStyle());
                paramCell.setCellStyle(sheet.getRow(12).getCell(1).getCellStyle());
                nameCell.setCellValue(wrtInfo.getPath());
                valueCell.setCellValue(wrtInfo.getValue());
                paramCell.setCellValue(wrtInfo.getName());
                rowIdx++;
            }
            os = new FileOutputStream(new File("/Users/yunli/work/test/NGAIA/水平展開実施結果(artf221579).xlsx"));
            os.flush();
            resBook.write(os);
        } finally {
            if (os != null) {
                os.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (resBook != null) {
                resBook.close();
            }
        }
        
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
