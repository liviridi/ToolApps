package com.liviridi.tools.xml;
/**
 * xml compare key object
 *
 * @author  yl
 * @version 2018-03-16 03:27:09 
 */
public interface XmlCompareKey extends Comparable<XmlCompareKey> {

    public String getTag();

    public String getTagAttr();

    public String getValueAttr();
}
