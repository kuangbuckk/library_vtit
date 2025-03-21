package com.project.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFormatUtil {
    public static String formatUrl(String url) {
        Pattern pattern = Pattern.compile("^/[^/]+(/api/v\\d+/.*?)(?:/\\d+)?/?$");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String extracted = matcher.group(1);
            return extracted.replaceAll("^/|/$", "").replace("/", ".");
        }

        return "";
    }
}
