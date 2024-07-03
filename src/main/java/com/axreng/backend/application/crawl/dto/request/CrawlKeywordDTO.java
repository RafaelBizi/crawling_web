package com.axreng.backend.application.crawl.dto.request;

public class CrawlKeywordDTO {
    private String keyword;

    public CrawlKeywordDTO(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}