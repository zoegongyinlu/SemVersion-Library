package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for the SemVer class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVer Tests")
class SemVerTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SemVer from valid version string")
        void shouldCreateSemVerFromValidVersionString() {
            // Test basic version
            SemVer version = new SemVer("1.2.3");
            assertEquals(1, version.getMajor());
            assertEquals(2, version.getMinor());
            assertEquals(3, version.getPatch());
            assertNull(version.getPrerelease());
            assertNull(version.getBuild());
            assertFalse(version.isPrerelease());
            assertFalse(version.hasBuild());
            assertEquals("1.2.3", version.toString());

            // Test with prerelease
            version = new SemVer("1.2.3-alpha.1");
            assertEquals(1, version.getMajor());
            assertEquals(2, version.getMinor());
            assertEquals(3, version.getPatch());
            assertEquals("alpha.1", version.getPrerelease());
            assertNull(version.getBuild());
            assertTrue(version.isPrerelease());
            assertFalse(version.hasBuild());

            // Test with build metadata
            version = new SemVer("1.2.3+build.1");
            assertEquals(1, version.getMajor());
            assertEquals(2, version.getMinor());
            assertEquals(3, version.getPatch());
            assertNull(version.getPrerelease());
            assertEquals("build.1", version.getBuild());
            assertFalse(version.isPrerelease());
            assertTrue(version.hasBuild());

            // Test with both prerelease and build
            version = new SemVer("1.2.3-alpha.1+build.1");
            assertEquals(1, version.getMajor());
            assertEquals(2, version.getMinor());
            assertEquals(3, version.getPatch());
            assertEquals("alpha.1", version.getPrerelease());
            assertEquals("build.1", version.getBuild());
            assertTrue(version.isPrerelease());
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should handle zero versions")
        void shouldHandleZeroVersions() {
            SemVer version = new SemVer("0.0.0");
            assertEquals(0, version.getMajor());
            assertEquals(0, version.getMinor());
            assertEquals(0, version.getPatch());
        }

        @Test
        @DisplayName("Should trim whitespace from version string")
        void shouldTrimWhitespaceFromVersionString() {
            SemVer version = new SemVer("  1.2.3  ");
            assertEquals("1.2.3", version.getOriginalVersion());
        }

        @Test
        @DisplayName("Should throw exception for null version")
        void shouldThrowExceptionForNullVersion() {
            assertThrows(IllegalArgumentException.class, () -> new SemVer(null));
        }

        @Test
        @DisplayName("Should throw exception for empty version")
        void shouldThrowExceptionForEmptyVersion() {
            assertThrows(InvalidSemVerException.class, () -> new SemVer(""));
            assertThrows(InvalidSemVerException.class, () -> new SemVer("   "));
        }

        @Test
        @DisplayName("Should throw exception for invalid version formats")
        void shouldThrowExceptionForInvalidVersionFormats() {
            String[] invalidVersions = {
                "1.2",           // Missing patch
                "1.2.3.4",       // Too many parts
                "1.2.3-",        // Empty prerelease
                "1.2.3+",        // Empty build
                "1.2.3-+",       // Empty prerelease and build
                "01.2.3",        // Leading zero in major
                "1.02.3",        // Leading zero in minor
                "1.2.03",        // Leading zero in patch
                "1.2.3-01",      // Leading zero in prerelease
                "v1.2.3",        // Version prefix
                "1.2.3-",        // Trailing dash
                "1.2.3+",        // Trailing plus
                "1.2.3-+build",  // Empty prerelease
                "1.2.3-+",       // Both empty
                "1.2.3-alpha..1", // Empty prerelease identifier
                "1.2.3+build..1", // Empty build identifier
                "1.2.3-",        // Empty prerelease
                "1.2.3+",        // Empty build
                "1.2.3-alpha+",  // Empty build
                "1.2.3-+build",  // Empty prerelease
                "1.2.3-alpha+build+extra" // Multiple build sections
            };

            for (String invalidVersion : invalidVersions) {
                assertThrows(InvalidSemVerException.class, 
                    () -> new SemVer(invalidVersion),
                    "Should throw exception for: " + invalidVersion);
            }
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should compare versions correctly")
        void shouldCompareVersionsCorrectly() {
            // Test major version comparison
            assertTrue(new SemVer("2.0.0").compareTo(new SemVer("1.0.0")) > 0);
            assertTrue(new SemVer("1.0.0").compareTo(new SemVer("2.0.0")) < 0);

            // Test minor version comparison
            assertTrue(new SemVer("1.2.0").compareTo(new SemVer("1.1.0")) > 0);
            assertTrue(new SemVer("1.1.0").compareTo(new SemVer("1.2.0")) < 0);

            // Test patch version comparison
            assertTrue(new SemVer("1.1.2").compareTo(new SemVer("1.1.1")) > 0);
            assertTrue(new SemVer("1.1.1").compareTo(new SemVer("1.1.2")) < 0);

            // Test equal versions
            assertEquals(0, new SemVer("1.1.1").compareTo(new SemVer("1.1.1")));
        }

        @Test
        @DisplayName("Should handle prerelease version precedence")
        void shouldHandlePrereleaseVersionPrecedence() {
            // Prerelease versions have lower precedence than release versions
            assertTrue(new SemVer("1.0.0").compareTo(new SemVer("1.0.0-alpha")) > 0);
            assertTrue(new SemVer("1.0.0-alpha").compareTo(new SemVer("1.0.0")) < 0);

            // Compare prerelease versions
            assertTrue(new SemVer("1.0.0-alpha.1").compareTo(new SemVer("1.0.0-alpha")) > 0);
            assertTrue(new SemVer("1.0.0-alpha").compareTo(new SemVer("1.0.0-alpha.1")) < 0);

            // Numeric vs non-numeric identifiers
            assertTrue(new SemVer("1.0.0-alpha.1").compareTo(new SemVer("1.0.0-alpha.beta")) < 0);
            assertTrue(new SemVer("1.0.0-alpha.beta").compareTo(new SemVer("1.0.0-alpha.1")) > 0);
        }

        @Test
        @DisplayName("Should ignore build metadata in comparison")
        void shouldIgnoreBuildMetadataInComparison() {
            assertEquals(0, new SemVer("1.0.0").compareTo(new SemVer("1.0.0+build")));
            assertEquals(0, new SemVer("1.0.0+build1").compareTo(new SemVer("1.0.0+build2")));
        }

        @Test
        @DisplayName("Should throw exception when comparing with null")
        void shouldThrowExceptionWhenComparingWithNull() {
            SemVer version = new SemVer("1.0.0");
            assertThrows(IllegalArgumentException.class, () -> version.compareTo(null));
        }
    }

    @Nested
    @DisplayName("Increment Tests")
    class IncrementTests {

        @Test
        @DisplayName("Should increment major version correctly")
        void shouldIncrementMajorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("2.0.0", nextMajor.toString());
            assertFalse(nextMajor.isPrerelease());
            assertFalse(nextMajor.hasBuild());

            // Test with prerelease and build
            version = new SemVer("1.2.3-alpha.1+build.1");
            nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("2.0.0", nextMajor.toString());
            assertFalse(nextMajor.isPrerelease());
            assertFalse(nextMajor.hasBuild());
        }

        @Test
        @DisplayName("Should increment minor version correctly")
        void shouldIncrementMinorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("1.3.0", nextMinor.toString());
            assertFalse(nextMinor.isPrerelease());
            assertFalse(nextMinor.hasBuild());

            // Test with prerelease and build
            version = new SemVer("1.2.3-alpha.1+build.1");
            nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("1.3.0", nextMinor.toString());
            assertFalse(nextMinor.isPrerelease());
            assertFalse(nextMinor.hasBuild());
        }

        @Test
        @DisplayName("Should increment patch version correctly")
        void shouldIncrementPatchVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("1.2.4", nextPatch.toString());
            assertFalse(nextPatch.isPrerelease());
            assertFalse(nextPatch.hasBuild());

            // Test with prerelease and build
            version = new SemVer("1.2.3-alpha.1+build.1");
            nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("1.2.4", nextPatch.toString());
            assertFalse(nextPatch.isPrerelease());
            assertFalse(nextPatch.hasBuild());
        }

        @Test
        @DisplayName("Should set major version correctly")
        void shouldSetMajorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newMajor = SemVerUtils.withMajor(version, 5);
            assertEquals("5.0.0", newMajor.toString());
            assertFalse(newMajor.isPrerelease());
            assertFalse(newMajor.hasBuild());

            // Test with prerelease and build
            version = new SemVer("1.2.3-alpha.1+build.1");
            newMajor = SemVerUtils.withMajor(version, 5);
            assertEquals("5.0.0", newMajor.toString());
            assertFalse(newMajor.isPrerelease());
            assertFalse(newMajor.hasBuild());
        }

        @Test
        @DisplayName("Should set minor version correctly")
        void shouldSetMinorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newMinor = SemVerUtils.withMinor(version, 5);
            assertEquals("1.5.0", newMinor.toString());
            assertFalse(newMinor.isPrerelease());
            assertFalse(newMinor.hasBuild());
        }

        @Test
        @DisplayName("Should set patch version correctly")
        void shouldSetPatchVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newPatch = SemVerUtils.withPatch(version, 5);
            assertEquals("1.2.5", newPatch.toString());
            assertFalse(newPatch.isPrerelease());
            assertFalse(newPatch.hasBuild());
        }

        @Test
        @DisplayName("Should throw exception for negative version numbers")
        void shouldThrowExceptionForNegativeVersionNumbers() {
            SemVer version = new SemVer("1.2.3");
            
            assertThrows(IllegalArgumentException.class, () -> SemVerUtils.withMajor(version, -1));
            assertThrows(IllegalArgumentException.class, () -> SemVerUtils.withMinor(version, -1));
            assertThrows(IllegalArgumentException.class, () -> SemVerUtils.withPatch(version, -1));
        }
    }

    @Nested
    @DisplayName("Sorting Tests")
    class SortingTests {

        @Test
        @DisplayName("Should sort versions in descending order")
        void shouldSortVersionsInDescendingOrder() {
            List<String> versions = Arrays.asList("1.0.0", "2.0.0", "1.1.0", "1.0.1");
            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList("2.0.0", "1.1.0", "1.0.1", "1.0.0");
            assertEquals(expected, sorted);
        }

        @Test
        @DisplayName("Should handle prerelease versions in sorting")
        void shouldHandlePrereleaseVersionsInSorting() {
            List<String> versions = Arrays.asList("1.0.0", "1.0.0-alpha", "1.0.0-beta", "1.0.0-alpha.1");
            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList("1.0.0", "1.0.0-beta", "1.0.0-alpha.1", "1.0.0-alpha");
            assertEquals(expected, sorted);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<String> versions = Collections.emptyList();
            List<String> sorted = SemVerUtils.sortVersions(versions);
            assertTrue(sorted.isEmpty());
        }

        @Test
        @DisplayName("Should handle single version")
        void shouldHandleSingleVersion() {
            List<String> versions = Collections.singletonList("1.0.0");
            List<String> sorted = SemVerUtils.sortVersions(versions);
            assertEquals(versions, sorted);
        }

        @Test
        @DisplayName("Should throw exception for null list")
        void shouldThrowExceptionForNullList() {
            assertThrows(IllegalArgumentException.class, () -> SemVerUtils.sortVersions(null));
        }

        @Test
        @DisplayName("Should throw exception for null version in list")
        void shouldThrowExceptionForNullVersionInList() {
            List<String> versions = Arrays.asList("1.0.0", null, "2.0.0");
            assertThrows(IllegalArgumentException.class, () -> SemVerUtils.sortVersions(versions));
        }

        @Test
        @DisplayName("Should throw exception for invalid version in list")
        void shouldThrowExceptionForInvalidVersionInList() {
            List<String> versions = Arrays.asList("1.0.0", "invalid", "2.0.0");
            assertThrows(InvalidSemVerException.class, () -> SemVerUtils.sortVersions(versions));
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should be equal for same version")
        void shouldBeEqualForSameVersion() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("1.2.3");
            assertEquals(version1, version2);
            assertEquals(version1.hashCode(), version2.hashCode());
        }

        @Test
        @DisplayName("Should be equal ignoring build metadata")
        void shouldBeEqualIgnoringBuildMetadata() {
            SemVer version1 = new SemVer("1.2.3+build1");
            SemVer version2 = new SemVer("1.2.3+build2");
            assertEquals(version1, version2);
            assertEquals(version1.hashCode(), version2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different versions")
        void shouldNotBeEqualForDifferentVersions() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("1.2.4");
            assertNotEquals(version1, version2);
        }

        @Test
        @DisplayName("Should not be equal for different prerelease")
        void shouldNotBeEqualForDifferentPrerelease() {
            SemVer version1 = new SemVer("1.2.3-alpha");
            SemVer version2 = new SemVer("1.2.3-beta");
            assertNotEquals(version1, version2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToString() {
            SemVer version = new SemVer("1.2.3");
            assertNotEquals(version, null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void shouldNotBeEqualToDifferentClass() {
            SemVer version = new SemVer("1.2.3");
            assertNotEquals(version, "1.2.3");
        }
    }
}
