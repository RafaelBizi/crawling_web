package com.axreng.backend.domain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RetryStrategy {
    private final int maxAttempts;
    private final long sleepTime;
    private static final Logger LOG = LoggerFactory.getLogger(RetryStrategy.class);

    public RetryStrategy(int maxAttempts, long sleepTime) {
        this.maxAttempts = maxAttempts;
        this.sleepTime = sleepTime;
    }

    public <T> T execute(Callable<T> callable) throws Exception {
        int attempts = 0;
        while (true) {
            try {
                return callable.call();
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxAttempts) {
                    throw e;
                }
                LOG.info("Retrying after failure, attempt number: {}", attempts);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry was interrupted", ie);
                }
            }
        }
    }
}