package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlKeywordDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;
import org.slf4j.Logger;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartCrawlUseCaseImpl implements StartCrawlUseCase{
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(StartCrawlUseCaseImpl.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final CrawlRepository crawlRepository;
    private final Crawler crawler;

    public StartCrawlUseCaseImpl(CrawlRepository crawlRepository, Crawler crawler) {
        this.crawlRepository = crawlRepository;
        this.crawler = crawler;
    }

    @Override
    public StartCrawlDTO execute(CrawlKeywordDTO keywordDTO) {
        Keyword keyword = new Keyword(keywordDTO.getKeyword());
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        crawlRepository.saveCrawl(crawl);

        executorService.submit(() -> {
            try {
                crawler.crawlWebPage(keyword, crawl);
            } catch (Exception e) {
                LOG.error("Error while crawling: {}", e.getMessage());
            }
        });

        return new StartCrawlDTO(crawl.getId());
    }
}