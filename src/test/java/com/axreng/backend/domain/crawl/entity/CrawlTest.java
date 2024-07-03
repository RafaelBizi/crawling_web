package com.axreng.backend.domain.crawl.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrawlTest {

    @Test
    void testCrawlInitialization() {
        // Given
        List<String> urls = Arrays.asList();

        // When
        Crawl crawl = new Crawl(urls);

        // Then
        assertNotNull(crawl.getId());
        assertEquals(CrawlStatus.ACTIVE, crawl.getStatus());
        assertEquals(urls, crawl.getUrls());
    }

    @Test
    void testGenerateId() {
        // Given
        List<String> urls = Arrays.asList("http://example.com");

        // When
        Crawl crawl = new Crawl(urls);
        String id = crawl.getId();

        // Then
        assertNotNull(id);
        assertEquals(8, id.length());
        assertTrue(id.chars().allMatch(Character::isLetterOrDigit));
    }

    @Test
    void testSetStatus() {
        // Given
        List<String> urls = Arrays.asList("http://example.com");

        // When
        Crawl crawl = new Crawl(urls);
        crawl.setStatus(CrawlStatus.DONE);

        // Then
        assertEquals(CrawlStatus.DONE, crawl.getStatus());
    }
}
