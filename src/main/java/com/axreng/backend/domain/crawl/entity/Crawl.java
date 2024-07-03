package com.axreng.backend.domain.crawl.entity;

import java.util.List;
import java.util.Random;

public class Crawl {

    private final static String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();
    private String id;
    private CrawlStatus status;
    private List<String> urls;

    public Crawl(List<String> urls) {
        this.id = generateId();
        this.status = CrawlStatus.ACTIVE;
        this.urls = urls;
    }

    private String generateId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int character = RANDOM.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String getId() {
        return id;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setStatus(CrawlStatus status) {
        this.status = status;
    }

    public CrawlStatus getStatus() {
        return status;
    }
}