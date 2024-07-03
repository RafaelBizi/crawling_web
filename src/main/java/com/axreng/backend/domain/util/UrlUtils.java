package com.axreng.backend.domain.util;

import com.axreng.backend.application.crawl.usecase.impl.StartCrawlUseCaseImpl;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UrlUtils.class);


    public static boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            LOG.warn("Invalid URL: {}", url, e);
            return false;
        }
    }

}
