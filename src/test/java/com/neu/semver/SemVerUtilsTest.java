package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for the SemVerUtils class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVerUtils Tests")
class SemVerUtilsTest {

    @Nested
    @DisplayName("Version Sorting Tests")
    class VersionSortingTests {

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
        @DisplayName("Should handle build metadata in sorting")
        void shouldHandleBuildMetadataInSorting() {
            List<String> versions = Arrays.asList("1.0.0+build1", "1.0.0+build2", "1.0.0", "2.0.0+build1");
            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList("2.0.0+build1", "1.0.0+build1", "1.0.0+build2", "1.0.0");
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
        @DisplayName("Should handle duplicate versions")
        void shouldHandleDuplicateVersions() {
            List<String> versions = Arrays.asList("1.0.0", "2.0.0", "1.0.0", "2.0.0");
            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList("2.0.0", "2.0.0", "1.0.0", "1.0.0");
            assertEquals(expected, sorted);
        }

        @Test
        @DisplayName("Should throw exception for null list")
        void shouldThrowExceptionForNullList() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.sortVersions(null)
            );
            assertEquals("Versions list cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for null version in list")
        void shouldThrowExceptionForNullVersionInList() {
            List<String> versions = Arrays.asList("1.0.0", null, "2.0.0");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.sortVersions(versions)
            );
            assertEquals("Version string cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid version in list")
        void shouldThrowExceptionForInvalidVersionInList() {
            List<String> versions = Arrays.asList("1.0.0", "invalid", "2.0.0");
            assertThrows(InvalidSemVerException.class, () -> SemVerUtils.sortVersions(versions));
        }
    }

    @Nested
    @DisplayName("Find Highest Version Tests")
    class FindHighestVersionTests {

        @Test
        @DisplayName("Should find highest version from list")
        void shouldFindHighestVersionFromList() {
            List<String> versions = Arrays.asList("1.0.0", "2.0.0", "1.1.0", "1.0.1");
            String highest = SemVerUtils.findHighestVersion(versions);
            assertEquals("2.0.0", highest);
        }

        @Test
        @DisplayName("Should find highest version with prerelease")
        void shouldFindHighestVersionWithPrerelease() {
            List<String> versions = Arrays.asList("1.0.0-alpha", "1.0.0-beta", "1.0.0", "1.0.0-alpha.1");
            String highest = SemVerUtils.findHighestVersion(versions);
            assertEquals("1.0.0", highest);
        }

        @Test
        @DisplayName("Should find highest version with build metadata")
        void shouldFindHighestVersionWithBuildMetadata() {
            List<String> versions = Arrays.asList("1.0.0+build1", "1.0.0+build2", "2.0.0+build1");
            String highest = SemVerUtils.findHighestVersion(versions);
            assertEquals("2.0.0+build1", highest);
        }

        @Test
        @DisplayName("Should handle single version")
        void shouldHandleSingleVersion() {
            List<String> versions = Collections.singletonList("1.0.0");
            String highest = SemVerUtils.findHighestVersion(versions);
            assertEquals("1.0.0", highest);
        }

        @Test
        @DisplayName("Should throw exception for null list")
        void shouldThrowExceptionForNullList() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.findHighestVersion(null)
            );
            assertEquals("Versions list cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty list")
        void shouldThrowExceptionForEmptyList() {
            List<String> versions = Collections.emptyList();
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.findHighestVersion(versions)
            );
            assertEquals("Versions list cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid version in list")
        void shouldThrowExceptionForInvalidVersionInList() {
            List<String> versions = Arrays.asList("1.0.0", "invalid", "2.0.0");
            assertThrows(InvalidSemVerException.class, () -> SemVerUtils.findHighestVersion(versions));
        }
    }

    @Nested
    @DisplayName("Version Validation Tests")
    class VersionValidationTests {

        @Test
        @DisplayName("Should return true for valid versions")
        void shouldReturnTrueForValidVersions() {
            String[] validVersions = {
                "1.0.0",
                "0.0.0",
                "999999.888888.777777",
                "1.0.0-alpha",
                "1.0.0-alpha.1",
                "1.0.0+build",
                "1.0.0-alpha+build",
                "1.0.0-alpha.1+build.123"
            };

            for (String version : validVersions) {
                assertTrue(SemVerUtils.isValidVersion(version), 
                    "Version should be valid: " + version);
            }
        }

        @Test
        @DisplayName("Should return false for invalid versions")
        void shouldReturnFalseForInvalidVersions() {
            String[] invalidVersions = {
                null,
                "",
                "   ",
                "1.2",
                "1.2.3.4",
                "01.2.3",
                "1.02.3",
                "1.2.03",
                "1.2.3-",
                "1.2.3+",
                "1.2.3-+",
                "v1.2.3",
                "1.2.3-alpha..1",
                "1.2.3+build..1",
                "invalid",
                "1.2.3-alpha+build+extra"
            };

            for (String version : invalidVersions) {
                assertFalse(SemVerUtils.isValidVersion(version), 
                    "Version should be invalid: " + version);
            }
        }
    }

    @Nested
    @DisplayName("Static Version Manipulation Tests")
    class StaticVersionManipulationTests {

        @Test
        @DisplayName("Should increment patch version correctly")
        void shouldIncrementPatchVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer incremented = SemVerUtils.nextPatch(original);
            
            assertEquals("1.2.4", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }

        @Test
        @DisplayName("Should increment patch version with prerelease and build")
        void shouldIncrementPatchVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer incremented = SemVerUtils.nextPatch(original);
            
            assertEquals("1.2.4", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }

        @Test
        @DisplayName("Should set major version correctly")
        void shouldSetMajorVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withMajor(original, 5);
            
            assertEquals("5.0.0", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set major version with prerelease and build")
        void shouldSetMajorVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer newVersion = SemVerUtils.withMajor(original, 5);
            
            assertEquals("5.0.0", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set minor version correctly")
        void shouldSetMinorVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withMinor(original, 5);
            
            assertEquals("1.5.0", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set minor version with prerelease and build")
        void shouldSetMinorVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer newVersion = SemVerUtils.withMinor(original, 5);
            
            assertEquals("1.5.0", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set patch version correctly")
        void shouldSetPatchVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withPatch(original, 5);
            
            assertEquals("1.2.5", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set patch version with prerelease and build")
        void shouldSetPatchVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer newVersion = SemVerUtils.withPatch(original, 5);
            
            assertEquals("1.2.5", newVersion.toString());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should throw exception for negative major version")
        void shouldThrowExceptionForNegativeMajorVersion() {
            SemVer original = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withMajor(original, -1)
            );
            assertEquals("Major version cannot be negative: -1", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative minor version")
        void shouldThrowExceptionForNegativeMinorVersion() {
            SemVer original = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withMinor(original, -1)
            );
            assertEquals("Minor version cannot be negative: -1", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative patch version")
        void shouldThrowExceptionForNegativePatchVersion() {
            SemVer original = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withPatch(original, -1)
            );
            assertEquals("Patch version cannot be negative: -1", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Version Increment Tests")
    class VersionIncrementTests {

        @Test
        @DisplayName("Should increment major version correctly")
        void shouldIncrementMajorVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer incremented = SemVerUtils.nextMajor(original);
            
            assertEquals("2.0.0", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }

        @Test
        @DisplayName("Should increment major version with prerelease and build")
        void shouldIncrementMajorVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer incremented = SemVerUtils.nextMajor(original);
            
            assertEquals("2.0.0", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }

        @Test
        @DisplayName("Should increment minor version correctly")
        void shouldIncrementMinorVersionCorrectly() {
            SemVer original = new SemVer("1.2.3");
            SemVer incremented = SemVerUtils.nextMinor(original);
            
            assertEquals("1.3.0", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }

        @Test
        @DisplayName("Should increment minor version with prerelease and build")
        void shouldIncrementMinorVersionWithPrereleaseAndBuild() {
            SemVer original = new SemVer("1.2.3-alpha.1+build.1");
            SemVer incremented = SemVerUtils.nextMinor(original);
            
            assertEquals("1.3.0", incremented.toString());
            assertFalse(incremented.isPrerelease());
            assertFalse(incremented.hasBuild());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle boundary values for increment methods")
        void shouldHandleBoundaryValuesForIncrementMethods() {
            SemVer version = new SemVer("0.0.0");
            
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("0.0.1", nextPatch.toString());
            
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("0.1.0", nextMinor.toString());
            
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("1.0.0", nextMajor.toString());
        }

        @Test
        @DisplayName("Should handle boundary values for with methods")
        void shouldHandleBoundaryValuesForWithMethods() {
            SemVer version = new SemVer("1.2.3");
            
            SemVer withZeroMajor = SemVerUtils.withMajor(version, 0);
            assertEquals("0.0.0", withZeroMajor.toString());
            
            SemVer withZeroMinor = SemVerUtils.withMinor(version, 0);
            assertEquals("1.0.0", withZeroMinor.toString());
            
            SemVer withZeroPatch = SemVerUtils.withPatch(version, 0);
            assertEquals("1.2.0", withZeroPatch.toString());
        }

        @Test
        @DisplayName("Should handle maximum values for with methods")
        void shouldHandleMaximumValuesForWithMethods() {
            SemVer version = new SemVer("1.2.3");
            
            SemVer withMaxMajor = SemVerUtils.withMajor(version, Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE + ".0.0", withMaxMajor.toString());
            
            SemVer withMaxMinor = SemVerUtils.withMinor(version, Integer.MAX_VALUE);
            assertEquals("1." + Integer.MAX_VALUE + ".0", withMaxMinor.toString());
            
            SemVer withMaxPatch = SemVerUtils.withPatch(version, Integer.MAX_VALUE);
            assertEquals("1.2." + Integer.MAX_VALUE, withMaxPatch.toString());
        }

        @Test
        @DisplayName("Should handle large version numbers in sorting")
        void shouldHandleLargeVersionNumbersInSorting() {
            List<String> versions = Arrays.asList(
                "999999.0.0",
                "1000000.0.0",
                "999999.999999.999999"
            );
            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList(
                "1000000.0.0",
                "999999.999999.999999",
                "999999.0.0"
            );
            assertEquals(expected, sorted);
        }

        @Test
        @DisplayName("Should handle complex version lists")
        void shouldHandleComplexVersionLists() {
            List<String> versions = Arrays.asList(
                "1.0.0-alpha",
                "1.0.0-alpha.1",
                "1.0.0-alpha.beta",
                "1.0.0-beta",
                "1.0.0-beta.2",
                "1.0.0-beta.11",
                "1.0.0-rc.1",
                "1.0.0",
                "1.0.1",
                "1.1.0",
                "2.0.0"
            );

            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList(
                "2.0.0",
                "1.1.0",
                "1.0.1",
                "1.0.0",
                "1.0.0-rc.1",
                "1.0.0-beta.11",
                "1.0.0-beta.2",
                "1.0.0-beta",
                "1.0.0-alpha.beta",
                "1.0.0-alpha.1",
                "1.0.0-alpha"
            );

            assertEquals(expected, sorted);
        }
    }
}
