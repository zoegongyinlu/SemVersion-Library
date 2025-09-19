package com.neu.semver;

/**
 * Exception thrown when attempting to parse an invalid semantic version string.
 *
 * This exception is thrown by the SemVer constructor when the provided version
 * string does not conform to the SemVer 2.0.0 specification.
 *
 * @author Zoe Gong
 * @version 0.1.0
 * @since 0.1.0
 */
public class InvalidSemVerException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new InvalidSemVerException with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public InvalidSemVerException(final String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidSemVerException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public InvalidSemVerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
