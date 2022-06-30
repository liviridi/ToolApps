package com.liviridi.tools.xml;

/**
 * xml tree map compare result map
 *
 * @author  yl
 * @version 2018-03-16 03:10:07 
 */
public class XmlTreeDiffMap extends XmlTreeMap {
	public static final short SAME = 0;
    public static final short CHANGED = 1;
    public static final short DELETED = 2;
    public static final short ADDED = 3;

    private static final long serialVersionUID = 1L;
    
    
    private XmlTreeMap destination;
    private boolean isDifferenceDeep = false;
    private boolean differenceChecked = false;

    /** 追加数 */
    private int addedCount = -1;
    /** 変更数 */
    private int changedCount = -1;
    /** 削除数 */
    private int deletedCount = -1;

    /** 削除定義体作成用：削除しようとしているフラグ */
    private boolean toBeDeleted = false;

    /** 削除定義体作成用：削除動作モード */
    private String deleteActionMode = null;

    public XmlTreeDiffMap() {
        super();
    }

    public XmlTreeDiffMap(String tag, String tagAttr, String valueAttr) {
        super(tag, tagAttr, valueAttr);
    }


    public void setDestination(XmlTreeMap destination) {
        this.destination = destination;
    }

    /**
     * 差分判定
     *
     * @return true:差分あり,false:差分無し
     */
    public boolean isDifference() {
        // 自身の値と比較先定義体Mapの値が異なれば、差分ありと判定
        if (this.valueAttr != null && this.destination != null) {
            return this.valueAttr.compareTo(destination.valueAttr) != 0;
        }
        // 自身の値が無く、かつ比較先定義体Mapの値が無い場合は、差分無しと判定
        if (this.valueAttr == null && this.destination != null && this.destination.valueAttr == null) {
            return false;
        }
        // 上記以外は差分ありと判定
        return true;
    }

    /**
     * 削除判定
     *
     */
    public boolean isDeleted() {
        // 自身の値が有り、比較先定義体Mapが無い場合は、削除と判定
        return this.valueAttr != null && destination == null;
    }

    /**
     * 追加判定
     *
     */
    public boolean isAdded() {
        // 比較先が有り、自身の値が無い場合は、追加と判定
        return destination != null && destination.valueAttr != null && this.valueAttr == null;
    }

    /**
     * 差分種別取得
     */
    public short getDiffType() {
        // 追加判定＝追加の場合は「追加」
        if (isAdded()) {
            return ADDED;
        }
        // 削除判定＝削除の場合は「削除」
        if (isDeleted()) {
            return DELETED;
        }
        // 差分判定＝差分有りの場合は「変更」
        if (isDifference()) {
            return CHANGED;
        }
        // 上記以外は「同じ」
        return SAME;
    } 

    /**
     * 差分判定(階層先を含む)
     *
     * @return
     */
    public boolean isDifferenceDeep() {
        // 自身の差分有り判定が差分有りなら、差分ありと判定
        if (this.differenceChecked) {
            return this.isDifferenceDeep;
        }

        if (this.isDifference()) {
            this.isDifferenceDeep = true;
            this.differenceChecked = true;
            return this.isDifferenceDeep;
        }

        // 差分なしの場合は、自身のMapが持つ差分定義体Mapで、差分判定(階層先を含む)が差分有りのMapがあれば差分有りと判定
        for (XmlCompareKey key : this.keySet()) {
            XmlTreeDiffMap info = (XmlTreeDiffMap) this.get(key);
            if (info.isDifferenceDeep()) {
                this.isDifferenceDeep = true;
                break;
            }
        }

        this.differenceChecked = true;
        return this.isDifferenceDeep;
    }

    /**
     * 追加件数取得
     *
     */
    public int getAddedCount() {
        if (this.addedCount >= 0) {
            return this.addedCount;
        }

        this.addedCount = getDiffType() == ADDED ? 1 : 0;
        for (XmlCompareKey key : this.keySet()) {
            XmlTreeDiffMap info = (XmlTreeDiffMap) this.get(key);
            this.addedCount += info.getAddedCount();
        }
        return this.addedCount;
    }

    /**
     * 変更件数取得
     *
     */
    public int getChangedCount() {
        if (this.changedCount >= 0) {
            return this.changedCount;
        }
        this.changedCount = getDiffType() == CHANGED ? 1 : 0;
        for (XmlCompareKey key : this.keySet()) {
            XmlTreeDiffMap info = (XmlTreeDiffMap) this.get(key);
            this.changedCount += info.getChangedCount();
        }
        return this.changedCount;
    }

    /**
     * 削除件数取得
     *
     */
    public int getDeletedCount() {
        if (this.deletedCount >= 0) {
            return this.deletedCount;
        }

        this.deletedCount = getDiffType() == DELETED ? 1 : 0;
        for (XmlCompareKey key : this.keySet()) {
            XmlTreeDiffMap info = (XmlTreeDiffMap) this.get(key);
            this.deletedCount += info.getDeletedCount();
        }
        return this.deletedCount;
    }

    public String getNewValueAttr() {
        return this.destination != null ? this.destination.valueAttr : null;
    }

    public XmlTreeMap getSource() {
        return this;
    }

    public XmlTreeMap getDestination() {
        return this.destination;
    }

    /**
     * 値取得
     */
    public String getValueAttr() {
        // 自身の値が無い場合は、比較先定義体Mapの値を返す
        if (this.valueAttr != null) {
            return this.valueAttr;
        }
        return destination != null ? destination.getValueAttr() : null;
    }

    /**
     * 該当Nodeを削除Nodeとマークする
     */
    public void markAsDelete(String actionMode) {
        if (!toBeDeleted) {
            toBeDeleted = true;
            for (XmlCompareKey key : this.keySet()) {
                XmlTreeDiffMap info = (XmlTreeDiffMap) this.get(key);
                info.markAsDelete(actionMode);
            }
        }
        if (deleteActionMode == null) {
            deleteActionMode = actionMode;
        }
    }

    /**
     * 削除するフラグを返却
     *
     * @return 削除するフラグ
     */
    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    /**
     * 削除するフラグを返却
     *
     * @return 削除するフラグ
     */
    public String getDeleteActionMode() {
        return deleteActionMode;
    }
}
