package com.axreng.backend.domain.crawl.usecase;

import com.axreng.backend.application.crawl.dto.request.CrawlKeywordDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;

public interface StartCrawlUseCase {

    StartCrawlDTO execute(CrawlKeywordDTO crawlKeywordDTO);

}
