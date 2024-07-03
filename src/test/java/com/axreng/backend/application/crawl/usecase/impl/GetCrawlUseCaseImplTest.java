package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.exception.CrawlNotExistsException;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetCrawlUseCaseImplTest {

    @Mock
    private CrawlRepository crawlRepository;

    @InjectMocks
    private GetCrawlUseCaseImpl getCrawlUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnCrawlWhenExists() {
        // Given
        CrawlIdDTO crawlIdDTO = new CrawlIdDTO("1jodfj2");
        Crawl crawl = new Crawl(List.of());
        when(crawlRepository.getCrawl("1jodfj2")).thenReturn(crawl);

        // When
        GetCrawlDTO result = getCrawlUseCase.execute(crawlIdDTO);

        // Then
        assertEquals(crawl.getId(), result.getId());
        assertEquals(crawl.getStatus().name().toLowerCase(), result.getStatus());
        assertEquals(crawl.getUrls(), result.getUrls());
    }

    @Test
    void shouldThrowExceptionWhenCrawlDoesNotExist() {
        // Given
        CrawlIdDTO crawlIdDTO = new CrawlIdDTO("1jodfj2");

        // When
        when(crawlRepository.getCrawl("1jodfj2")).thenReturn(null);

        // Then
        assertThrows(CrawlNotExistsException.class, () -> getCrawlUseCase.execute(crawlIdDTO));
    }
}