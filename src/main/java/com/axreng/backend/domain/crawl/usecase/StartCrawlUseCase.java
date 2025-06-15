package com.axreng.backend.domain.crawl.usecase;

import com.axreng.backend.application.crawl.dto.request.CrawlRequestDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;

public interface StartCrawlUseCase {
    StartCrawlDTO execute(CrawlRequestDTO crawlRequestDTO);
}