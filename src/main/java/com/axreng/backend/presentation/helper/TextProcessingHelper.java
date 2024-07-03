package com.axreng.backend.presentation.helper;

// if just visible html texts are needed, this class can be used
public class TextProcessingHelper {
    public static String removeHtmlTagsAndMetadata(String text) {
        return text
                .replaceAll("(?i)<script[^>]*>[\\s\\S]*?<\\/script>", "")
                .replaceAll("(?i)<style[^>]*>[\\s\\S]*?<\\/style>", "")
                .replaceAll("(?i)<meta.*?>", "")
                .replaceAll("(?i)<div class=\"license\" style=\"display: block;\">[\\s\\S]*?<\\/div>", "")
                .replaceAll("(?i)<div class=\"license\"[^>]*>([\\s\\S]*?)<\\/div>", "")
                .replaceAll("\\<.*?\\>", "");
    }
}