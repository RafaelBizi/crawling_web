package com.axreng.backend.infrastructure.crawl.repository.impl;

import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CrawlRepositoryImpl implements CrawlRepository {

    private final ConcurrentMap<String, Crawl> crawlMap = new ConcurrentHashMap<>();

    @Override
    public Crawl getCrawl(String id) {
        return crawlMap.get(id);
    }

    @Override
    public void saveCrawl(Crawl crawl) {
        crawlMap.put(crawl.getId(), crawl);
    }
}