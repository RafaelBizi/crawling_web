package com.axreng.backend.application.crawl.usecase.impl;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.exception.CrawlNotExistsException;
import com.axreng.backend.domain.crawl.repository.CrawlRepository;
import com.axreng.backend.domain.crawl.usecase.GetCrawlUseCase;
import org.slf4j.Logger;

public class GetCrawlUseCaseImpl implements GetCrawlUseCase {

    private final CrawlRepository crawlRepository;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(GetCrawlUseCaseImpl.class);

    public GetCrawlUseCaseImpl(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }

    @Override
    public GetCrawlDTO execute(CrawlIdDTO crawlIdDTO) {
        String id = crawlIdDTO.getId();
        Crawl crawl = crawlRepository.getCrawl(id);
        if (crawl == null) {
            LOG.error("Crawl with id {} does not exist.", id);
            throw new CrawlNotExistsException("Crawl with id " + id + " does not exist.");
        }
        return new GetCrawlDTO(crawl);
    }

}
