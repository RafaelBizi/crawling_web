package com.axreng.backend;

import com.axreng.backend.application.crawl.usecase.impl.GetCrawlUseCaseImpl;
import com.axreng.backend.application.crawl.usecase.impl.StartCrawlUseCaseImpl;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.repository.HttpClientRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import com.axreng.backend.domain.crawl.service.impl.CrawlerImpl;
import com.axreng.backend.domain.crawl.usecase.GetCrawlUseCase;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;
import com.axreng.backend.infrastructure.crawl.repository.impl.CrawlRepositoryImpl;
import com.axreng.backend.infrastructure.crawl.repository.impl.HttpClientRepositoryImpl;
import com.axreng.backend.presentation.crawl.controller.CrawlController;

import java.net.http.HttpClient;
import java.time.Duration;

import static spark.Spark.port;

public class Main {

    public static final int PORT = 4567;

    public static void main(String[] args) {

        port(PORT);
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpClientRepository httpClientRepository = new HttpClientRepositoryImpl(httpClient);
        CrawlRepository crawlRepository = new CrawlRepositoryImpl();

        Crawler crawler = new CrawlerImpl(httpClientRepository);

        StartCrawlUseCase startCrawlUseCase = new StartCrawlUseCaseImpl(crawlRepository, crawler);
        GetCrawlUseCase getCrawlUseCase = new GetCrawlUseCaseImpl(crawlRepository);

        CrawlController crawlController = new CrawlController(startCrawlUseCase, getCrawlUseCase);
        crawlController.setupEndpoints();
    }
}
