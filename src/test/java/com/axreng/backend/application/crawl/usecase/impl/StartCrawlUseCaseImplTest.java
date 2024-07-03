package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlKeywordDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(crawlRepository, crawler);
    }

    @Test
    void shouldStartCrawlWithValidKeyword() throws InterruptedException {
        // Given
        CrawlKeywordDTO keywordDTO = new CrawlKeywordDTO("keyword");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When
        StartCrawlDTO result = startCrawlUseCase.execute(keywordDTO);

        // Then
        assertNotNull(result.getId());
        Thread.sleep(1000);
        verify(crawlRepository, times(1)).saveCrawl(any(Crawl.class));
        verify(crawler, times(1)).crawlWebPage(any(Keyword.class), any(Crawl.class));
    }

    @Test
    void shouldNotStartCrawlWithEmptyKeyword() {
        // Given
        CrawlKeywordDTO keywordDTO = new CrawlKeywordDTO("");
        doNothing().when(crawlRepository).saveCrawl(any(Crawl.class));

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> startCrawlUseCase.execute(keywordDTO));

        // Then
        verify(crawlRepository, times(0)).saveCrawl(any(Crawl.class));
        verify(crawler, times(0)).crawlWebPage(any(Keyword.class), any(Crawl.class));
    }
}