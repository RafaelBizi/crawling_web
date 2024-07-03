package com.axreng.backend.domain.crawl.entity;

import com.axreng.backend.domain.crawl.exception.InvalidKeywordException;

public class Keyword {
    public static final int MIN_LENGHT = 4;
    public static final int MAX_LENGHT = 32;
    private String value;

    public Keyword(String value) {
        if (!isValid(value)) {
            throw new InvalidKeywordException("Keyword length must be between 4 and 32 characters");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String value) {
        return value != null && value.length() >= MIN_LENGHT && value.length() <= MAX_LENGHT;
    }

}
