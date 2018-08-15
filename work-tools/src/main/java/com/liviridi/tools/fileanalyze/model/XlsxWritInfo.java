package com.liviridi.tools.fileanalyze.model;

public class XlsxWritInfo {

    private String fileName;

    private String searchRes;

    private String foundCtnt;

    private String OldCtnt;

    public XlsxWritInfo() {
        
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSearchRes() {
        return searchRes;
    }

    public void setSearchRes(String searchRes) {
        this.searchRes = searchRes;
    }

    public String getFoundCtnt() {
        return foundCtnt;
    }

    public void setFoundCtnt(String foundCtnt) {
        this.foundCtnt = foundCtnt;
    }

    public String getOldCtnt() {
        return OldCtnt;
    }

    public void setOldCtnt(String oldCtnt) {
        OldCtnt = oldCtnt;
    }

}
