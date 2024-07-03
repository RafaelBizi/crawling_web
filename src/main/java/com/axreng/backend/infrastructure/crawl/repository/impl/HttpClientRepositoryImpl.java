package com.axreng.backend.infrastructure.crawl.repository.impl;

import com.axreng.backend.domain.crawl.repository.HttpClientRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientRepositoryImpl implements HttpClientRepository {
    private final HttpClient httpClient;

    public HttpClientRepositoryImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse<String> send(HttpRequest request) throws Exception {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder().uri(uri).build();
    }
}