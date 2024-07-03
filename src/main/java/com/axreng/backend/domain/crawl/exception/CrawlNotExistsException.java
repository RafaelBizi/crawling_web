package com.axreng.backend.domain.crawl.exception;

public class CrawlNotExistsException extends RuntimeException {
    public CrawlNotExistsException(String message) {
        super(message);
    }
}