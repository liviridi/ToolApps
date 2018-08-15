package com.liviridi.tools.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.liviridi.tools.common.log.CommonLogger;
import com.liviridi.tools.fileanalyze.model.AnalyseInfo;

public class AnalyseUtil {

    /** ログ */
    private static CommonLogger logger = CommonLogger.getLogger(AnalyseUtil.class);

    /** 解析単位：行 */
    public static final String ANALYSE_UNIT_LINE = "line";

    /** 解析単位：ファイル */
    public static final String ANALYSE_UNIT_FILE = "file";

    /** default base work path */
    public static final String TEST_BASE_PATH = "/Users/yunli/work/test/NGAIA/";

    /**
     * XML内容インターセプト
     *
     * @param anFile
     *            解析ファイル
     * @param tagName
     *            タッグ名
     * @return インターセプト結果
     * @throws IOException
     *             ファイル読込エラー
     * @throws DocumentException
     *             XML形エラー
     */
    public static String doXmlIntercept(File anFile, String tagName) throws IOException {
        StringBuffer outStr = new StringBuffer();

        Document xmlDocument = null;
        SAXReader reader = new SAXReader();
        InputStream inStr = null;
        InputStreamReader inStrRd = null;
        try {

            inStr = new FileInputStream(anFile);
            inStrRd = new InputStreamReader(inStr);
            xmlDocument = reader.read(inStrRd);
            Iterator<Node> nodeIter = xmlDocument.nodeIterator();
            while (nodeIter.hasNext()) {
                Node node = nodeIter.next();
                if (tagName.equals(node.getName())) {
                    outStr.append(node.asXML());
                    outStr.append(FileUtil.LBRK_CRLF);
                }
            }
        } catch (DocumentException de) {
            logger.error("ファイル：「" + anFile.getAbsolutePath() + "」xml形エラー：" + de.getMessage());
        } finally {
            if (inStr != null) {
                inStr.close();
            }
            if (inStrRd != null) {
                inStrRd.close();
            }
        }

        return outStr.toString();
    }

    /**
     * 内容インターセプト
     *
     * @param anFile
     *            解析ファイル
     * @param anUnit
     *            解析単位
     * @param anStartStr
     *            解析開始文字
     * @param anEndStr
     *            解析終了文字
     * @param encode
     *            ファイルencode
     * @return インターセプト結果
     * @throws IOException
     *             ファイル読込エラー
     */
    public static String doIntercept(File anFile, String anUnit, String anStartStr, String anEndStr, String encode)
            throws IOException {
        String anCtnt = FileUtil.readFileContent(anFile, encode);

        if (anUnit == null) {
            anUnit = ANALYSE_UNIT_LINE;
        }

        StringBuffer outF = new StringBuffer();
        switch (anUnit) {
            case ANALYSE_UNIT_LINE:

                String[] lines = anCtnt.split(FileUtil.LBRK_CRLF);
                logger.info("行数：" + lines.length);
                for (String line : lines) {
                    List<AnalyseInfo> ctnt = interceptCtnt(line, anStartStr, anEndStr);
                    for (int i = 0; i < ctnt.size(); i++) {
                        outF.append(ctnt.get(i).getContent());
                        if (i < ctnt.size() - 1) {
                            outF.append("  ");
                        }
                    }
                    outF.append(FileUtil.LBRK_CRLF);
                }

                break;
            case ANALYSE_UNIT_FILE:
                List<AnalyseInfo> ctnt = interceptCtnt(anCtnt, anStartStr, anEndStr);
                for (int i = 0; i < ctnt.size(); i++) {
                    outF.append(ctnt.get(i).getStartLineNo() + "行目～" + ctnt.get(i).getEndLineNo() + "行目：" + FileUtil.LBRK_CRLF);
                    outF.append(ctnt.get(i).getContent());
                    if (i < ctnt.size() - 1) {
                        outF.append(FileUtil.LBRK_CRLF);
                    }
                }
                outF.append(FileUtil.LBRK_CRLF);
                break;
            default:
                break;
        }
        return outF.toString();
    }

    /**
     * idxからの文字列がマッチするかの判断
     *
     * @param target
     *            マッチ判断内容対象
     * @param index
     *            マッチ判断内容の開始位置
     * @param matchStr
     *            マッチ
     * @return 探した結果
     */
    public static boolean isMatched(char[] target, int index, String matchStr, boolean startEndFlg) {
        boolean matchRes = true;
        if (index >= target.length) {
            return false;
        }

        char[] matchCh = matchStr.toCharArray();
        int matchStartIdx = startEndFlg ? 0 : (matchCh.length - 1);
        int tgtIdx = new Integer(index);

        for (int i = matchStartIdx; i >= 0 && i < matchCh.length && tgtIdx >= 0 && tgtIdx < target.length;) {
            if (target[tgtIdx] != matchCh[i]) {
                if (matchCh[i] == ' ' && (target[tgtIdx] == '\r' || target[tgtIdx] == '\n')) {

                } else {
                    matchRes = false;
                    break;
                }
            }
            if (startEndFlg) {
                i++;
                tgtIdx++;
            } else {
                i--;
                tgtIdx--;
            }
            if (tgtIdx < 0 || tgtIdx >= target.length) {
                matchRes = false;
            }
        }

        return matchRes;
    }

    /**
     * インターセプトの内容を探す
     *
     * @param ctnt
     *            探す対象
     * @param startSymb
     *            開始文字列
     * @param endSymb
     *            終了文字列
     * @return 探した結果
     */
    public static List<AnalyseInfo> interceptCtnt(String ctnt, String startSymb, String endSymb) {
        List<AnalyseInfo> resCtnt = new ArrayList<AnalyseInfo>();
        char[] lineC = ctnt.toCharArray();
        int idx = 0;
        StringBuffer ctntBuff = new StringBuffer();
        boolean started = false;

        int lineNo = 1;
        AnalyseInfo info = new AnalyseInfo();

        while (idx < lineC.length) {
            if (lineC[idx] == '\n') {
                lineNo++;
            }
            if (!started && isMatched(lineC, idx, startSymb, true)) {
                info.setStartLineNo(lineNo);
                started = true;
            }
            if (started) {
                if (isMatched(lineC, idx, startSymb, true)) {
                    info.setStartLineNo(lineNo);
                    ctntBuff = new StringBuffer();
                }
                ctntBuff.append(lineC[idx]);
            }
            if (started && isMatched(lineC, idx, endSymb, false)) {
                info.setEndLineNo(lineNo);
                info.setContent(ctntBuff.toString());
                resCtnt.add(info);
                ctntBuff = new StringBuffer();
                info = new AnalyseInfo();
                started = false;
            }
            idx++;
        }
        return resCtnt;
    }

    /**
     * idに対する作業パスを取得
     * 
     * @return 作業パス
     */
    public static String getWorkPath(String id) {

        return TEST_BASE_PATH + id + "/";
    }

}
