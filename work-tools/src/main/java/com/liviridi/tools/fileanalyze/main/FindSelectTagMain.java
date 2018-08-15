package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;
import com.liviridi.tools.fileanalyze.model.AnalyseInfo;
import com.liviridi.tools.fileanalyze.model.SearchResLine;
import com.liviridi.tools.fileanalyze.model.XlsxWritInfo;

public class FindSelectTagMain {

    private static CommonLogger logger = CommonLogger.getLogger(FindSelectTagMain.class);

    private static final String TRACER_ID = "artf222389";

    private static final String OUT_XSLX_FILE = "水平展開実施結果(" + TRACER_ID + ").xlsx";

    public static void main(String[] args) throws IOException {

        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        String resStorePath = AnalyseUtil.getWorkPath(TRACER_ID);

        File findResFl = new File(resStorePath + "findResult.txt");

        String resCtnt = FileUtil.readFileContent(findResFl, "SJIS");

        String[] resLines = resCtnt.split(FileUtil.LBRK_CRLF);

        int i = 0;
        List<XlsxWritInfo> wrtInfos = new ArrayList<XlsxWritInfo>();

        for (String resLine : resLines) {
            i++;
            SearchResLine lineInfo = null;
            XlsxWritInfo wrtInfo = new XlsxWritInfo();
            try {
                lineInfo = new SearchResLine(resLine, ".jsp");
            } catch (Exception e) {
                System.out.println(i + "行目エラー、メッセージ：" + e.getMessage());
                System.exit(1);
            }
            wrtInfo.setFileName(lineInfo.getFilePath().replace("\\", "/").replace("C:/Ver_GAIA1_WorkSpace", ""));
            wrtInfo.setSearchRes(lineInfo.getSchResult());
            String wholeTag = findCtntInFile(lineInfo);
            wrtInfo.setFoundCtnt(wholeTag);
            wrtInfos.add(wrtInfo);
        }
        FileInputStream fis = null;
        XSSFWorkbook resBook = null;
        File outputXls = null;
        try {
            outputXls = new File(AnalyseUtil.TEST_BASE_PATH + OUT_XSLX_FILE);
            fis = new FileInputStream(outputXls);
            resBook = new XSSFWorkbook(fis);
            Sheet sheet = resBook.getSheet(TRACER_ID);

            //同ファイル同名項目 異なるファイル同名項目
            wrtCtntToBook(wrtInfos, sheet, resBook, outputXls);
            
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (resBook != null) {
                resBook.close();
            }
        }

    }

    private static void wrtCtntToBook(List<XlsxWritInfo> wrtInfos, Sheet sheet, XSSFWorkbook resBook, File output) throws IOException {
        int rowIdx = 14;
        for (XlsxWritInfo info : wrtInfos) {
            Row ctntRow = sheet.getRow(rowIdx);
            if (ctntRow == null) {
                ctntRow = sheet.createRow(rowIdx);
            }

            for (int cellIdx = 0;cellIdx <= 18; cellIdx++) {
                Cell cell = ctntRow.getCell(cellIdx);
                if (cell == null) {
                    cell = ctntRow.createCell(cellIdx);
                }
                cell.setCellStyle(sheet.getRow(13).getCell(cellIdx).getCellStyle());
                if (cellIdx == 1) {
                    cell.setCellValue(info.getFileName());
                } else if (cellIdx == 2) {
                    cell.setCellValue(info.getSearchRes());
                } else if (cellIdx == 3) {
                    cell.setCellValue(info.getFoundCtnt());
                }
            }

            rowIdx++;
        }

        // シート内容書き込み
        OutputStream os = null;
        try {
            os = new FileOutputStream(output);
            os.flush();
            resBook.write(os);
        } finally {
            if (os != null) {
                os.close();
            }
        }
        
    }

    private static String findCtntInFile(SearchResLine lineInfo) throws IOException {
        String lineCtnt = lineInfo.getLnCtnt();
        String[] tag = findTag(lineCtnt, lineInfo.getLnPosComp());
        String result = null;
        
        if (tag == null) {
            result = "";
        } else {
            String fileCtnt = FileUtil.readFileContent(new File(lineInfo.getFilePath()), "UTF-8");
            List<AnalyseInfo> infos = AnalyseUtil.interceptCtnt(fileCtnt, tag[0], tag[1]);
            for (AnalyseInfo info : infos) {
                if (lineInfo.getLnNo().equals(new Integer(info.getStartLineNo()))) {
                    result = info.getContent();
                    break;
                }
            }
        }
        return result;
    }

    private static String[] findTag(String lineCtnt, Integer lnPos) {
        char[] lineCtntArr = lineCtnt.toCharArray();
        int idx = 0;
        for (idx = lnPos; idx >= 0; idx--) {
            if ('<' == lineCtntArr[idx]) {
                break;
            }
        }
        if (idx == -1) {
            return null;
        }
        StringBuffer tagName = new StringBuffer();
        for (;idx <= lnPos; idx++) {
            tagName.append(lineCtntArr[idx]);
        }
        tagName.append("elect");
        String tagEnd = tagName.toString().replace("<", "</") + ">";
        return new String[] { tagName.toString(), tagEnd };
    }

}
