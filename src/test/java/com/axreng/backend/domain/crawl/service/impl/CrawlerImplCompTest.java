package com.axreng.backend.domain.crawl.service.impl;

import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.HttpClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;

public class CrawlerImplCompTest {

    @Mock
    private HttpClientRepository httpClientRepository;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private CrawlerImpl crawlerImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCrawlWebPageWithKeywordManual() throws Exception {
        // Given
        Keyword keyword = new Keyword("manual");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        HttpRequest requestHome
                = HttpRequest.newBuilder().uri(new URI("http://hiring.axreng.com/")).build();
        HttpRequest requestManpageIndex
                = HttpRequest.newBuilder().uri(new URI("http://hiring.axreng.com/manpageindex.html")).build();
        HttpRequest requestIndex
                = HttpRequest.newBuilder().uri(new URI("http://hiring.axreng.com/index.html")).build();
        HttpRequest requestIndex1
                = HttpRequest.newBuilder().uri(new URI("http://hiring.axreng.com/index1.html")).build();
        String homeHtml
                = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/com.axreng.backend.infrastructure.stubb/HomeHiringAxreng.html")));
        String manpageIndexHtml
                = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/com.axreng.backend.infrastructure.stubb/ManualPageHiringAxreng.html")));
        String requestIndexHtml
                = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/com.axreng.backend.infrastructure.stubb/IndexPageWithManualKeyword.html")));

        when(httpClientRepository.send(requestHome)).thenReturn(httpResponse);
        when(httpClientRepository.send(requestManpageIndex)).thenReturn(httpResponse);
        when(httpClientRepository.send(requestIndex)).thenReturn(httpResponse);
        when(httpClientRepository.send(requestIndex1)).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(homeHtml, manpageIndexHtml, requestIndexHtml);

        // When
        crawlerImpl.crawlWebPage(keyword, crawl);

        // Then
        assertThat(
                crawl.getUrls(),
                hasItems("http://hiring.axreng.com/",
                        "http://hiring.axreng.com/index.html",
                        "http://hiring.axreng.com/index1.html"));
    }

    @Test
    void testCrawlWebPageWithoutKeywordManual() throws Exception {
        // Given
        Keyword keyword = new Keyword("manual");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        HttpRequest requestHome
                = HttpRequest.newBuilder().uri(new URI("http://hiring.axreng.com/")).build();
        String testHtml
                = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/com.axreng.backend.infrastructure.stubb/NotExistsManualKeyword.html")));

        when(httpClientRepository.send(requestHome)).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(testHtml);

        // When
        crawlerImpl.crawlWebPage(keyword, crawl);

        // Then
        Assertions.assertTrue(crawl.getUrls().isEmpty());
    }

}
