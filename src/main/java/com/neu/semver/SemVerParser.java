package com.neu.semver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for semantic version strings compliant with SemVer 2.0.0.
 * with regex
* @author Yinlu Gong
 * @version 0.1.0
 */
public final class SemVerParser {

    /**
     * Regular expression pattern for SemVer 2.0.0 strings.
     * Matches MAJOR.MINOR.PATCH[-PRERELEASE][+BUILD].
     */
    private static final Pattern SEMVER_PATTERN = Pattern.compile(
        "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)"
        + "(?:-((?:0|[1-9]\\d*|[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z])"
        + "(?:\\.(?:0|[1-9]\\d*|[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z]))*))?"
        + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$"
    );

    // Group indices for SEMVER_PATTERN matcher
    /** Group index for the major version. */
    private static final int GROUP_MAJOR = 1;
    /** Group index for the minor version. */
    private static final int GROUP_MINOR = 2;
    /** Group index for the patch version. */
    private static final int GROUP_PATCH = 3;
    /** Group index for the prerelease identifier. */
    private static final int GROUP_PRERELEASE = 4;
    /** Group index for the build metadata. */
    private static final int GROUP_BUILD = 5;
    /**
     * Private constructor to prevent instantiation.
     */
    private SemVerParser() {
        // Utility class (cannot be instantiated)
    }
    /**
     * Parses a version string into a {@link SemVer} instance.
     *
     * @param version the version string
     * @return a instance representing the parsed version
     * @throws IllegalArgumentException if {@code version} is null
     * @throws InvalidSemVerException if the version is empty or invalid
     */
    public static SemVer parse(final String version) {
        if (version == null) {
            throw new IllegalArgumentException(
                "Version string cannot be null"
                );
        }
        final String trimmed = version.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidSemVerException(
                "Version string cannot be empty"
                );
        }
        final Matcher matcher = SEMVER_PATTERN.matcher(trimmed);
        if (!matcher.matches()) {
            throw new InvalidSemVerException(
                "Invalid semantic version: "
                + trimmed);
        }
        // Extract all group values to avoid Law of Demeter violations
        final String majorStr = matcher.group(GROUP_MAJOR);
        final String minorStr = matcher.group(GROUP_MINOR);
        final String patchStr = matcher.group(GROUP_PATCH);
        final String prereleaseStr = matcher.group(GROUP_PRERELEASE);
        final String buildStr = matcher.group(GROUP_BUILD);
        return new SemVer(
            Integer.parseInt(majorStr),
            Integer.parseInt(minorStr),
            Integer.parseInt(patchStr),
            prereleaseStr,
            buildStr,
            trimmed
        );
    }
}
