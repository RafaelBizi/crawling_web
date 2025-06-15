package com.axreng.backend.presentation.crawl.controller;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.request.CrawlRequestDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.exception.CrawlNotExistsException;
import com.axreng.backend.domain.crawl.usecase.GetCrawlUseCase;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;
import com.axreng.backend.domain.util.GsonSingleton;
import com.axreng.backend.application.response.ErrorResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URL;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlController {
    private StartCrawlUseCase startCrawlUseCase;
    private GetCrawlUseCase getCrawlUseCase;
    private final Gson gson = GsonSingleton.getInstance();

    public CrawlController(
            StartCrawlUseCase startCrawlUseCase,
            GetCrawlUseCase getCrawlUseCase) {
        this.startCrawlUseCase = startCrawlUseCase;
        this.getCrawlUseCase = getCrawlUseCase;
    }

    public void setupEndpoints() {
        get("/", this::handleHome);
        post("/crawl", this::handlePostCrawl);
        get("/crawl/:id", this::handleGetCrawl);
    }

    private String handleHome(Request req, Response res) {
        res.type("text/html");
        return renderHtml();
    }

    public String handlePostCrawl(Request req, Response res) {
        try {
            res.type("application/json");
            CrawlRequestDTO crawlRequestDTO = gson.fromJson(req.body(), CrawlRequestDTO.class);
            
            // Validação da URL base
            if (crawlRequestDTO.getBaseUrl() == null || crawlRequestDTO.getBaseUrl().trim().isEmpty()) {
                throw new IllegalArgumentException("A URL base é obrigatória");
            }
            
            try {
                new URL(crawlRequestDTO.getBaseUrl());
            } catch (Exception e) {
                throw new IllegalArgumentException("URL base inválida");
            }
            
            // Validação da keyword
            if (crawlRequestDTO.getKeyword() == null || 
                crawlRequestDTO.getKeyword().length() < 4 || 
                crawlRequestDTO.getKeyword().length() > 32) {
                throw new IllegalArgumentException("A palavra-chave deve ter entre 4 e 32 caracteres");
            }

            StartCrawlDTO startCrawlDTO = startCrawlUseCase.execute(crawlRequestDTO);
            res.status(200);
            return gson.toJson(startCrawlDTO);
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, e.getMessage()));
        }
    }

    public String handleGetCrawl(Request req, Response res) {
        try {
            res.type("application/json");
            String id = req.params(":id");
            CrawlIdDTO crawlIdDTO = new CrawlIdDTO(id);
            GetCrawlDTO getCrawlDTO = getCrawlUseCase.execute(crawlIdDTO);
            return gson.toJson(getCrawlDTO);
        } catch (CrawlNotExistsException e) {
            res.status(404);
            return gson.toJson(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, e.getMessage()));
        }
    }

    private String renderHtml() {
        try {
            return new String(getClass().getResourceAsStream("/static/index.html").readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error loading HTML file";
        }
    }
}