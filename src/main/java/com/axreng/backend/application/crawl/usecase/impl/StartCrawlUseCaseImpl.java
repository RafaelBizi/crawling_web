package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlRequestDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class StartCrawlUseCaseImpl implements StartCrawlUseCase {
    private final CrawlRepository crawlRepository;
    private final Crawler crawler;

    public StartCrawlUseCaseImpl(CrawlRepository crawlRepository, Crawler crawler) {
        this.crawlRepository = crawlRepository;
        this.crawler = crawler;
    }

    @Override
    public StartCrawlDTO execute(CrawlRequestDTO requestDTO) {
        Keyword keyword = new Keyword(requestDTO.getKeyword());
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        crawlRepository.saveCrawl(crawl);

        CompletableFuture.runAsync(() -> 
            crawler.crawlWebPage(requestDTO.getBaseUrl(), keyword, crawl));

        return new StartCrawlDTO(crawl.getId());
    }

}