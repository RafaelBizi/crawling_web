package com.axreng.backend.domain.crawl.util;

import com.axreng.backend.domain.util.RetryStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryStrategyTest {
    private RetryStrategy retryStrategy;

    @BeforeEach
    public void setUp() {
        retryStrategy = new RetryStrategy(1, 500);
    }

    @Test
    public void executeShouldReturnResultWhenCallableSucceeds() throws Exception {
        // Given
        Callable<String> callable = () -> "Success";

        // When
        String result = retryStrategy.execute(callable);

        // Then
        assertEquals("Success", result);
    }

    @Test
    public void executeShouldRetryWhenCallableFails() {
        // Given
        Callable<String> callable = () -> {
            throw new Exception("Failure");
        };

        // When / Then
        assertThrows(Exception.class, () -> retryStrategy.execute(callable));
    }

}