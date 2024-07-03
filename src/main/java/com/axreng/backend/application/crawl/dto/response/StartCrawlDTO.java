package com.axreng.backend.application.crawl.dto.response;

public class StartCrawlDTO {
    private String id;

    public StartCrawlDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
