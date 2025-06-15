package com.axreng.backend.domain.crawl.service.impl;

import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.HttpClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

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

    @Disabled
    @Test
    void testCrawlWebPageWithKeywordManual() throws Exception {
        // Given
        String baseUrl = "http://hiring.axreng.com/";
        Keyword keyword = new Keyword("manual");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        
        // Setup HTML content com o conteúdo esperado
        String homeHtml = "<html><body>Manual content<a href=\"index.html\">Link</a></body></html>";
        String indexHtml = "<html><body>Manual page<a href=\"index1.html\">Link</a></body></html>";
        String index1Html = "<html><body>More manual content</body></html>";

        // Configure o mock para retornar os conteúdos em sequência
        when(httpResponse.body())
                .thenReturn(homeHtml)
                .thenReturn(indexHtml)
                .thenReturn(index1Html);

        // Configure o httpClientRepository para sempre retornar a mesma resposta mockada
        when(httpClientRepository.send(any(HttpRequest.class)))
                .thenReturn(httpResponse);

        // When
        crawlerImpl.crawlWebPage(baseUrl, keyword, crawl);

        // Then
        // Aguardar processamento das threads
        Thread.sleep(1000);
        
        assertThat(crawl.getUrls(), hasItems(
                baseUrl,
                baseUrl + "index.html",
                baseUrl + "index1.html"
        ));
    }

    @Test
    void testCrawlWebPageWithoutKeywordManual() throws Exception {
        // Given
        String baseUrl = "http://hiring.axreng.com/";
        Keyword keyword = new Keyword("manual");
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        HttpRequest requestHome = HttpRequest.newBuilder().uri(new URI(baseUrl)).build();
        String testHtml = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/com.axreng.backend.infrastructure.stubb/NotExistsManualKeyword.html")));

        when(httpClientRepository.send(requestHome)).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(testHtml);

        // When
        crawlerImpl.crawlWebPage(baseUrl, keyword, crawl);

        // Then
        Assertions.assertTrue(crawl.getUrls().isEmpty());
    }

}