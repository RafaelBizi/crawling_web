package com.axreng.backend.domain.crawl.entity;

public enum CrawlStatus {
    ACTIVE("active"),
    DONE("done");

    private final String status;

    CrawlStatus(String status) {
        this.status = status;
    }

}