package com.liviridi.tools.fileanalyze.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.liviridi.tools.common.constants.SystemConstants;
import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.common.util.AnalyseUtil;
import com.liviridi.tools.common.util.FileUtil;
import com.liviridi.tools.fileanalyze.model.AnalyseInfo;
import com.liviridi.tools.fileanalyze.model.NodeInfo;

public class ChkJSPMain {

    private static CommonLogger logger = CommonLogger.getLogger(ChkJSPMain.class);

    /**
     * jsp id check
     *
     * @param args
     * @throws IOException
     *             ファイル読込エラー
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {

        CommonLogger.initConfig(SystemConstants.LOG_CONFIG_PATH);

        Document pageXml = getXmlDoc("/Share/conf/common/part/east/page-config.xml");
        Element page = getPage("SC_A03_04", pageXml);
        Map<String, String> partsJspMap = createPartJspMap();

        Iterator<Element> pageIter = page.elementIterator();

        List<String> jspPaths = new ArrayList<String>();
        while (pageIter.hasNext()) {
            Element pagePart = pageIter.next();
            String ppLvl = pagePart.attributeValue("level");
            String ppId = pagePart.attributeValue("id");

            if ("1".equals(ppLvl)) {
                continue;
            }
            String keylvl = ppId + "," + ppLvl;
            if (!partsJspMap.containsKey(keylvl)) {

                String keynull = ppId + "," + null;
                if (!partsJspMap.containsKey(keynull)) {
                    String[] partIdp = ppId.split("\\.");
                    if ("part".equals(partIdp[1])) {
                        System.out.println("error:[" + ppId + "] + lvl:2 と + null 両方とも値が見つかりません。");
                    }
                    continue;
                }
                keylvl = keynull;
            }
            // logger.debug("info:[" + ppId + "] src:" + partsJspMap.get(keylvl));
            jspPaths.add(partsJspMap.get(keylvl));
        }
        System.out.println(jspPaths.size());
        // Map<String, String> jspNameMap = analyseJSP(jspPaths);
        FileInputStream fis = null;
        XSSFWorkbook resBook = null;
        try {
            //fis = new FileInputStream(new File("/Users/yunli/work/test/NGAIA/log/水平展開実施結果(同名項目).xlsx"));
            fis = new FileInputStream(new File("/Users/yunli/work/test/NGAIA/log/水平展開実施結果(同名項目)SC_A03_04.xlsx"));
            resBook = new XSSFWorkbook(fis);
            Sheet sheet = resBook.getSheet("異なるファイル同名項目");

            //同ファイル同名項目 異なるファイル同名項目
            logger.debug("---------------------name compare----------------------");
            int rowIdx = 12;
            List<Map<String, NodeInfo>> maps = analyseJSP(jspPaths, resBook);
            for (int i = 0; i < maps.size(); i++) {
                for (int j = i + 1; j < maps.size(); j++) {
                    if (i == j) {
                        System.out.println("error:coding err");
                        continue;
                    }
                    Map<String, NodeInfo> srcMap = maps.get(i);
                    Map<String, NodeInfo> tarMap = maps.get(j);
                    for (String key : tarMap.keySet()) {
                        if (srcMap.containsKey(key)) {
                            logger.debug("異なるファイルに同名項目[" + key + "]が存在。ファイル：[" + srcMap.get(key).getPath() + "]と["
                                    + tarMap.get(key).getPath() + "];内容:[" + srcMap.get(key).getValue() + "]と["
                                    + tarMap.get(key).getValue() + "]");
                            wrtCtntToBook(rowIdx, tarMap.get(key), srcMap.get(key), sheet, resBook, false);
                            rowIdx++;
                        }
                    }
                }
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (resBook != null) {
                resBook.close();
            }
        }

    }

    private static void wrtCtntToBook(int rowIdx, NodeInfo target, NodeInfo src, Sheet sheet, XSSFWorkbook resBook, boolean isSame) throws IOException {
        Row ctntRow = sheet.getRow(rowIdx);
        if (ctntRow == null) {
            ctntRow = sheet.createRow(rowIdx);
        }
        Cell nameCell = ctntRow.getCell(1);
        if (nameCell == null) {
            nameCell = ctntRow.createCell(1);
        }
        Cell pathCell = ctntRow.getCell(2);
        if (pathCell == null) {
            pathCell = ctntRow.createCell(2);
        }
        Cell valueCell = ctntRow.getCell(3);
        if (valueCell == null) {
            valueCell = ctntRow.createCell(3);
        }
        nameCell.setCellStyle(sheet.getRow(11).getCell(1).getCellStyle());
        pathCell.setCellStyle(sheet.getRow(11).getCell(1).getCellStyle());
        valueCell.setCellStyle(sheet.getRow(11).getCell(1).getCellStyle());
        nameCell.setCellValue(target.getName());
        if (isSame) {
            pathCell.setCellValue("[" + target.getPath() + "]");
        } else {
            pathCell.setCellValue("[" + target.getPath() + "]\nと\n[" + src.getPath() + "]");
        }
        valueCell.setCellValue("[" + target.getValue() + "]\nと\n[" + src.getValue() + "]");
        // シート内容書き込み
        OutputStream os = null;

        try {
            //os = new FileOutputStream(new File("/Users/yunli/work/test/NGAIA/log/水平展開実施結果(同名項目).xlsx"));
            os = new FileOutputStream(new File("/Users/yunli/work/test/NGAIA/log/水平展開実施結果(同名項目)SC_A03_04.xlsx"));
            os.flush();
            resBook.write(os);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    private static List<Map<String, NodeInfo>> analyseJSP(List<String> jspPaths, XSSFWorkbook resBook) throws IOException, DocumentException {
        // String jpath = "/Ver_GAIA1_WorkSpace/NGN/NGNOPS/web/WEB-INF/jsp/apm/SC_A03.part.dev_info_edit.jsp";
        List<Map<String, NodeInfo>> maps = new ArrayList<Map<String, NodeInfo>>();

        logger.debug("---------------------=====----------------------");
        for (String jpath : jspPaths) {
            System.out.println("----" + jpath + ":開始----");
            logger.debug("----" + jpath + ":開始----");
            StringBuffer xmlCtnt = new StringBuffer();
            xmlCtnt.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            xmlCtnt.append(FileUtil.LBRK_LF);
            xmlCtnt.append("<root>");
            String ctnt = FileUtil.readFileContent(new File(jpath), "UTF-8");
            xmlCtnt.append(FileUtil.LBRK_LF);
            xmlCtnt.append(ctnt.replace("<%--", "<!--").replace("--%>", "-->").replace(":", "").replace("&", "8")
                    .replace("\"", "\" ").replace("=\" ", "=\"").replace("\t", "    "));
            xmlCtnt.append(FileUtil.LBRK_LF);
            xmlCtnt.append("</root>");
            // FileUtil.writeFile(replaceInput(xmlCtnt.toString()), "/Users/yunli/work/test/NGAIA/log/xml.log");

            Document jspDoc = DocumentHelper.parseText(replaceInput(xmlCtnt.toString()));
            Element root = jspDoc.getRootElement();

            // Map<String, String> namesMap = new LinkedHashMap<String, String>();
            List<NodeInfo> jspNameInfo = new ArrayList<NodeInfo>();
            Map<String, NodeInfo> map = new LinkedHashMap<String, NodeInfo>();

            wrtNameInfo(jpath, root, null, jspNameInfo);
            Sheet sheet = resBook.getSheet("同ファイル同名項目");
            int rowIdx = 12;
            for (NodeInfo key : jspNameInfo) {
                // logger.debug(key.getName() + "::" + key.getIfTest() + "→→→→" + key.getPath() + ":[" + key.getValue()
                // + "]");
                if (map.containsKey(key.getName())) {
                    logger.debug("同ファイルに同名項目:[" + key.getName() + "][" + key.getValue() + "]");
                    wrtCtntToBook(rowIdx, key, map.get(key.getName()), sheet, resBook, true);
                    rowIdx++;
                    continue;
                }
                map.put(key.getName(), key);
            }

            maps.add(map);

        }

        return maps;

    }

    private static void wrtNameInfo(String jspPath, Element elmt, String ifTest, List<NodeInfo> namesInfo) {
        Iterator<Element> jspEleIter = elmt.elementIterator();

        String mkey = null;
        if (ifTest != null) {
            mkey = ifTest;
        }
        String nextIfTest = mkey;
        while (jspEleIter.hasNext()) {
            Element jspNode = jspEleIter.next();
            if (jspNode.attributeValue("name") != null) {
                NodeInfo nodeI = new NodeInfo();
                nodeI.setIfTest(mkey);
                nodeI.setName(jspNode.attributeValue("name"));
                nodeI.setPath(jspPath);
                nodeI.setValue(getAttr(jspNode));
                nodeI.setXml(jspNode.asXML());
                namesInfo.add(nodeI);
            }
            if ("cif".equals(jspNode.getName())) {
                nextIfTest = nextIfTest == null ? "" : nextIfTest;
                nextIfTest += "[" + jspNode.attributeValue("test") + "]";
            }
            wrtNameInfo(jspPath, jspNode, nextIfTest, namesInfo);
        }

    }

    private static String getAttr(Element jspNode) {
        StringBuffer res = new StringBuffer();
        res.append("<");
        res.append(jspNode.getName());
        res.append(" ");
        Iterator<Attribute> nodeAttrIter = jspNode.attributeIterator();
        while (nodeAttrIter.hasNext()) {
            Attribute attr = nodeAttrIter.next();
            res.append(attr.getName());
            res.append("=\"");
            res.append(attr.getText());
            res.append("\"");
            res.append(" ");
        }
        res.append(">");

        return res.toString();
    }

    private static String replaceInput(String xml) {

        List<AnalyseInfo> rts = AnalyseUtil.interceptCtnt(xml, "<%", "%>");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = "";
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<%", "%/>");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = "";
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<!--", "-->");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = "";
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<script ", "</script>");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = "";
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<tr", ">");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = inputCtntBf.replace(">", "/>").replace("//>", "/>");
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<image ", ">");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = inputCtntBf.replace(">", "/>").replace("//>", "/>");
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<IMG ", ">");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = inputCtntBf.replace(">", "/>").replace("//>", "/>");
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<textarea ", "/>");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = inputCtntBf;
            if (inputCtntBf.contains("disabled") && !inputCtntBf.contains("disabled=")) {
                inputCtntAf = inputCtntAf.replace("disabled", "");
            }
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        rts = AnalyseUtil.interceptCtnt(xml, "<cout ", "/>");
        for (AnalyseInfo rt : rts) {
            String inputCtntBf = rt.getContent();
            String inputCtntAf = "";
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        List<AnalyseInfo> inputs = AnalyseUtil.interceptCtnt(xml, "<input ", ">");
        for (AnalyseInfo input : inputs) {
            String inputCtntBf = input.getContent();
            String inputCtntAf = inputCtntBf.replace(">", "/>").replace("//>", "/>");
            if (inputCtntBf.contains("checked") && !inputCtntBf.contains("checked=")) {
                inputCtntAf = inputCtntAf.replace("checked", "");
            }
            if (inputCtntBf.contains("disabled") && !inputCtntBf.contains("disabled=")) {
                inputCtntAf = inputCtntAf.replace("disabled", "");
            }
            xml = xml.replace(inputCtntBf, inputCtntAf);
        }
        return xml.replace("<BR>", "<BR/>").replace("<br>", "<br/>").replace("</tr>", "<tr/>");
    }

    private static Map<String, String> createPartJspMap() throws IOException, DocumentException {
        Map<String, String> jspMap = new LinkedHashMap<String, String>();
        Document partXml = getXmlDoc("/Ver_GAIA1_WorkSpace/NGN/Share/conf/common/part/part-config.xml");

        Element parts = partXml.getRootElement();

        Iterator<Element> partIter = parts.elementIterator();

        while (partIter.hasNext()) {
            Element part = partIter.next();
            if (part.getNodeType() == Element.COMMENT_NODE) {
                continue;
            }
            String lvl = part.attributeValue("level");
            String id = part.attributeValue("id");
            String jsp = part.attributeValue("jsp");
            String mkey = "";
            if (jsp == null) {
                Iterator<Element> jspIter = part.elementIterator();
                while (jspIter.hasNext()) {
                    Element jspE = jspIter.next();
                    if (jspE.getNodeType() == Element.COMMENT_NODE) {
                        continue;
                    }
                    if (!"jsp".equals(jspE.getName())) {
                        System.out.println("warn:[" + id + "]にjsp以外のタグも含まれている");
                        continue;
                    }
                    jsp = jspE.attributeValue("src");
                    lvl = jspE.attributeValue("level");
                    mkey = id + "," + lvl;
                    if (jspMap.containsKey(mkey)) {
                        System.out.println("warn:[" + mkey + "]が既に登録されています。");
                        if (jspMap.get(mkey).equals(jsp)) {
                            System.out.println("warn:既に登録された[" + mkey + "]値が同じで、問題ない");
                        } else {
                            System.out.println("error:既に登録された[" + mkey + "]値が違うので、チェックしなさい");
                        }
                    }
                    if (jsp == null) {
                        System.out.println("error:[" + mkey + "]のjspがnull");
                    }
                }
            } else {
                mkey = id + "," + lvl;
                if (jspMap.containsKey(mkey)) {
                    System.out.println("warn:[" + mkey + "]が既に登録されています。");
                    if (jspMap.get(mkey).equals(jsp)) {
                        System.out.println("warn:既に登録された[" + mkey + "]値が同じで、問題ない");
                    } else {
                        System.out.println("error:既に登録された[" + mkey + "]値が違うので、チェックしなさい");
                    }
                }
            }
            jspMap.put(mkey, "/Ver_GAIA1_WorkSpace/NGN/NGNOPS/web" + jsp);
        }
        return jspMap;
    }

    private static Element getPage(String id, Document pageXml) {
        Element root = pageXml.getRootElement();
        Element res = null;
        Iterator<Element> nodeIter = root.elementIterator();
        int cnt = 0;
        while (nodeIter.hasNext()) {
            Element page = nodeIter.next();
            cnt++;

            if (id.equals(page.attributeValue("id"))) {
                if (res != null) {
                    System.out.println("重複");
                }
                res = page;
            }
        }
        System.out.println(cnt);
        return res;
    }

    private static Document getXmlDoc(String filePath) throws IOException, DocumentException {
        Document xmlDocument = null;
        SAXReader reader = new SAXReader();
        InputStream inStr = null;
        InputStreamReader inStrRd = null;
        try {

            inStr = new FileInputStream(filePath);
            inStrRd = new InputStreamReader(inStr);
            xmlDocument = reader.read(inStrRd);
        } catch (DocumentException de) {
            System.out.println("error:ファイル：「" + filePath + "」xml形エラー：" + de.getMessage());
            throw de;
        } finally {
            if (inStr != null) {
                inStr.close();
            }
            if (inStrRd != null) {
                inStrRd.close();
            }
        }

        return xmlDocument;
    }

}
