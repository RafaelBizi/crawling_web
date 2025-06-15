package com.axreng.backend.com.axreng.backend.presentation.controller;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.request.CrawlRequestDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.application.response.ErrorResponse;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.exception.CrawlNotExistsException;
import com.axreng.backend.domain.crawl.usecase.GetCrawlUseCase;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;
import com.axreng.backend.domain.util.GsonSingleton;
import com.axreng.backend.presentation.crawl.controller.CrawlController;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;
import spark.Response;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrawlControllerTest {

    @Mock
    private StartCrawlUseCase startCrawlUseCase;

    @Mock
    private GetCrawlUseCase getCrawlUseCase;

    @Mock
    private Request req;

    @Mock
    private Response res;

    @InjectMocks
    private CrawlController crawlController;

    private Gson gson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = GsonSingleton.getInstance();
        crawlController = new CrawlController(startCrawlUseCase, getCrawlUseCase);
    }

    // ... outros códigos permanecem inalterados ...

    @Test
    void testHandlePostCrawlSuccess() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO("http://example.com", "keyword");
        StartCrawlDTO startCrawlDTO = new StartCrawlDTO("crawlId");

        when(req.body()).thenReturn(gson.toJson(requestDTO));
        when(startCrawlUseCase.execute(any(CrawlRequestDTO.class))).thenReturn(startCrawlDTO);

        // When
        String response = crawlController.handlePostCrawl(req, res);

        // Then
        verify(res).status(200);
        verify(res).type("application/json");
        assertThat(response, is(gson.toJson(startCrawlDTO)));
    }

    @Disabled
    @Test
    void testHandlePostCrawlIllegalArgumentException() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO("http://example.com", "k");

        when(req.body()).thenReturn(gson.toJson(requestDTO));
        when(startCrawlUseCase.execute(any(CrawlRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid keyword"));

        // When
        String response = crawlController.handlePostCrawl(req, res);

        // Then
        verify(res).status(400);
        verify(res).type("application/json");
        assertThat(response, is(gson.toJson(new ErrorResponse(400, "Invalid keyword"))));
    }

    @Test
    void testHandlePostCrawlException() {
        // Given
        CrawlRequestDTO requestDTO = new CrawlRequestDTO("http://example.com", "keyword");

        when(req.body()).thenReturn(gson.toJson(requestDTO));
        when(startCrawlUseCase.execute(any(CrawlRequestDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        String response = crawlController.handlePostCrawl(req, res);

        // Then
        assertThat(response, is(gson.toJson(new ErrorResponse(500, "Unexpected error"))));
    }

    @Test
    void testHandleGetCrawlSuccess() {
        String id = "crawlId";
        Crawl crawl = new Crawl(new CopyOnWriteArrayList<>());
        GetCrawlDTO getCrawlDTO = new GetCrawlDTO(crawl);

        when(req.params(":id")).thenReturn(id);
        when(getCrawlUseCase.execute(any(CrawlIdDTO.class))).thenReturn(getCrawlDTO);

        String response = crawlController.handleGetCrawl(req, res);

        verify(res).type("application/json");
        assertThat(response, is(gson.toJson(getCrawlDTO)));
    }

    @Test
    void testHandleGetCrawlCrawlNotExistsException() {
        String id = "crawlId";

        when(req.params(":id")).thenReturn(id);
        when(getCrawlUseCase.execute(any(CrawlIdDTO.class))).thenThrow(new CrawlNotExistsException("Crawl not found"));

        String response = crawlController.handleGetCrawl(req, res);

        verify(res).status(404);
        verify(res).type("application/json");
        assertThat(response, is(gson.toJson(new ErrorResponse(404, "Crawl not found"))));
    }

    @Test
    void testHandleGetCrawlException() {
        String id = "crawlId";

        when(req.params(":id")).thenReturn(id);
        when(getCrawlUseCase.execute(any(CrawlIdDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        String response = crawlController.handleGetCrawl(req, res);
        
        assertThat(response, is(gson.toJson(new ErrorResponse(500, "Unexpected error"))));
    }

    // ... restante dos testes permanecem inalterados ...
}