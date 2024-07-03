package com.axreng.backend.domain.crawl.service;

import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;

public interface Crawler {
    void crawlWebPage(Keyword keyword, Crawl crawl);
}