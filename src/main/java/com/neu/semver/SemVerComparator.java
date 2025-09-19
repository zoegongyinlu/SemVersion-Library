package com.neu.semver;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator implementing SemVer 2.0.0 precedence rules.
 * @author Yinlu Gong
 * @version 0.1.0
 */
public final class SemVerComparator implements
    Comparator<SemVer>, Serializable {

    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** Singleton instance. */
    public static final SemVerComparator INSTANCE = new SemVerComparator();

    /** Private constructor to prevent instantiation. */
    private SemVerComparator() { }

    /**
     * Compares two SemVer objects according to SemVer 2.0.0 precedence rules.
     * This static method provides a clean interface that avoids Law of Demeter
     * violations by not requiring direct access to the singleton instance.
     * The comparison follows these precedence rules:
     * 1. Major version numbers are compared first
     * 2. If major versions are equal, minor versions are compared
     * 3. If minor versions are equal, patch versions are compared
     * 4. If patch versions are equal, prerelease identifiers are compared
     * 5. Build metadata is ignored in precedence calculations
     * For prerelease comparison:
     * - A version without prerelease has higher precedence than
     * one with prerelease
     * - Prerelease identifiers are compared dot-separated
     * - Numeric identifiers have lower precedence than non-numeric identifiers
     * - When both are numeric, they are compared numerically
     * - When both are non-numeric, they are compared lexicographically
     *
     * @param version1 the first SemVer object to compare
     * @param version2 the second SemVer object to compare
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second
     * @throws IllegalArgumentException if either version is null
     */
    public static int compareVersions(
        final SemVer version1,
        final SemVer version2
    ) {
        if (
            version1 == null
                || version2 == null) {
            throw new IllegalArgumentException(
                "Versions must not be null"
            );
        }
        return INSTANCE.compare(version1, version2);
    }

    @Override
    public int compare(final SemVer version1, final SemVer version2) {
        int result = Integer.compare(version1.getMajor(), version2.getMajor());
        if (result == 0) {
            result = Integer.compare(version1.getMinor(), version2.getMinor());
        }
        if (result == 0) {
            result = Integer.compare(version1.getPatch(), version2.getPatch());
        }
        if (result == 0) {
            result = comparePrerelease(
                version1.getPrerelease(),
                version2.getPrerelease());
        }
        return result;
    }

    /**
     * Compares prerelease identifiers according to SemVer 2.0.0 rules.
     *
     * @param prerelease1 first prerelease identifier
     * @param prerelease2 second prerelease identifier
     * @return comparison result
     */
    private static int comparePrerelease(
        final String prerelease1,
        final String prerelease2) {
        int result;
        if (prerelease1 == null && prerelease2 == null) {
            result = 0;
        } else if (prerelease1 == null) {
            result = 1;
        } else if (prerelease2 == null) {
            result = -1;
        } else {
            final String[] identifiers1 = prerelease1.split("\\.");
            final String[] identifiers2 = prerelease2.split("\\.");
            final int minLength = Math.min(
                identifiers1.length,
                identifiers2.length
            );
            result = 0;
            for (int i = 0; i < minLength && result == 0; i++) {
                result = compareIdentifier(
                    identifiers1[i],
                    identifiers2[i]
                );
            }
            if (result == 0) {
                result = Integer.compare(
                    identifiers1.length,
                    identifiers2.length
                );
            }
        }
        return result;
    }

    /**
     * Compares two prerelease identifiers.
     * Numeric identifiers have lower
     * precedence than non-numeric identifiers.
     *
     * @param identifier1 first identifier
     * @param identifier2 second identifier
     * @return comparison result
     */
    private static int compareIdentifier(
        final String identifier1,
        final String identifier2
    ) {
        final boolean isNumeric1 = isNumeric(identifier1);
        final boolean isNumeric2 = isNumeric(identifier2);
        int result;
        if (isNumeric1 && isNumeric2) {
            result = Integer.compare(
                Integer.parseInt(identifier1),
                Integer.parseInt(identifier2)
            );
        } else if (isNumeric1) {
            result = -1;
        } else if (isNumeric2) {
            result = 1;
        } else {
            result = identifier1.compareTo(identifier2);
        }
        return result;
    }

    /**
     * Determines whether a string is a valid non-negative integer.
     *
     * @param value value to test
     * @return true if numeric, false otherwise
     */
    private static boolean isNumeric(final String value) {
        boolean result = value != null && !value.isEmpty();
        if (result) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                result = false;
            }
        }
        return result;
    }
}

