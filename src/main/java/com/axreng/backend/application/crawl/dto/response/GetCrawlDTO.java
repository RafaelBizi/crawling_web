package com.axreng.backend.application.crawl.dto.response;

import com.axreng.backend.domain.crawl.entity.Crawl;

import java.util.List;

public class GetCrawlDTO {
    private String id;
    private String status;
    private List<String> urls;

    public GetCrawlDTO(Crawl crawl) {
        this.id = crawl.getId();
        this.status = crawl.getStatus().name().toLowerCase();
        this.urls = crawl.getUrls();
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getUrls() {
        return urls;
    }

}
