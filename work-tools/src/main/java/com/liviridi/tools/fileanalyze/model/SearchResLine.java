package com.liviridi.tools.fileanalyze.model;

import com.liviridi.tools.common.util.AnalyseUtil;

public class SearchResLine {

    private String schResult;

    private String filePath;

    private String fileName;

    private Integer lnNo;

    private Integer lnPosPhy;

    private Integer lnPosComp;

    private String lnCtnt;

    public SearchResLine() {

    }

    public SearchResLine(String line, String fileTyp) throws Exception {
        schResult = line;
        filePath = AnalyseUtil.interceptCtnt(line, "C:", fileTyp).get(0).getContent();
        String rplPath = AnalyseUtil.interceptCtnt(line, "\"C:", fileTyp + "\"").get(0).getContent();
        String infoCtnt = AnalyseUtil.interceptCtnt(line, "\"C:", "):").get(0).getContent();
        String[] position = (infoCtnt.replace(rplPath, "")).split(",");
        if (position == null || position.length != 2) {
            throw new Exception("行内容フォーマットエラー");
        }
        // fileName to test
        String[] splitNames = filePath.replace("\\", "/").split("/");
        fileName = splitNames[splitNames.length - 1];
        lnNo = Integer.valueOf(position[0].replace("(", ""));
        lnPosPhy = Integer.valueOf(position[1].replace("):", ""));
        lnPosComp = lnPosPhy - 1;
        lnCtnt = line.replace(infoCtnt, "");
    }

    public String getSchResult() {
        return schResult;
    }

    public void setSchResult(String schResult) {
        this.schResult = schResult;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getLnNo() {
        return lnNo;
    }

    public void setLnNo(Integer lnNo) {
        this.lnNo = lnNo;
    }

    public Integer getLnPosPhy() {
        return lnPosPhy;
    }

    public void setLnPosPhy(Integer lnPosPhy) {
        this.lnPosPhy = lnPosPhy;
    }

    public Integer getLnPosComp() {
        return lnPosComp;
    }

    public void setLnPosComp(Integer lnPosComp) {
        this.lnPosComp = lnPosComp;
    }

    public String getLnCtnt() {
        return lnCtnt;
    }

    public void setLnCtnt(String lnCtnt) {
        this.lnCtnt = lnCtnt;
    }

}
