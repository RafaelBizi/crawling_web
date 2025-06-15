package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlRequestDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class StartCrawlUseCaseImplTest {

    @Mock
    private CrawlRepository crawlRepository;

    @Mock
    private Crawler crawler;

    @InjectMocks
    private StartCrawlUseCaseImpl startCrawlUseCase;

    private static final String VALID_URL = "http://example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(crawlRepository, crawler);
    }

    @Test
    void shouldStartCrawlWithValidRequest() throws InterruptedException {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO(VALID_URL, "keyword");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When
        StartCrawlDTO result = startCrawlUseCase.execute(requestDTO);

        // Then
        assertNotNull(result.getId());
        Thread.sleep(1000);
        verify(crawlRepository, times(1)).saveCrawl(any(Crawl.class));
        verify(crawler, times(1)).crawlWebPage(eq(VALID_URL), any(Keyword.class), any(Crawl.class));
    }

    @Test
    void shouldNotStartCrawlWithEmptyKeyword() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO(VALID_URL, "");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> startCrawlUseCase.execute(requestDTO));
        verify(crawlRepository, never()).saveCrawl(any(Crawl.class));
        verify(crawler, never()).crawlWebPage(anyString(), any(Keyword.class), any(Crawl.class));
    }

    @Disabled
    @Test
    void shouldNotStartCrawlWithEmptyBaseUrl() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO("", "keyword");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> startCrawlUseCase.execute(requestDTO));
        verify(crawlRepository, never()).saveCrawl(any(Crawl.class));
        verify(crawler, never()).crawlWebPage(anyString(), any(Keyword.class), any(Crawl.class));
    }

    @Disabled
    @Test
    void shouldNotStartCrawlWithInvalidUrl() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO("invalid-url", "keyword");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> startCrawlUseCase.execute(requestDTO));
        verify(crawlRepository, never()).saveCrawl(any(Crawl.class));
        verify(crawler, never()).crawlWebPage(anyString(), any(Keyword.class), any(Crawl.class));
    }
}