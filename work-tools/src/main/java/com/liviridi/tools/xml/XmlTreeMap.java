package com.liviridi.tools.xml;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.liviridi.tools.common.util.FileUtil;
import com.liviridi.tools.common.util.StringUtil;

/**
 * xml tree map
 *
 * @author  yl
 * @version 2018-03-16 03:10:51 
 */
public class XmlTreeMap extends TreeMap<XmlCompareKey, XmlTreeMap> implements XmlCompareKey {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * タグ
     */
    protected String tag;
    
    /**
     * タグ名称
     */
    protected String tagAttr;
    
    /**
     * タグ値
     */
    protected String valueAttr;

    /**
     * Documentオブジェクトのノード
     */
    private Node node;

    /**
     * TreeMapの元Documentオブジェクト
     */
    private Document savedDoc = null;

    /**
     * current node's parent node map
     */
    private XmlTreeMap parentMap = null;

    /**
     * current node's parent node map
     */
    private String xpath = null;

    public XmlTreeMap() {
        this.tag = null;
        this.tagAttr = null;
        this.valueAttr = null;
    }

    public XmlTreeMap(String tag, String tagAttr, String valueAttr) {
        this.tag = tag;
        this.tagAttr = tagAttr;
        this.valueAttr = valueAttr;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setDocument(Document document) {
        this.savedDoc = document;
    }

    public Document getDocument() {
        return this.savedDoc;
    }

    public void setParentMap(XmlTreeMap parent) {
        this.parentMap = parent;
    }

    public XmlTreeMap getParentMap() {
        return this.parentMap;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getXpath() {
        return this.xpath;
    }

    /**
     * compare(only tag and value)
     */
    public int compareTo(XmlCompareKey info) {
        int result;

        if ((result = this.getTag().compareTo(info.getTag())) != 0) {
            return result;
        }
        if (this.getValueAttr() == null) {
            return info.getValueAttr() == null ? 0 : 1;
        }
        return this.getValueAttr().compareTo(info.getValueAttr());
    }

    public XmlTreeMap[] getSameTagDefinitions(String tag) {
        ArrayList<XmlTreeMap> maps = new ArrayList<XmlTreeMap>();

        if (tag != null) {
            for (XmlCompareKey key : this.keySet()) {
                XmlTreeMap info = (XmlTreeDiffMap) this.get(key);
                if (info != null && tag.equals(info.getTag())) {
                    maps.add(info);
                }
            }
        }
        return maps.toArray(new XmlTreeMap[maps.size()]);
    }

    public boolean isRootMap() {
        return savedDoc != null && parentMap == null;
    }

    public String getTag() {
        return this.tag;
    }

    public String getTagAttr() {
        return this.tagAttr;
    }

    public String getValueAttr() {
        return this.valueAttr;
    }

    public String asXml() {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        return xmlHeader + FileUtil.LBRK_LF + getNodeXml(this, "");
    }

    private String getNodeXml(XmlTreeMap currentNode, String lastTab) {
        String tab = "    ";
        StringBuffer xmlBuffer = new StringBuffer();

        xmlBuffer.append(lastTab);
        xmlBuffer.append("<");
        xmlBuffer.append(currentNode.tag);
        xmlBuffer.append(currentNode.tagAttr == null ? "" : " tag=\"" + currentNode.tagAttr + "\"");
        xmlBuffer.append(currentNode.valueAttr == null ? "" : " value=\"" + currentNode.valueAttr + "\"");
        if (currentNode.size() == 0) {
            xmlBuffer.append("/>");
        } else {
            xmlBuffer.append(">");
            xmlBuffer.append(FileUtil.LBRK_LF);
            for (XmlCompareKey key : currentNode.keySet()) {
                xmlBuffer.append(getNodeXml(currentNode.get(key), tab + lastTab));
            }
            xmlBuffer.append(lastTab);
            xmlBuffer.append("</");
            xmlBuffer.append(currentNode.tag);
            xmlBuffer.append(">");
        }
        xmlBuffer.append(FileUtil.LBRK_LF);
        return xmlBuffer.toString();
    }

    public static XmlTreeMap convert(String file) throws Exception {
        XmlTreeMap map;
        if(!StringUtil.isEmptyStr(file)){
            StringReader sr = new StringReader(file); 
            InputSource is = new InputSource(sr);
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            try{
                document = builder.parse(is);
            } catch (Throwable th){
                throw th;
            }
            Element element = document.getDocumentElement();
            map = createDefinitionMap(element);
            convert(map, element.getFirstChild());
            map.setDocument(document);
            map.setNode(element);
        } else {
            map  = new XmlTreeDiffMap();
        }
        return map;
    }

    /**
     * XML node convert into map
     *
     */
    private static void convert(XmlTreeMap map, Node node) throws Exception {
        XmlTreeMap childKey, childMap;
        Node nextNode;

        nextNode = node;
        while (nextNode != null) {
            if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
                childKey = childMap = createDefinitionMap(nextNode);
                convert(childMap, nextNode.getFirstChild());
                map.put(childKey, childMap);
                childMap.setNode(nextNode);
                childMap.setParentMap(map);
                String xpath = (map.isRootMap() ? "" : map.getXpath()) + "." + childMap.getTag();
                childMap.setXpath(xpath);
            }
            nextNode = nextNode.getNextSibling();
        }
    }

    private static XmlTreeMap createDefinitionMap(Node node) throws Exception {
        Node attr;

        if (node == null) {
            return null;
        }

        String tag = node.getNodeName();
        String tagAttr = null;
        String valueAttr = null;

        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap != null) {
            attr = nodeMap.getNamedItem("tag");
            if (attr != null) {
                tagAttr = attr.getNodeValue();
            }

            attr = nodeMap.getNamedItem("value");
            if (attr != null) {
                valueAttr = attr.getNodeValue();
            }
        }

        return new XmlTreeMap(tag, tagAttr, valueAttr);
    }

    @Override
    public XmlTreeMap remove(Object key) {
        if (this.get(key) != null && this.get(key).node != null) {
            Node nodeToRemove = this.get(key).node;
            if (nodeToRemove != null) {
                nodeToRemove.getParentNode().removeChild(nodeToRemove);
            }
        }
        return super.remove(key);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("{");
        result.append(tag + ":" + valueAttr + "=");
        for (XmlCompareKey key : this.keySet()) {
            XmlTreeMap info = (XmlTreeDiffMap) this.get(key);
            result.append(info.toString());
        }
        result.append("}");
        return result.toString();
    }
}
