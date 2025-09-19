package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for edge cases and complex scenarios in the SemVer class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVer Edge Cases Tests")
class SemVerEdgeCasesTest {

    @Nested
    @DisplayName("Complex Prerelease Tests")
    class ComplexPrereleaseTests {

        @Test
        @DisplayName("Should handle complex prerelease identifiers")
        void shouldHandleComplexPrereleaseIdentifiers() {
            // Test various prerelease formats
            SemVer version1 = new SemVer("1.0.0-alpha");
            SemVer version2 = new SemVer("1.0.0-alpha.1");
            SemVer version3 = new SemVer("1.0.0-alpha.beta");
            SemVer version4 = new SemVer("1.0.0-alpha.beta.1");
            SemVer version5 = new SemVer("1.0.0-alpha.1.beta");
            SemVer version6 = new SemVer("1.0.0-alpha.10");
            SemVer version7 = new SemVer("1.0.0-alpha.2");

            // Test precedence
            assertTrue(version1.compareTo(version2) < 0);
            assertTrue(version2.compareTo(version3) < 0);
            assertTrue(version3.compareTo(version4) < 0);
            assertTrue(version4.compareTo(version5) > 0); // alpha.beta.1 > alpha.1.beta (beta > 1)
            assertTrue(version6.compareTo(version7) > 0); // 10 > 2 numerically
        }

        @Test
        @DisplayName("Should handle mixed numeric and non-numeric prerelease identifiers")
        void shouldHandleMixedNumericAndNonNumericPrereleaseIdentifiers() {
            SemVer version1 = new SemVer("1.0.0-1");
            SemVer version2 = new SemVer("1.0.0-alpha");
            SemVer version3 = new SemVer("1.0.0-1.alpha");
            SemVer version4 = new SemVer("1.0.0-alpha.1");

            // Numeric identifiers have lower precedence than non-numeric
            assertTrue(version1.compareTo(version2) < 0);
            assertTrue(version3.compareTo(version4) < 0);
        }

        @Test
        @DisplayName("Should handle long prerelease identifiers")
        void shouldHandleLongPrereleaseIdentifiers() {
            SemVer version = new SemVer("1.0.0-alpha.beta.gamma.delta.epsilon");
            assertEquals("alpha.beta.gamma.delta.epsilon", version.getPrerelease());
            assertTrue(version.isPrerelease());
        }
    }

    @Nested
    @DisplayName("Complex Build Metadata Tests")
    class ComplexBuildMetadataTests {

        @Test
        @DisplayName("Should handle complex build metadata")
        void shouldHandleComplexBuildMetadata() {
            SemVer version = new SemVer("1.0.0+build.1.2.3");
            assertEquals("build.1.2.3", version.getBuild());
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should handle build metadata with special characters")
        void shouldHandleBuildMetadataWithSpecialCharacters() {
            SemVer version = new SemVer("1.0.0+build-123");
            assertEquals("build-123", version.getBuild());
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should ignore build metadata in comparison")
        void shouldIgnoreBuildMetadataInComparison() {
            SemVer version1 = new SemVer("1.0.0+build1");
            SemVer version2 = new SemVer("1.0.0+build2");
            SemVer version3 = new SemVer("1.0.0");

            assertEquals(0, version1.compareTo(version2));
            assertEquals(0, version1.compareTo(version3));
            assertEquals(0, version2.compareTo(version3));
        }
    }

    @Nested
    @DisplayName("Large Version Number Tests")
    class LargeVersionNumberTests {

        @Test
        @DisplayName("Should handle large version numbers")
        void shouldHandleLargeVersionNumbers() {
            SemVer version = new SemVer("999999.888888.777777");
            assertEquals(999999, version.getMajor());
            assertEquals(888888, version.getMinor());
            assertEquals(777777, version.getPatch());
        }

        @Test
        @DisplayName("Should handle maximum integer values")
        void shouldHandleMaximumIntegerValues() {
            SemVer version = new SemVer("2147483647.2147483647.2147483647");
            assertEquals(Integer.MAX_VALUE, version.getMajor());
            assertEquals(Integer.MAX_VALUE, version.getMinor());
            assertEquals(Integer.MAX_VALUE, version.getPatch());
        }
    }

    @Nested
    @DisplayName("Complex Sorting Tests")
    class ComplexSortingTests {

        @Test
        @DisplayName("Should sort complex version lists correctly")
        void shouldSortComplexVersionListsCorrectly() {
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

        @Test
        @DisplayName("Should sort versions with build metadata correctly")
        void shouldSortVersionsWithBuildMetadataCorrectly() {
            List<String> versions = Arrays.asList(
                "1.0.0+build1",
                "1.0.0+build2",
                "1.0.0",
                "2.0.0+build1"
            );

            List<String> sorted = SemVerUtils.sortVersions(versions);
            
            List<String> expected = Arrays.asList(
                "2.0.0+build1",
                "1.0.0+build1",
                "1.0.0+build2",
                "1.0.0"
            );

            assertEquals(expected, sorted);
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle boundary values for increment methods")
        void shouldHandleBoundaryValuesForIncrementMethods() {
            SemVer version = new SemVer("0.0.0");
            
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("1.0.0", nextMajor.toString());
            
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("0.1.0", nextMinor.toString());
            
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("0.0.1", nextPatch.toString());
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
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should preserve original version string")
        void shouldPreserveOriginalVersionString() {
            String originalVersion = "1.2.3-alpha.1+build.1";
            SemVer version = new SemVer(originalVersion);
            assertEquals(originalVersion, version.getOriginalVersion());
            assertEquals(originalVersion, version.toString());
        }

        @Test
        @DisplayName("Should handle trimmed version strings")
        void shouldHandleTrimmedVersionStrings() {
            String originalVersion = "  1.2.3  ";
            SemVer version = new SemVer(originalVersion);
            assertEquals("1.2.3", version.getOriginalVersion());
            assertEquals("1.2.3", version.toString());
        }
    }
}
