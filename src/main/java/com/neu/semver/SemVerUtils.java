package com.neu.semver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Utility class for SemVer operations.
 * Contains static utility methods for version manipulation and sorting.
 *
 * @author Yinlu Gong
 * @version 0.1.0
 */
public final class SemVerUtils {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SemVerUtils() {
    }

    /**
     * Sorts a list of version strings in descending order (newest to oldest).
     *
     * @param versions the list of version strings to sort
     * @return a new list containing the versions sorted in descending order
     * @throws InvalidSemVerException if any version string is invalid
     * @throws IllegalArgumentException if versions is null or contains null
     *         elements
     */
    public static List<String> sortVersions(final List<String> versions) {
        if (versions == null) {
            throw new IllegalArgumentException(
                "Versions list cannot be null"
            );
        }

        // Validate all versions first and collect SemVer objects
        final List<SemVer> semVerObjects = new ArrayList<>();
        for (final String version : versions) {
            if (version == null) {
                throw new IllegalArgumentException(
                    "Version string cannot be null"
                );
            }
            semVerObjects.add(SemVerParser.parse(version));
        }
        semVerObjects.sort(Collections.reverseOrder());
        final List<String> result = new ArrayList<>();
        for (final SemVer semVer : semVerObjects) {
            result.add(semVer.getOriginalVersion());
        }
        return result;

    }
    /**
     * Finds the highest version from a list of version strings.
     *
     * @param versions the list of version strings to compare
     * @return the highest version string
     * @throws InvalidSemVerException if any version string is invalid
     * @throws IllegalArgumentException if versions is null,
     * empty, or contains null elements
     */
    public static String findHighestVersion(final List<String> versions) {
        if (versions == null || versions.isEmpty()) {
            throw new IllegalArgumentException(
                "Versions list cannot be null or empty"
            );
        }
        final List<String> sortedVersions = sortVersions(versions);
        // First element is the highest due to descending sort
        final String highestVersion = sortedVersions.get(0);
        return highestVersion;
    }
    /**
     * Checks if a version string is valid according
     * to SemVer 2.0.0 specification.
     *
     * @param version the version string to validate
     * @return true if the version is valid, false otherwise
     */
    public static boolean isValidVersion(final String version) {
        boolean isValid;
        try {
            SemVerParser.parse(version);
            isValid = true;
        } catch (IllegalArgumentException e) {
            isValid = false;
        }
        return isValid;
    }
    /**
     * Increments the major version and resets minor and patch to 0.
     *
     * @param semVer the SemVer object to increment
     * @return a new SemVer object with incremented major version
     */
    public static SemVer nextMajor(final SemVer semVer) {
        return new SemVer((semVer.getMajor() + 1) + ".0.0");
    }

    /**
     * Increments the minor version and resets patch to 0.
     * @param semVer the SemVersion object
     * @return a new SemVer object with incremented minor version
     */
    public static SemVer nextMinor(final SemVer semVer) {
        return new SemVer(
            semVer.getMajor()
                + "."
                +
                (semVer.getMinor() + 1)
                + ".0"
        );
    }

    /**
     * Increments the patch version.
     * @param version the SemVersion object
     * @return a new SemVer object with incremented patch version
     */
    public static SemVer nextPatch(final SemVer version) {
        return new SemVer(
            version.getMajor()
                + "." + version.getMinor()
                + "." + (version.getPatch() + 1)
        );
    }


    /**
     * Sets the major version to the specified value and resets minor and
     * patch to 0.
     * @param version the SemVersion object
     * @param newMajor the new major version number
     * @return a new SemVer object with the specified major version
     * @throws IllegalArgumentException if newMajor is negative
     */
    public static SemVer withMajor(final SemVer version, final int newMajor) {
        if (newMajor < 0) {
            throw new IllegalArgumentException(
                "Major version cannot be negative: "
                    + newMajor
            );
        }
        return new SemVer(
            newMajor
                + ".0.0"
        );
    }

    /**
     * Sets the minor version to the specified value and resets patch to 0.
     *
     * @param version the SemVersion object
     * @param newMinor the new minor version number
     * @return a new SemVer object with the specified minor version
     * @throws IllegalArgumentException if newMinor is negative
     */
    public static SemVer withMinor(final SemVer version, final int newMinor) {
        if (newMinor < 0) {
            throw new IllegalArgumentException(
                "Minor version cannot be negative: "
                    + newMinor
            );
        }
        return new SemVer(
            version.getMajor() + "."
                + newMinor + ".0"
        );
    }

    /**
     * Sets the patch version to the specified value.
     *@param version the SemVersion object
     * @param newPatch the new patch version number
     * @return a new SemVer object with the specified patch version
     * @throws IllegalArgumentException if newPatch is negative
     */
    public static SemVer withPatch(final SemVer version, final int newPatch) {
        if (newPatch < 0) {
            throw new IllegalArgumentException(
                "Patch version cannot be negative: "
                    + newPatch
            );
        }
        return new SemVer(
            version.getMajor()
                + "." + version.getMinor()
                + "." + newPatch
        );
    }

}
