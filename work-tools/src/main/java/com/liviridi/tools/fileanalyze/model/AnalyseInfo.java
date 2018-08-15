package com.liviridi.tools.fileanalyze.model;

public class AnalyseInfo {

    /** 探した内容の開始行番号 */
    private int startLineNo;

    /** 探した内容の終了行番号 */
    private int endLineNo;

    /** 探した内容 */
    private String content;

    /**
     * 開始行番号取得
     *
     * @return 開始行番号
     */
    public int getStartLineNo() {
        return startLineNo;
    }

    /**
     * 開始行番号設定
     *
     * @param startLineNo
     *            開始行番号
     */
    public void setStartLineNo(int startLineNo) {
        this.startLineNo = startLineNo;
    }

    /**
     * 終了行番号取得
     *
     * @return 終了行番号
     */
    public int getEndLineNo() {
        return endLineNo;
    }

    /**
     * 終了行番号設定
     *
     * @param endLineNo
     *            終了行番号
     */
    public void setEndLineNo(int endLineNo) {
        this.endLineNo = endLineNo;
    }

    /**
     * 探した内容取得
     *
     * @return 探した内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 探した内容設定
     *
     * @param content
     *            探した内容
     */
    public void setContent(String content) {
        this.content = content;
    }

}
