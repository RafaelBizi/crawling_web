package com.axreng.backend.com.axreng.backend.presentation.helper;

import com.axreng.backend.presentation.helper.TextProcessingHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextProcessingHelperTest {

    @Test
    void shouldRemoveHtmlTagsAndMetadata() throws IOException {
        // Given
        List<String> lines
                = Files.readAllLines(Paths.get("src/test/resources/com.axreng.backend.infrastructure.stubb/" +
                "TagsAndMetadataToBeRemoved.html"));
        String input = lines.stream().reduce("", String::concat);
        String expected = "Some text";

        // When
        String actual = TextProcessingHelper.removeHtmlTagsAndMetadata(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyStringWhenInputIsEmpty() {
        // Given
        String input = "";

        // When
        String actual = TextProcessingHelper.removeHtmlTagsAndMetadata(input);

        // Then
        assertEquals(input, actual);
    }

    @Test
    void shouldReturnSameStringWhenNoHtmlTagsOrMetadata() {
        // Given
        String input = "Some text";

        // When
        String actual = TextProcessingHelper.removeHtmlTagsAndMetadata(input);

        // Then
        assertEquals(input, actual);
    }
}