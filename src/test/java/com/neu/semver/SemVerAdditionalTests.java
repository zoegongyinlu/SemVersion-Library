package com.neu.semver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Additional comprehensive tests for the SemVer class to cover all methods and edge cases.
 * 
 * @author Yinlu Gong
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("SemVer Additional Tests")
class SemVerAdditionalTests {

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct major version")
        void shouldReturnCorrectMajorVersion() {
            SemVer version = new SemVer("5.2.8");
            assertEquals(5, version.getMajor());
        }

        @Test
        @DisplayName("Should return correct minor version")
        void shouldReturnCorrectMinorVersion() {
            SemVer version = new SemVer("5.2.8");
            assertEquals(2, version.getMinor());
        }

        @Test
        @DisplayName("Should return correct patch version")
        void shouldReturnCorrectPatchVersion() {
            SemVer version = new SemVer("5.2.8");
            assertEquals(8, version.getPatch());
        }

        @Test
        @DisplayName("Should return correct prerelease identifier")
        void shouldReturnCorrectPrereleaseIdentifier() {
            SemVer version = new SemVer("1.0.0-alpha.1");
            assertEquals("alpha.1", version.getPrerelease());
        }

        @Test
        @DisplayName("Should return null for prerelease when not present")
        void shouldReturnNullForPrereleaseWhenNotPresent() {
            SemVer version = new SemVer("1.0.0");
            assertNull(version.getPrerelease());
        }

        @Test
        @DisplayName("Should return correct build metadata")
        void shouldReturnCorrectBuildMetadata() {
            SemVer version = new SemVer("1.0.0+build.123");
            assertEquals("build.123", version.getBuild());
        }

        @Test
        @DisplayName("Should return null for build when not present")
        void shouldReturnNullForBuildWhenNotPresent() {
            SemVer version = new SemVer("1.0.0");
            assertNull(version.getBuild());
        }

        @Test
        @DisplayName("Should return original version string")
        void shouldReturnOriginalVersionString() {
            String originalVersion = "1.0.0-alpha.1+build.123";
            SemVer version = new SemVer(originalVersion);
            assertEquals(originalVersion, version.getOriginalVersion());
        }
    }

    @Nested
    @DisplayName("Boolean Check Method Tests")
    class BooleanCheckMethodTests {

        @Test
        @DisplayName("Should return true for prerelease version")
        void shouldReturnTrueForPrereleaseVersion() {
            SemVer version = new SemVer("1.0.0-alpha");
            assertTrue(version.isPrerelease());
        }

        @Test
        @DisplayName("Should return false for non-prerelease version")
        void shouldReturnFalseForNonPrereleaseVersion() {
            SemVer version = new SemVer("1.0.0");
            assertFalse(version.isPrerelease());
        }

        @Test
        @DisplayName("Should return true for version with build metadata")
        void shouldReturnTrueForVersionWithBuildMetadata() {
            SemVer version = new SemVer("1.0.0+build");
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should return false for version without build metadata")
        void shouldReturnFalseForVersionWithoutBuildMetadata() {
            SemVer version = new SemVer("1.0.0");
            assertFalse(version.hasBuild());
        }

        @Test
        @DisplayName("Should return true for version with both prerelease and build")
        void shouldReturnTrueForVersionWithBothPrereleaseAndBuild() {
            SemVer version = new SemVer("1.0.0-alpha+build");
            assertTrue(version.isPrerelease());
            assertTrue(version.hasBuild());
        }
    }

    @Nested
    @DisplayName("Increment Method Tests")
    class IncrementMethodTests {

        @Test
        @DisplayName("Should increment major version correctly")
        void shouldIncrementMajorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            
            assertEquals("2.0.0", nextMajor.toString());
            assertEquals(2, nextMajor.getMajor());
            assertEquals(0, nextMajor.getMinor());
            assertEquals(0, nextMajor.getPatch());
            assertFalse(nextMajor.isPrerelease());
            assertFalse(nextMajor.hasBuild());
        }

        @Test
        @DisplayName("Should increment minor version correctly")
        void shouldIncrementMinorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            
            assertEquals("1.3.0", nextMinor.toString());
            assertEquals(1, nextMinor.getMajor());
            assertEquals(3, nextMinor.getMinor());
            assertEquals(0, nextMinor.getPatch());
            assertFalse(nextMinor.isPrerelease());
            assertFalse(nextMinor.hasBuild());
        }

        @Test
        @DisplayName("Should increment patch version correctly")
        void shouldIncrementPatchVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            
            assertEquals("1.2.4", nextPatch.toString());
            assertEquals(1, nextPatch.getMajor());
            assertEquals(2, nextPatch.getMinor());
            assertEquals(4, nextPatch.getPatch());
            assertFalse(nextPatch.isPrerelease());
            assertFalse(nextPatch.hasBuild());
        }

        @Test
        @DisplayName("Should handle zero versions in increment methods")
        void shouldHandleZeroVersionsInIncrementMethods() {
            SemVer version = new SemVer("0.0.0");
            
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("1.0.0", nextMajor.toString());
            
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("0.1.0", nextMinor.toString());
            
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("0.0.1", nextPatch.toString());
        }

        @Test
        @DisplayName("Should handle large versions in increment methods")
        void shouldHandleLargeVersionsInIncrementMethods() {
            SemVer version = new SemVer("999999.888888.777777");
            
            SemVer nextMajor = SemVerUtils.nextMajor(version);
            assertEquals("1000000.0.0", nextMajor.toString());
            
            SemVer nextMinor = SemVerUtils.nextMinor(version);
            assertEquals("999999.888889.0", nextMinor.toString());
            
            SemVer nextPatch = SemVerUtils.nextPatch(version);
            assertEquals("999999.888888.777778", nextPatch.toString());
        }
    }

    @Nested
    @DisplayName("With Method Tests")
    class WithMethodTests {

        @Test
        @DisplayName("Should set major version correctly")
        void shouldSetMajorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withMajor(version, 5);
            
            assertEquals("5.0.0", newVersion.toString());
            assertEquals(5, newVersion.getMajor());
            assertEquals(0, newVersion.getMinor());
            assertEquals(0, newVersion.getPatch());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set minor version correctly")
        void shouldSetMinorVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withMinor(version, 5);
            
            assertEquals("1.5.0", newVersion.toString());
            assertEquals(1, newVersion.getMajor());
            assertEquals(5, newVersion.getMinor());
            assertEquals(0, newVersion.getPatch());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should set patch version correctly")
        void shouldSetPatchVersionCorrectly() {
            SemVer version = new SemVer("1.2.3");
            SemVer newVersion = SemVerUtils.withPatch(version, 5);
            
            assertEquals("1.2.5", newVersion.toString());
            assertEquals(1, newVersion.getMajor());
            assertEquals(2, newVersion.getMinor());
            assertEquals(5, newVersion.getPatch());
            assertFalse(newVersion.isPrerelease());
            assertFalse(newVersion.hasBuild());
        }

        @Test
        @DisplayName("Should handle zero values in with methods")
        void shouldHandleZeroValuesInWithMethods() {
            SemVer version = new SemVer("5.5.5");
            
            SemVer withZeroMajor = SemVerUtils.withMajor(version, 0);
            assertEquals("0.0.0", withZeroMajor.toString());
            
            SemVer withZeroMinor = SemVerUtils.withMinor(version, 0);
            assertEquals("5.0.0", withZeroMinor.toString());
            
            SemVer withZeroPatch = SemVerUtils.withPatch(version, 0);
            assertEquals("5.5.0", withZeroPatch.toString());
        }

        @Test
        @DisplayName("Should handle maximum values in with methods")
        void shouldHandleMaximumValuesInWithMethods() {
            SemVer version = new SemVer("1.2.3");
            
            SemVer withMaxMajor = SemVerUtils.withMajor(version, Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE + ".0.0", withMaxMajor.toString());
            
            SemVer withMaxMinor = SemVerUtils.withMinor(version, Integer.MAX_VALUE);
            assertEquals("1." + Integer.MAX_VALUE + ".0", withMaxMinor.toString());
            
            SemVer withMaxPatch = SemVerUtils.withPatch(version, Integer.MAX_VALUE);
            assertEquals("1.2." + Integer.MAX_VALUE, withMaxPatch.toString());
        }

        @Test
        @DisplayName("Should throw exception for negative major version")
        void shouldThrowExceptionForNegativeMajorVersion() {
            SemVer version = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withMajor(version, -1)
            );
            assertEquals("Major version cannot be negative: -1", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative minor version")
        void shouldThrowExceptionForNegativeMinorVersion() {
            SemVer version = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withMinor(version, -1)
            );
            assertEquals("Minor version cannot be negative: -1", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative patch version")
        void shouldThrowExceptionForNegativePatchVersion() {
            SemVer version = new SemVer("1.2.3");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SemVerUtils.withPatch(version, -1)
            );
            assertEquals("Patch version cannot be negative: -1", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Comparison Method Tests")
    class ComparisonMethodTests {

        @Test
        @DisplayName("Should compare versions correctly using compareTo")
        void shouldCompareVersionsCorrectlyUsingCompareTo() {
            SemVer version1 = new SemVer("1.0.0");
            SemVer version2 = new SemVer("2.0.0");
            
            assertTrue(version1.compareTo(version2) < 0);
            assertTrue(version2.compareTo(version1) > 0);
            assertEquals(0, version1.compareTo(version1));
        }

        @Test
        @DisplayName("Should handle prerelease comparison in compareTo")
        void shouldHandlePrereleaseComparisonInCompareTo() {
            SemVer releaseVersion = new SemVer("1.0.0");
            SemVer prereleaseVersion = new SemVer("1.0.0-alpha");
            
            assertTrue(releaseVersion.compareTo(prereleaseVersion) > 0);
            assertTrue(prereleaseVersion.compareTo(releaseVersion) < 0);
        }

        @Test
        @DisplayName("Should ignore build metadata in compareTo")
        void shouldIgnoreBuildMetadataInCompareTo() {
            SemVer version1 = new SemVer("1.0.0+build1");
            SemVer version2 = new SemVer("1.0.0+build2");
            
            assertEquals(0, version1.compareTo(version2));
        }

        @Test
        @DisplayName("Should throw exception when comparing with null")
        void shouldThrowExceptionWhenComparingWithNull() {
            SemVer version = new SemVer("1.0.0");
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> version.compareTo(null)
            );
            assertEquals("Cannot compare with null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should return original version string in toString")
        void shouldReturnOriginalVersionStringInToString() {
            String originalVersion = "1.2.3-alpha.1+build.123";
            SemVer version = new SemVer(originalVersion);
            assertEquals(originalVersion, version.toString());
        }

        @Test
        @DisplayName("Should preserve original formatting in toString")
        void shouldPreserveOriginalFormattingInToString() {
            String originalVersion = "  1.2.3  ";
            SemVer version = new SemVer(originalVersion);
            assertEquals("1.2.3", version.toString()); // Should be trimmed
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should be equal for identical versions")
        void shouldBeEqualForIdenticalVersions() {
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
        @DisplayName("Should not be equal for different major versions")
        void shouldNotBeEqualForDifferentMajorVersions() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("2.2.3");
            
            assertNotEquals(version1, version2);
        }

        @Test
        @DisplayName("Should not be equal for different minor versions")
        void shouldNotBeEqualForDifferentMinorVersions() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("1.3.3");
            
            assertNotEquals(version1, version2);
        }

        @Test
        @DisplayName("Should not be equal for different patch versions")
        void shouldNotBeEqualForDifferentPatchVersions() {
            SemVer version1 = new SemVer("1.2.3");
            SemVer version2 = new SemVer("1.2.4");
            
            assertNotEquals(version1, version2);
        }

        @Test
        @DisplayName("Should not be equal for different prerelease versions")
        void shouldNotBeEqualForDifferentPrereleaseVersions() {
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

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            SemVer version = new SemVer("1.2.3");
            assertEquals(version, version);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle maximum integer values")
        void shouldHandleMaximumIntegerValues() {
            String maxVersion = Integer.MAX_VALUE + "." + Integer.MAX_VALUE + "." + Integer.MAX_VALUE;
            SemVer version = new SemVer(maxVersion);
            
            assertEquals(Integer.MAX_VALUE, version.getMajor());
            assertEquals(Integer.MAX_VALUE, version.getMinor());
            assertEquals(Integer.MAX_VALUE, version.getPatch());
            assertEquals(maxVersion, version.toString());
        }

        @Test
        @DisplayName("Should handle very long prerelease identifiers")
        void shouldHandleVeryLongPrereleaseIdentifiers() {
            String longPrerelease = "alpha.beta.gamma.delta.epsilon.zeta.eta.theta.iota.kappa.lambda.mu.nu.xi.omicron.pi";
            SemVer version = new SemVer("1.0.0-" + longPrerelease);
            
            assertEquals(longPrerelease, version.getPrerelease());
            assertTrue(version.isPrerelease());
        }

        @Test
        @DisplayName("Should handle very long build metadata")
        void shouldHandleVeryLongBuildMetadata() {
            String longBuild = "build.1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.17.18.19.20";
            SemVer version = new SemVer("1.0.0+" + longBuild);
            
            assertEquals(longBuild, version.getBuild());
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should handle mixed alphanumeric identifiers")
        void shouldHandleMixedAlphanumericIdentifiers() {
            SemVer version = new SemVer("1.0.0-alpha1.beta2.gamma3+build1.build2.build3");
            
            assertEquals("alpha1.beta2.gamma3", version.getPrerelease());
            assertEquals("build1.build2.build3", version.getBuild());
            assertTrue(version.isPrerelease());
            assertTrue(version.hasBuild());
        }

        @Test
        @DisplayName("Should handle special characters in identifiers")
        void shouldHandleSpecialCharactersInIdentifiers() {
            SemVer version = new SemVer("1.0.0-alpha-1.beta-2+build-123");
            
            assertEquals("alpha-1.beta-2", version.getPrerelease());
            assertEquals("build-123", version.getBuild());
            assertTrue(version.isPrerelease());
            assertTrue(version.hasBuild());
        }
    }
}
