package com.axreng.backend.application.crawl.dto.request;

public class CrawlRequestDTO {
    private String baseUrl;
    private String keyword;

    public CrawlRequestDTO(String baseUrl, String keyword) {
        this.baseUrl = baseUrl;
        this.keyword = keyword;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getKeyword() {
        return keyword;
    }
}