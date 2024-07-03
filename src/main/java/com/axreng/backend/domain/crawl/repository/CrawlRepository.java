package com.axreng.backend.domain.crawl.repository;

import com.axreng.backend.domain.crawl.entity.Crawl;

public interface CrawlRepository {

    Crawl getCrawl(String id);
    void saveCrawl(Crawl crawl);
}
