package com.axreng.backend.domain.crawl.usecase;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;

public interface GetCrawlUseCase {
    GetCrawlDTO execute(CrawlIdDTO crawlIdDTO);
}
