package com.axreng.backend.domain.crawl.exception;

public class InvalidKeywordException extends IllegalArgumentException {

    public InvalidKeywordException(String message) {
        super(message);
    }
}
