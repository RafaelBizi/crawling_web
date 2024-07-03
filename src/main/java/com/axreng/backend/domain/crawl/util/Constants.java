package com.axreng.backend.domain.crawl.util;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern HREF_PATTERN = Pattern.compile("href=\"([^\"]*?)\"", Pattern.CASE_INSENSITIVE);
    public static final String VALID_CHARS_URL_REGEX = "^[a-zA-Z0-9-._~:/?#\\[\\]@!$&'()*+,;=%]*$";
    public static final String HTTP = "http";
    public static final String HTML_EXTENSION = "html";
    public static final String HTML_TAG_REGEX = "<.*?>";
    public static final String MAILTO_PREFIX = "mailto:";
    public static final String JAVASCRIPT_PREFIX = "javascript:";

}
