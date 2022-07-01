package com.liviridi.tools.xml;

public class XmlComparor {

    private XmlTreeMap sourceRootMap;
    private XmlTreeMap destinationRootMap;
    
    // ログ
    //private static CommonLogger logger = CommonLogger.getLogger(XmlComparor.class);

    public XmlComparor(String source, String destination) throws Exception {
        sourceRootMap = XmlTreeMap.convert(source);
        destinationRootMap = XmlTreeMap.convert(destination);

        ((XmlTreeDiffMap) sourceRootMap).setDestination(destinationRootMap);
        compareMap((XmlTreeDiffMap) sourceRootMap, destinationRootMap);
    }

    public XmlTreeDiffMap getDifferenceRootMap() {
        return (XmlTreeDiffMap) sourceRootMap;
    }

    public XmlTreeMap getSourceMap() {
        return sourceRootMap;
    }

    public XmlTreeMap getDestinationMap() {
        return destinationRootMap;
    }

    /**
     * comparation
     *
     */
    private XmlTreeDiffMap compareMap(XmlTreeDiffMap source, XmlTreeMap destination) throws Exception {

        if (source != null) {
            for (XmlCompareKey key : source.keySet()) {
                XmlTreeDiffMap srcChild = (XmlTreeDiffMap) source.get(key);
                XmlTreeMap destChild = destination != null ? destination.remove(key) : null;

                if (srcChild.isEmpty() && destination != null && destChild == null) {
                    XmlTreeMap[] srcChildren = source.getSameTagDefinitions(srcChild.getTag());
                    XmlTreeMap[] destChildren = destination.getSameTagDefinitions(srcChild.getTag());
                    if (srcChildren.length == 1 && destChildren.length == 1) {
                        destChild = destination.remove(destChildren[0]);
                    }
                }

                if (destChild != null) {
                    srcChild.setDestination(destChild);
                }

                if (!srcChild.isEmpty() || (destChild != null && !destChild.isEmpty())) {
                    compareMap(srcChild, destChild);
                }
            }
        }

        if (destination != null) {
            for (XmlCompareKey key : destination.keySet()) {
                XmlTreeDiffMap srcChild = source != null ? (XmlTreeDiffMap) source.get(key) : null;
                XmlTreeMap destChild = destination.get(key);

                if (srcChild == null) {
                    srcChild = new XmlTreeDiffMap(destChild.getTag(), destChild.getTagAttr(), null);
                    srcChild.setDestination(destChild);
                    source.put(srcChild, srcChild);
    
                    if (!destChild.isEmpty()) {
                        compareMap(srcChild, destChild);
                    }
                }
            }
        }

        return source;
    }
}
