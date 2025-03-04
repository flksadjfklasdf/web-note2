package com.web.note.util;

public class SQLUtil {
    public SQLUtil() {
    }

    public static String escapeSqlSpecialChar(String str) {
        return str.trim().replaceAll("\\s", "").replace("\\", "\\\\\\\\").replace("_", "\\_").replace("\\'", "\\\\'").replace("%", "\\%").replace("*", "\\*");
    }
}
