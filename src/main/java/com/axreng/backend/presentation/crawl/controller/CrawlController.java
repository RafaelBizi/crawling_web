package com.axreng.backend.presentation.crawl.controller;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.request.CrawlKeywordDTO;
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
        post("/crawl", this::handlePostCrawl);
        get("/crawl/:id", this::handleGetCrawl);
    }

    public String handlePostCrawl(Request req, Response res) {
        try {
            res.type("application/json");
            CrawlKeywordDTO crawlKeywordDTO = gson.fromJson(req.body(), CrawlKeywordDTO.class);
            StartCrawlDTO startCrawlDTO = startCrawlUseCase.execute(crawlKeywordDTO);
            res.status(200);
            return gson.toJson(startCrawlDTO);
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
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
            return gson.toJson(new ErrorResponse(500, e.getMessage()));
        }
    }
}