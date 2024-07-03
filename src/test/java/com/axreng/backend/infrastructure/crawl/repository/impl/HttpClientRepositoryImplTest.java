package com.axreng.backend.infrastructure.crawl.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpClientRepositoryImplTest {

    @Mock
    private HttpClient httpClient;

    private HttpClientRepositoryImpl httpClientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        httpClientRepository = new HttpClientRepositoryImpl(httpClient);
    }

    @Test
    void shouldSendHttpRequest() throws Exception {
        // Given
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://example.com")).build();
        HttpResponse<String> expectedResponse = mock(HttpResponse.class);

        // When
        when(expectedResponse.statusCode()).thenReturn(200);
        when(expectedResponse.body()).thenReturn("OK");
        when(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(expectedResponse);
        HttpResponse<String> actualResponse = httpClientRepository.send(request);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldBuildHttpRequest() {
        // Given
        URI uri = URI.create("http://example.com");

        // When
        HttpRequest actualRequest = httpClientRepository.buildRequest(uri);

        // Then
        assertEquals(uri, actualRequest.uri());
    }
}