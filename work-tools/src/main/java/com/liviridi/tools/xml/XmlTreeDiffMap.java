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

    private int addedCount = -1;
    private int changedCount = -1;
    private int deletedCount = -1;

    /** delete mark */
    private boolean toBeDeleted = false;

    /** delete action */
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

    public boolean isDifference() {
        if (this.valueAttr != null && this.destination != null) {
            return this.valueAttr.compareTo(destination.valueAttr) != 0;
        }
        if (this.valueAttr == null && this.destination != null && this.destination.valueAttr == null) {
            return false;
        }
        return true;
    }

    public boolean isDeleted() {
        return this.valueAttr != null && destination == null;
    }

    public boolean isAdded() {
        return destination != null && destination.valueAttr != null && this.valueAttr == null;
    }

    /**
     * difference result
     */
    public short getDiffType() {
        if (isAdded()) {
            return ADDED;
        }
        if (isDeleted()) {
            return DELETED;
        }
        if (isDifference()) {
            return CHANGED;
        }
        return SAME;
    } 

    public boolean isDifferenceDeep() {
        if (this.differenceChecked) {
            return this.isDifferenceDeep;
        }

        if (this.isDifference()) {
            this.isDifferenceDeep = true;
            this.differenceChecked = true;
            return this.isDifferenceDeep;
        }

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
     * value attribute
     */
    public String getValueAttr() {
        // 自身の値が無い場合は、比較先定義体Mapの値を返す
        if (this.valueAttr != null) {
            return this.valueAttr;
        }
        return destination != null ? destination.getValueAttr() : null;
    }

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
     * to delete mark
     *
     * @return mark
     */
    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    /**
     * delete action
     *
     * @return delete action
     */
    public String getDeleteActionMode() {
        return deleteActionMode;
    }

}
