package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Unit tests for the SemVerComparator class.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVerComparator Tests")
class SemVerComparatorTest {

    @Nested
    @DisplayName("Basic Version Comparison Tests")
    class BasicVersionComparisonTests {

        @Test
        @DisplayName("Should compare major versions correctly")
        void shouldCompareMajorVersionsCorrectly() {
            SemVer version1 = new SemVer("1.0.0");
            SemVer version2 = new SemVer("2.0.0");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version1));
        }

        @Test
        @DisplayName("Should compare minor versions correctly")
        void shouldCompareMinorVersionsCorrectly() {
            SemVer version1 = new SemVer("1.1.0");
            SemVer version2 = new SemVer("1.2.0");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version1));
        }

        @Test
        @DisplayName("Should compare patch versions correctly")
        void shouldComparePatchVersionsCorrectly() {
            SemVer version1 = new SemVer("1.1.1");
            SemVer version2 = new SemVer("1.1.2");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version1));
        }

        @Test
        @DisplayName("Should return zero for identical versions")
        void shouldReturnZeroForIdenticalVersions() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("1.2.3");
            
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version2));
        }
    }

    @Nested
    @DisplayName("Prerelease Version Comparison Tests")
    class PrereleaseVersionComparisonTests {

        @Test
        @DisplayName("Should handle prerelease vs release version precedence")
        void shouldHandlePrereleaseVsReleaseVersionPrecedence() {
            SemVer releaseVersion = new SemVer("1.0.0");
            SemVer prereleaseVersion = new SemVer("1.0.0-alpha");
            
            // Release versions have higher precedence than prerelease versions
            assertTrue(SemVerComparator.INSTANCE.compare(releaseVersion, prereleaseVersion) > 0);
            assertTrue(SemVerComparator.INSTANCE.compare(prereleaseVersion, releaseVersion) < 0);
        }

        @Test
        @DisplayName("Should compare prerelease versions with different lengths")
        void shouldComparePrereleaseVersionsWithDifferentLengths() {
            SemVer shortPrerelease = new SemVer("1.0.0-alpha");
            SemVer longPrerelease = new SemVer("1.0.0-alpha.1");
            
            // Longer prerelease has higher precedence
            assertTrue(SemVerComparator.INSTANCE.compare(shortPrerelease, longPrerelease) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(longPrerelease, shortPrerelease) > 0);
        }

        @Test
        @DisplayName("Should compare numeric vs non-numeric prerelease identifiers")
        void shouldCompareNumericVsNonNumericPrereleaseIdentifiers() {
            SemVer numericPrerelease = new SemVer("1.0.0-1");
            SemVer nonNumericPrerelease = new SemVer("1.0.0-alpha");
            
            // Non-numeric identifiers have higher precedence than numeric
            assertTrue(SemVerComparator.INSTANCE.compare(numericPrerelease, nonNumericPrerelease) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(nonNumericPrerelease, numericPrerelease) > 0);
        }

        @Test
        @DisplayName("Should compare numeric prerelease identifiers numerically")
        void shouldCompareNumericPrereleaseIdentifiersNumerically() {
            SemVer version1 = new SemVer("1.0.0-1");
            SemVer version2 = new SemVer("1.0.0-2");
            SemVer version3 = new SemVer("1.0.0-10");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version3) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version3, version2) > 0);
        }

        @Test
        @DisplayName("Should compare non-numeric prerelease identifiers lexicographically")
        void shouldCompareNonNumericPrereleaseIdentifiersLexicographically() {
            SemVer version1 = new SemVer("1.0.0-alpha");
            SemVer version2 = new SemVer("1.0.0-beta");
            SemVer version3 = new SemVer("1.0.0-gamma");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version3) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version3, version2) > 0);
        }

        @Test
        @DisplayName("Should handle mixed numeric and non-numeric prerelease identifiers")
        void shouldHandleMixedNumericAndNonNumericPrereleaseIdentifiers() {
            SemVer version1 = new SemVer("1.0.0-alpha.1");
            SemVer version2 = new SemVer("1.0.0-alpha.beta");
            SemVer version3 = new SemVer("1.0.0-1.alpha");
            SemVer version4 = new SemVer("1.0.0-alpha.1");
            
            // alpha.1 vs alpha.beta: 1 (numeric) < beta (non-numeric)
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
            
            // 1.alpha vs alpha.1: 1 (numeric) < alpha (non-numeric)
            assertTrue(SemVerComparator.INSTANCE.compare(version3, version4) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version4, version3) > 0);
        }

        @Test
        @DisplayName("Should handle complex prerelease comparisons")
        void shouldHandleComplexPrereleaseComparisons() {
            SemVer version1 = new SemVer("1.0.0-alpha");
            SemVer version2 = new SemVer("1.0.0-alpha.1");
            SemVer version3 = new SemVer("1.0.0-alpha.beta");
            SemVer version4 = new SemVer("1.0.0-alpha.beta.1");
            SemVer version5 = new SemVer("1.0.0-alpha.1.beta");
            
            // Test precedence order: alpha < alpha.1 < alpha.beta < alpha.beta.1 < alpha.1.beta
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version3) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version3, version4) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version4, version5) > 0);
        }
    }

    @Nested
    @DisplayName("Build Metadata Tests")
    class BuildMetadataTests {

        @Test
        @DisplayName("Should ignore build metadata in comparison")
        void shouldIgnoreBuildMetadataInComparison() {
            SemVer version1 = new SemVer("1.0.0");
            SemVer version2 = new SemVer("1.0.0+build1");
            SemVer version3 = new SemVer("1.0.0+build2");
            
            // Build metadata should be ignored in comparison
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version2));
            assertEquals(0, SemVerComparator.INSTANCE.compare(version2, version3));
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version3));
        }

        @Test
        @DisplayName("Should ignore build metadata with prerelease versions")
        void shouldIgnoreBuildMetadataWithPrereleaseVersions() {
            SemVer version1 = new SemVer("1.0.0-alpha");
            SemVer version2 = new SemVer("1.0.0-alpha+build1");
            SemVer version3 = new SemVer("1.0.0-alpha+build2");
            
            // Build metadata should be ignored even with prerelease
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version2));
            assertEquals(0, SemVerComparator.INSTANCE.compare(version2, version3));
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version3));
        }
    }

    @Nested
    @DisplayName("Static Method Tests")
    class StaticMethodTests {

        @Test
        @DisplayName("Should work with static compareVersions method")
        void shouldWorkWithStaticCompareVersionsMethod() {
            SemVer version1 = new SemVer("1.0.0");
            SemVer version2 = new SemVer("2.0.0");
            
            assertTrue(SemVerComparator.compareVersions(version1, version2) < 0);
            assertTrue(SemVerComparator.compareVersions(version2, version1) > 0);
            assertEquals(0, SemVerComparator.compareVersions(version1, version1));
        }

        @Test
        @DisplayName("Should return same results as instance method")
        void shouldReturnSameResultsAsInstanceMethod() {
            SemVer version1 = new SemVer("1.0.0-alpha");
            SemVer version2 = new SemVer("1.0.0-beta");
            
            int staticResult = SemVerComparator.compareVersions(version1, version2);
            int instanceResult = SemVerComparator.INSTANCE.compare(version1, version2);
            
            assertEquals(staticResult, instanceResult);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle large version numbers")
        void shouldHandleLargeVersionNumbers() {
            SemVer version1 = new SemVer("999999.0.0");
            SemVer version2 = new SemVer("1000000.0.0");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
        }

        @Test
        @DisplayName("Should handle maximum integer values")
        void shouldHandleMaximumIntegerValues() {
            String maxVersion = Integer.MAX_VALUE + ".0.0";
            SemVer version1 = new SemVer(maxVersion);
            SemVer version2 = new SemVer(maxVersion);
            
            assertEquals(0, SemVerComparator.INSTANCE.compare(version1, version2));
        }

        @Test
        @DisplayName("Should handle zero versions")
        void shouldHandleZeroVersions() {
            SemVer version1 = new SemVer("0.0.0");
            SemVer version2 = new SemVer("0.0.1");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
        }

        @Test
        @DisplayName("Should handle very long prerelease identifiers")
        void shouldHandleVeryLongPrereleaseIdentifiers() {
            SemVer version1 = new SemVer("1.0.0-alpha.beta.gamma.delta.epsilon");
            SemVer version2 = new SemVer("1.0.0-alpha.beta.gamma.delta.epsilon.zeta");
            
            assertTrue(SemVerComparator.INSTANCE.compare(version1, version2) < 0);
            assertTrue(SemVerComparator.INSTANCE.compare(version2, version1) > 0);
        }

    }

    @Nested
    @DisplayName("Comprehensive Comparison Tests")
    class ComprehensiveComparisonTests {

        @Test
        @DisplayName("Should maintain correct precedence order for complex versions")
        void shouldMaintainCorrectPrecedenceOrderForComplexVersions() {
            SemVer[] versions = {
                new SemVer("1.0.0-alpha"),
                new SemVer("1.0.0-alpha.1"),
                new SemVer("1.0.0-alpha.beta"),
                new SemVer("1.0.0-beta"),
                new SemVer("1.0.0-beta.2"),
                new SemVer("1.0.0-beta.11"),
                new SemVer("1.0.0-rc.1"),
                new SemVer("1.0.0"),
                new SemVer("1.0.1"),
                new SemVer("1.1.0"),
                new SemVer("2.0.0")
            };

            // Test that each version is less than the next one
            for (int i = 0; i < versions.length - 1; i++) {
                assertTrue(
                    SemVerComparator.INSTANCE.compare(versions[i], versions[i + 1]) < 0,
                    "Version " + versions[i] + " should be less than " + versions[i + 1]
                );
                assertTrue(
                    SemVerComparator.INSTANCE.compare(versions[i + 1], versions[i]) > 0,
                    "Version " + versions[i + 1] + " should be greater than " + versions[i]
                );
            }
        }

        @Test
        @DisplayName("Should handle versions with build metadata in precedence order")
        void shouldHandleVersionsWithBuildMetadataInPrecedenceOrder() {
            SemVer[] versions = {
                new SemVer("1.0.0-alpha+build1"),
                new SemVer("1.0.0-alpha.1+build1"),
                new SemVer("1.0.0-beta+build1"),
                new SemVer("1.0.0+build1"),
                new SemVer("1.0.1+build1")
            };

            // Test that each version is less than the next one (ignoring build metadata)
            for (int i = 0; i < versions.length - 1; i++) {
                assertTrue(
                    SemVerComparator.INSTANCE.compare(versions[i], versions[i + 1]) < 0,
                    "Version " + versions[i] + " should be less than " + versions[i + 1]
                );
            }
        }
    }
}
