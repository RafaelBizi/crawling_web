package com.axreng.backend.domain.crawl.repository;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpClientRepository {
    HttpResponse<String> send(HttpRequest request) throws Exception;
    HttpRequest buildRequest(URI uri);
}