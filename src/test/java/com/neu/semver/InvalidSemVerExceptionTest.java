package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for the InvalidSemVerException class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("InvalidSemVerException Tests")
class InvalidSemVerExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String message = "Invalid version format";
        InvalidSemVerException exception = new InvalidSemVerException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Invalid version format";
        Throwable cause = new RuntimeException("Root cause");
        InvalidSemVerException exception = new InvalidSemVerException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should be instance of IllegalArgumentException")
    void shouldBeInstanceOfIllegalArgumentException() {
        InvalidSemVerException exception = new InvalidSemVerException("test");
        assertTrue(exception instanceof IllegalArgumentException);
    }
}
