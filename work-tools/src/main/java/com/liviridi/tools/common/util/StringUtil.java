package com.liviridi.tools.common.util;

public class StringUtil {

    /**
     * check if the string is empty
     * @param str the string to check
     * @return true if empty
     */
    public static boolean isEmptyStr(String str) {
        return str == null || str.trim().length() == 0;
    }

}
