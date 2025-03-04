package com.web.note.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static final Pattern imageId = Pattern.compile("<img.*?/i/g/([0-9a-f]{32}).*?custom-img.*?>");

    public static List<String> extractImageId(String documentContent) {
        List<String> list = new ArrayList<>();

        Matcher matcher = imageId.matcher(documentContent);

        while (matcher.find()) {
            String group = matcher.group(1);
            list.add(group);
        }

        return list;
    }
}
