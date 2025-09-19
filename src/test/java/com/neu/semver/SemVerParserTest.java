package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for the SemVerParser class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVerParser Tests")
class SemVerParserTest {

    @Nested
    @DisplayName("Valid Version Parsing Tests")
    class ValidVersionParsingTests {

        @Test
        @DisplayName("Should parse basic version strings")
        void shouldParseBasicVersionStrings() {
            // Test basic versions
            SemVer version1 = SemVerParser.parse("1.0.0");
            assertEquals(1, version1.getMajor());
            assertEquals(0, version1.getMinor());
            assertEquals(0, version1.getPatch());
            assertNull(version1.getPrerelease());
            assertNull(version1.getBuild());
            assertEquals("1.0.0", version1.getOriginalVersion());

            SemVer version2 = SemVerParser.parse("2.5.10");
            assertEquals(2, version2.getMajor());
            assertEquals(5, version2.getMinor());
            assertEquals(10, version2.getPatch());
            assertNull(version2.getPrerelease());
            assertNull(version2.getBuild());
            assertEquals("2.5.10", version2.getOriginalVersion());
        }

        @Test
        @DisplayName("Should parse versions with prerelease identifiers")
        void shouldParseVersionsWithPrereleaseIdentifiers() {
            // Simple prerelease
            SemVer version1 = SemVerParser.parse("1.0.0-alpha");
            assertEquals(1, version1.getMajor());
            assertEquals(0, version1.getMinor());
            assertEquals(0, version1.getPatch());
            assertEquals("alpha", version1.getPrerelease());
            assertNull(version1.getBuild());
            assertEquals("1.0.0-alpha", version1.getOriginalVersion());

            // Complex prerelease
            SemVer version2 = SemVerParser.parse("1.0.0-alpha.1.beta");
            assertEquals(1, version2.getMajor());
            assertEquals(0, version2.getMinor());
            assertEquals(0, version2.getPatch());
            assertEquals("alpha.1.beta", version2.getPrerelease());
            assertNull(version2.getBuild());
            assertEquals("1.0.0-alpha.1.beta", version2.getOriginalVersion());

            // Numeric prerelease
            SemVer version3 = SemVerParser.parse("1.0.0-123");
            assertEquals(1, version3.getMajor());
            assertEquals(0, version3.getMinor());
            assertEquals(0, version3.getPatch());
            assertEquals("123", version3.getPrerelease());
            assertNull(version3.getBuild());
            assertEquals("1.0.0-123", version3.getOriginalVersion());
        }

        @Test
        @DisplayName("Should parse versions with build metadata")
        void shouldParseVersionsWithBuildMetadata() {
            // Simple build
            SemVer version1 = SemVerParser.parse("1.0.0+build");
            assertEquals(1, version1.getMajor());
            assertEquals(0, version1.getMinor());
            assertEquals(0, version1.getPatch());
            assertNull(version1.getPrerelease());
            assertEquals("build", version1.getBuild());
            assertEquals("1.0.0+build", version1.getOriginalVersion());

            // Complex build
            SemVer version2 = SemVerParser.parse("1.0.0+build.123.abc");
            assertEquals(1, version2.getMajor());
            assertEquals(0, version2.getMinor());
            assertEquals(0, version2.getPatch());
            assertNull(version2.getPrerelease());
            assertEquals("build.123.abc", version2.getBuild());
            assertEquals("1.0.0+build.123.abc", version2.getOriginalVersion());

            // Numeric build
            SemVer version3 = SemVerParser.parse("1.0.0+123");
            assertEquals(1, version3.getMajor());
            assertEquals(0, version3.getMinor());
            assertEquals(0, version3.getPatch());
            assertNull(version3.getPrerelease());
            assertEquals("123", version3.getBuild());
            assertEquals("1.0.0+123", version3.getOriginalVersion());
        }

        @Test
        @DisplayName("Should parse versions with both prerelease and build metadata")
        void shouldParseVersionsWithBothPrereleaseAndBuildMetadata() {
            SemVer version = SemVerParser.parse("1.0.0-alpha.1+build.123");
            assertEquals(1, version.getMajor());
            assertEquals(0, version.getMinor());
            assertEquals(0, version.getPatch());
            assertEquals("alpha.1", version.getPrerelease());
            assertEquals("build.123", version.getBuild());
            assertEquals("1.0.0-alpha.1+build.123", version.getOriginalVersion());
        }

        @Test
        @DisplayName("Should handle zero versions")
        void shouldHandleZeroVersions() {
            SemVer version = SemVerParser.parse("0.0.0");
            assertEquals(0, version.getMajor());
            assertEquals(0, version.getMinor());
            assertEquals(0, version.getPatch());
            assertNull(version.getPrerelease());
            assertNull(version.getBuild());
            assertEquals("0.0.0", version.getOriginalVersion());
        }

        @Test
        @DisplayName("Should handle large version numbers")
        void shouldHandleLargeVersionNumbers() {
            SemVer version = SemVerParser.parse("999999.888888.777777");
            assertEquals(999999, version.getMajor());
            assertEquals(888888, version.getMinor());
            assertEquals(777777, version.getPatch());
            assertNull(version.getPrerelease());
            assertNull(version.getBuild());
            assertEquals("999999.888888.777777", version.getOriginalVersion());
        }

        @Test
        @DisplayName("Should trim whitespace from version strings")
        void shouldTrimWhitespaceFromVersionStrings() {
            SemVer version = SemVerParser.parse("  1.2.3  ");
            assertEquals(1, version.getMajor());
            assertEquals(2, version.getMinor());
            assertEquals(3, version.getPatch());
            assertEquals("1.2.3", version.getOriginalVersion());
        }
    }

    @Nested
    @DisplayName("Invalid Version Parsing Tests")
    class InvalidVersionParsingTests {

        @Test
        @DisplayName("Should throw exception for null version")
        void shouldThrowExceptionForNullVersion() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerParser.parse(null)
            );
            assertEquals("Version string cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty version")
        void shouldThrowExceptionForEmptyVersion() {
            InvalidSemVerException exception1 = assertThrows(
                InvalidSemVerException.class,
                () -> SemVerParser.parse("")
            );
            assertEquals("Version string cannot be empty", exception1.getMessage());

            InvalidSemVerException exception2 = assertThrows(
                InvalidSemVerException.class,
                () -> SemVerParser.parse("   ")
            );
            assertEquals("Version string cannot be empty", exception2.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid version formats")
        void shouldThrowExceptionForInvalidVersionFormats() {
            String[] invalidVersions = {
                "1.2",                    // Missing patch
                "1.2.3.4",               // Too many parts
                "1.2.3-",                // Empty prerelease
                "1.2.3+",                // Empty build
                "1.2.3-+",               // Empty prerelease and build
                "01.2.3",                // Leading zero in major
                "1.02.3",                // Leading zero in minor
                "1.2.03",                // Leading zero in patch
                "1.2.3-01",              // Leading zero in prerelease
                "v1.2.3",                // Version prefix
                "1.2.3-alpha..1",        // Empty prerelease identifier
                "1.2.3+build..1",        // Empty build identifier
                "1.2.3-alpha+",          // Empty build
                "1.2.3-+build",          // Empty prerelease
                "1.2.3-alpha+build+extra", // Multiple build sections
                "1.2.3-",                // Trailing dash
                "1.2.3+",                // Trailing plus
                "1.2.3-+",               // Both empty
                "1.2.3-alpha-",          // Invalid prerelease format
                "1.2.3+build+",          // Invalid build format
                "1.2.3-alpha+build+",    // Invalid build format
                "1.2.3-alpha+build+extra+more", // Multiple build sections
                "1.2.3-alpha+build+extra+more+final" // Multiple build sections
            };

            for (String invalidVersion : invalidVersions) {
                InvalidSemVerException exception = assertThrows(
                    InvalidSemVerException.class,
                    () -> SemVerParser.parse(invalidVersion),
                    "Should throw exception for: " + invalidVersion
                );
                assertTrue(exception.getMessage().contains("Invalid semantic version: " + invalidVersion.trim()));
            }
        }

        @Test
        @DisplayName("Should throw exception for non-numeric version parts")
        void shouldThrowExceptionForNonNumericVersionParts() {
            String[] invalidVersions = {
                "a.2.3",     // Non-numeric major
                "1.b.3",     // Non-numeric minor
                "1.2.c",     // Non-numeric patch
                "1.2.3-",    // Empty prerelease
                "1.2.3+",    // Empty build
                "1.2.3-+",   // Both empty
                "1.2.3-alpha+build+extra" // Multiple build sections
            };

            for (String invalidVersion : invalidVersions) {
                assertThrows(
                    InvalidSemVerException.class,
                    () -> SemVerParser.parse(invalidVersion),
                    "Should throw exception for: " + invalidVersion
                );
            }
        }

        @Test
        @DisplayName("Should throw exception for negative version numbers")
        void shouldThrowExceptionForNegativeVersionNumbers() {
            String[] invalidVersions = {
                "-1.2.3",    // Negative major
                "1.-2.3",    // Negative minor
                "1.2.-3"     // Negative patch
            };

            for (String invalidVersion : invalidVersions) {
                assertThrows(
                    InvalidSemVerException.class,
                    () -> SemVerParser.parse(invalidVersion),
                    "Should throw exception for: " + invalidVersion
                );
            }
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle maximum integer values")
        void shouldHandleMaximumIntegerValues() {
            String maxVersion = Integer.MAX_VALUE + "." + Integer.MAX_VALUE + "." + Integer.MAX_VALUE;
            SemVer version = SemVerParser.parse(maxVersion);
            assertEquals(Integer.MAX_VALUE, version.getMajor());
            assertEquals(Integer.MAX_VALUE, version.getMinor());
            assertEquals(Integer.MAX_VALUE, version.getPatch());
            assertEquals(maxVersion, version.getOriginalVersion());
        }

        @Test
        @DisplayName("Should handle complex prerelease identifiers")
        void shouldHandleComplexPrereleaseIdentifiers() {
            String[] complexPrereleases = {
                "alpha.beta.gamma",
                "1.2.3.4.5",
                "alpha-1.beta-2.gamma-3",
                "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p"
            };

            for (String prerelease : complexPrereleases) {
                String versionString = "1.0.0-" + prerelease;
                SemVer version = SemVerParser.parse(versionString);
                assertEquals(prerelease, version.getPrerelease());
                assertEquals(versionString, version.getOriginalVersion());
            }
        }

        @Test
        @DisplayName("Should handle complex build metadata")
        void shouldHandleComplexBuildMetadata() {
            String[] complexBuilds = {
                "build.1.2.3.4.5",
                "build-alpha-beta-gamma",
                "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p"
            };

            for (String build : complexBuilds) {
                String versionString = "1.0.0+" + build;
                SemVer version = SemVerParser.parse(versionString);
                assertEquals(build, version.getBuild());
                assertEquals(versionString, version.getOriginalVersion());
            }
        }

        @Test
        @DisplayName("Should handle mixed alphanumeric identifiers")
        void shouldHandleMixedAlphanumericIdentifiers() {
            SemVer version = SemVerParser.parse("1.0.0-alpha1.beta2.gamma3+build1.build2.build3");
            assertEquals("alpha1.beta2.gamma3", version.getPrerelease());
            assertEquals("build1.build2.build3", version.getBuild());
            assertEquals("1.0.0-alpha1.beta2.gamma3+build1.build2.build3", version.getOriginalVersion());
        }
    }
}
