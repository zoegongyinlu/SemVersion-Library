package com.neu.semver;

import java.util.Objects;

/**
 * A Java implementation of SemVer 2.0.
 * Semantic versioning uses the format: MAJOR.MINOR.PATCH[-PRERELEASE][+BUILD]
 *
 * @author Yinlu Gong
 * @version 0.1.0
 */
public final class SemVer implements Comparable<SemVer> {



    /** The major version number. */
    private final int major;
    /** The minor version number. */
    private final int minor;
    /** The patch version number. */
    private final int patch;
    /** The prerelease identifier, or null if not present. */
    private final String prerelease;
    /** The build metadata, or null if not present. */
    private final String build;
    /** The original version string used to construct this SemVer object. */
    private final String originalVersion;

    /**
     * Internal constructor used by {@link SemVerParser} to create
     * a SemVer object directly from parsed fields.
     *
     * Package-private access modifier is intentional for internal use only.
     * This constructor is not part of the public API and should only be
     * used by classes within the same package.
     *
     * @param majorVersion the major version number
     * @param minorVersion the minor version number
     * @param patchVersion the patch version number
     * @param prereleaseVersion the prerelease identifier,
     *                          or null if not present
     * @param buildMetadata the build metadata,
     *                     or null if not present
     * @param originalString the original version string
     */
    SemVer(
        final int majorVersion,
        final int minorVersion,
        final int patchVersion,
        final String prereleaseVersion,
        final String buildMetadata,
        final String originalString
    ) {
        this.major = majorVersion;
        this.minor = minorVersion;
        this.patch = patchVersion;
        this.prerelease = prereleaseVersion;
        this.build = buildMetadata;
        this.originalVersion = originalString;
    }

    /**
     * Constructs a SemVer object from a version string.
     *
     * @param version the semantic version string to parse
     * @throws InvalidSemVerException if the version string is invalid
     * @throws IllegalArgumentException if the version string is null
     */
    public SemVer(final String version) {
        final SemVer parsed = SemVerParser.parse(version);
        this.major = parsed.major;
        this.minor = parsed.minor;
        this.patch = parsed.patch;
        this.prerelease = parsed.prerelease;
        this.build = parsed.build;
        this.originalVersion = parsed.originalVersion;
    }

    /**
     * Returns the major version number.
     * @return the major version number
     */
    public int getMajor() {
        return major;
    }

    /** Return the minor version number.
     * @return the minor version number
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the patch version number.
     *
     * @return the patch version number
     */
    public int getPatch() {
        return patch;
    }

    /** Return the prerelease identifier, or null if not present.
     * @return the prerelease identifier, or null if not present
     */
    public String getPrerelease() {
        return prerelease;
    }

    /** Return the build metadata, or null if not present.
     * @return the build metadata, or null if not present
     */
    public String getBuild() {
        return build;
    }

    /** Return the original version string.
     * @return the original version string
     */
    public String getOriginalVersion() {
        return originalVersion;
    }

    /**
     * Boolean checks if the version is prerelease version.
     *
     * @return true if it is prerelease version
     */
    public boolean isPrerelease() {
        return prerelease != null && !prerelease.isEmpty();
    }

    /**
     * Returns true if this version has build metadata.
     *
     * @return true if this version has build metadata
     */
    public boolean hasBuild() {
        return build != null && !build.isEmpty();
    }


    /**
     * Compares this SemVer object with another according to SemVer 2.0.0
     * precedence rules. Build metadata is ignored in precedence calculations.
     *
     * @param other the SemVer object to compare with
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object
     * @throws IllegalArgumentException if other semVer is empty or null
     */
    @Override
    public int compareTo(final SemVer other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null");
        }
        return SemVerComparator.compareVersions(this, other);
    }

    /**
     * Returns a string representation of this SemVer object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return originalVersion;
    }

    /**
     * Indicates whether some other object is equal to this SemVer object.
     * Two SemVer objects are equal if they have the same major, minor, patch,
     * and prerelease values. Build metadata is ignored in equality comparison
     *
     * @param obj the object to compare with
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && obj != null && getClass() == obj.getClass()) {
            final SemVer semVer = (SemVer) obj;
            result = major == semVer.major
                    && minor == semVer.minor
                    && patch == semVer.patch
                    && Objects.equals(prerelease, semVer.prerelease);
        }
        return result;
    }

    /**
     * Returns a hash code for this SemVer object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, prerelease);
    }
}
