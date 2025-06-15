package com.axreng.backend.domain.crawl.service.impl;

import com.axreng.backend.application.util.PropertyReader;
import com.axreng.backend.domain.crawl.entity.Crawl;
import com.axreng.backend.domain.crawl.entity.CrawlStatus;
import com.axreng.backend.domain.crawl.entity.Keyword;
import com.axreng.backend.domain.crawl.repository.HttpClientRepository;
import com.axreng.backend.domain.crawl.service.Crawler;
import com.axreng.backend.domain.util.RetryStrategy;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;

import static com.axreng.backend.domain.crawl.util.Constants.*;
import static com.axreng.backend.domain.util.UrlUtils.isValidUrl;
import static java.lang.System.getenv;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CrawlerImpl implements Crawler {
    private static final String BASE_URL
            = getenv("BASE_URL") != null ? getenv("BASE_URL") : PropertyReader.getProperty("base.url");
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(CrawlerImpl.class);
    public static final Integer THREAD_POOL_SIZE
            = Integer.parseInt(PropertyReader.getProperty("crawler.threadPoolSize"));
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    private final ThreadPoolExecutor executorService
            = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, MILLISECONDS, workQueue);

    private final HttpClientRepository httpClientRepository;

    private final Semaphore semaphore = new Semaphore(THREAD_POOL_SIZE);

    private final RetryStrategy retryStrategy = new RetryStrategy(3, 100);

    public CrawlerImpl(HttpClientRepository httpClientRepository) {
        this.httpClientRepository = httpClientRepository;
    }

    @Override
    public void crawlWebPage(String baseUrl, Keyword keyword, Crawl crawl) {
        LOG.info("Crawling started for keyword: {}, baseUrl: {}, id: {}",
                keyword.getValue(), baseUrl, crawl.getId());

        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Set<String> visited = Collections.synchronizedSet(new LinkedHashSet<>());
        Set<String> addedToQueue = Collections.synchronizedSet(new LinkedHashSet<>());

        queue.add(baseUrl);
        addedToQueue.add(baseUrl);

        List<Future<?>> futures = new ArrayList<>();

        while (!queue.isEmpty() || futures.stream().anyMatch(f -> !f.isDone())) {
            String currentUrl = queue.poll();
            if (currentUrl != null) {
                try {
                    semaphore.acquire();
                    futures.add(executorService.submit(() -> {
                        try {
                            retryStrategy.execute(() -> {
                                // Movido para depois da verificação do conteúdo
                                if (visited.contains(currentUrl)) {
                                    return null;
                                }

                                HttpRequest request = HttpRequest.newBuilder()
                                        .uri(new URI(currentUrl))
                                        .build();
                                HttpResponse<String> response = httpClientRepository.send(request);

                                String responseBodyLowerCase = response.body().toLowerCase();

                                // Adiciona à lista de visitados após obter o conteúdo
                                visited.add(currentUrl);

                                if (responseBodyLowerCase.contains(keyword.getValue().toLowerCase())) {
                                    crawl.getUrls().add(currentUrl);
                                }

                                Matcher matcher = HREF_PATTERN.matcher(response.body());
                                while (matcher.find()) {
                                    String href = matcher.group(1);
                                    href = href.replaceAll(HTML_TAG_REGEX, "");

                                    if (isValidHref(href)) {
                                        href = baseUrl + href;

                                        if (isValidUrl(href) && !visited.contains(href) && addedToQueue.add(href)) {
                                            queue.add(href);
                                        }
                                    }
                                }
                                return null;
                            });
                        } catch (Exception e) {
                            LOG.error("Error while crawling: {}", e.getMessage());
                        } finally {
                            semaphore.release();
                        }
                    }));
                } catch (InterruptedException e) {
                    LOG.error("Error while acquiring semaphore: {}", e.getMessage());
                }
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Error while waiting for a crawling task to complete: {}", e.getMessage());
            }
        }

        crawl.setStatus(CrawlStatus.DONE);
    }

    private boolean isValidHref(String href) {
        return href != null
                && !href.startsWith(HTTP)
                && !href.startsWith(MAILTO_PREFIX)
                && !href.startsWith(JAVASCRIPT_PREFIX)
                && href.matches(VALID_CHARS_URL_REGEX)
                && href.endsWith(HTML_EXTENSION);
    }

}