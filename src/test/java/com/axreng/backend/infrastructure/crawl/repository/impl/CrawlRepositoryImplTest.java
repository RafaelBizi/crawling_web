package com.axreng.backend.infrastructure.crawl.repository.impl;

import com.axreng.backend.domain.crawl.entity.Crawl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CrawlRepositoryImplTest {

    private CrawlRepositoryImpl crawlRepository;

    @BeforeEach
    void setUp() {
        crawlRepository = new CrawlRepositoryImpl();
    }

    @Test
    void shouldReturnCrawlWhenExists() {
        // Given
        Crawl crawl = new Crawl(List.of());
        crawlRepository.saveCrawl(crawl);

        // When
        Crawl result = crawlRepository.getCrawl(crawl.getId());

        // Then
        assertEquals(crawl, result);
    }

    @Test
    void shouldReturnNullWhenCrawlDoesNotExist() {
        // When
        Crawl result = crawlRepository.getCrawl("dfu64f8d");

        // Then
        assertNull(result);
    }
}