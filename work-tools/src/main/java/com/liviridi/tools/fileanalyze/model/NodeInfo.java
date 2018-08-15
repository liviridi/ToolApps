package com.liviridi.tools.fileanalyze.model;

public class NodeInfo {
    private String name;

    private String ifTest;

    private String path;

    private String value;

    private String xml;

    public NodeInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIfTest() {
        return ifTest;
    }

    public void setIfTest(String ifTest) {
        this.ifTest = ifTest;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
