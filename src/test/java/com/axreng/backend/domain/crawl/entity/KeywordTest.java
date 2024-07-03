package com.axreng.backend.domain.crawl.entity;

import com.axreng.backend.domain.crawl.exception.InvalidKeywordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordTest {

    @Test
    void testValidKeyword() {
        // Given and When
        Keyword keyword = new Keyword("validKeyword");

        // Then
        assertEquals("validKeyword", keyword.getValue(), "Keyword value should match the input value");
    }

    @Test
    void testKeywordTooShort() {
        // Given and When
        Exception exception = assertThrows(InvalidKeywordException.class, () -> new Keyword("abc"));
        String expectedMessage = "Keyword length must be between 4 and 32 characters";
        String actualMessage = exception.getMessage();

        // Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testKeywordTooLong() {
        // Given and When
        Exception exception = assertThrows(InvalidKeywordException.class, () -> new Keyword("a".repeat(33)));
        String expectedMessage = "Keyword length must be between 4 and 32 characters";
        String actualMessage = exception.getMessage();

        // Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testKeywordNull() {
        // Given and When
        Exception exception = assertThrows(InvalidKeywordException.class, () -> new Keyword(null));
        String expectedMessage = "Keyword length must be between 4 and 32 characters";
        String actualMessage = exception.getMessage();

        // Then
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
