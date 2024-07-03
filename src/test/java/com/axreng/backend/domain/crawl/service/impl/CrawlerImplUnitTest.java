package com.axreng.backend.domain.crawl.service.impl;

import com.axreng.backend.application.util.PropertyReader;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.CrawlStatus;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.HttpClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrawlerImplUnitTest {

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
    void testCrawlWebPageWithValidKeyword() throws Exception {
        // Given
        Keyword keyword = new Keyword("keyword");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        HttpRequest request
                = HttpRequest.newBuilder().uri(new URI(PropertyReader.getProperty("base.url"))).build();
        when(httpClientRepository.send(any(HttpRequest.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("<html><body>Sample body with keyword</body></html>");

        // When
        crawlerImpl.crawlWebPage(keyword, crawl);

        // Then
        verify(httpClientRepository, times(1)).send(request);
        assertThat(crawl.getUrls(), hasItem(PropertyReader.getProperty("base.url")));
        assertEquals(CrawlStatus.DONE, crawl.getStatus(), "Crawl status should be DONE");
    }

    @Test
    void testStartWithNoKeyword() throws Exception {
        String body = "<html><body>Sample body without the keyword</body></html>";
        String url = "http://example.com";

        when(httpClientRepository.send(any(HttpRequest.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(body);

        Keyword keyword = new Keyword("keyword");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());

        crawlerImpl.crawlWebPage(keyword, crawl);

        assertThat(crawl.getUrls(), not(hasItem(url)));
        assertEquals(CrawlStatus.DONE, crawl.getStatus(), "Crawl status should be DONE");
    }

    @Test
    void testStartWithError() throws Exception {
        String url = "http://example.com";

        when(httpClientRepository.send(any(HttpRequest.class))).thenThrow(new RuntimeException("Test exception"));

        Keyword keyword = new Keyword("keyword");
        Crawl crawl = new Crawl(List.of());

        crawlerImpl.crawlWebPage(keyword, crawl);

        assertThat(crawl.getUrls(), not(hasItem(url)));
        assertEquals(CrawlStatus.DONE, crawl.getStatus(), "Crawl status should be DONE");
    }

}