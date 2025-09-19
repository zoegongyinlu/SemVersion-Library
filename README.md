# SemVer Java Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.zoegongyinlu/semver-java.svg)](https://search.maven.org/artifact/io.github.zoegongyinlu/semver-java)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.java.net/)

A comprehensive Java library for validating, creating, and manipulating semantic versions according to the [SemVer 2.0 specification](https://semver.org/).

## Features

- ✅ Full SemVer 2.0 compliance
- ✅ Immutable SemVer objects
- ✅ Comprehensive validation
- ✅ Version comparison and sorting
- ✅ Prerelease and build metadata support
- ✅ Extensive test coverage (>70%)
- ✅ Static analysis and code quality tools
- ✅ Complete Javadoc documentation

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.zoegongyinlu</groupId>
    <artifactId>semver-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```gradle
implementation 'io.github.zoegongyinlu:semver-java:0.1.0'
```

### Maven Central

This library is published to Maven Central. You can find it at:
- **Maven Central**: https://search.maven.org/artifact/io.github.zoegongyinlu/semver-java
- **Direct Link**: https://repo1.maven.org/maven2/io/github/zoegongyinlu/semver-java/

## Quick Start

```java
import com.neu.semver.SemVer;
import com.neu.semver.SemVerParser;
import com.neu.semver.SemVerComparator;

// Parse a semantic version
SemVer version = SemVerParser.parse("1.2.3-alpha.1+build.123");

// Access version components
System.out.println("Major: " + version.getMajor());        // 1
System.out.println("Minor: " + version.getMinor());        // 2
System.out.println("Patch: " + version.getPatch());        // 3
System.out.println("Prerelease: " + version.getPrerelease()); // "alpha.1"
System.out.println("Build: " + version.getBuild());        // "build.123"

// Compare versions
SemVer v1 = SemVerParser.parse("1.2.3");
SemVer v2 = SemVerParser.parse("1.2.4");
System.out.println(v1.compareTo(v2)); // -1 (v1 < v2)

// Check if version is stable (no prerelease)
System.out.println(v1.isStable()); // true
System.out.println(SemVerParser.parse("1.0.0-alpha").isStable()); // false
```

## API Reference

### SemVer Class

The main class representing a semantic version.

#### Constructors
- `SemVer(int major, int minor, int patch)` - Creates a stable version
- `SemVer(int major, int minor, int patch, String prerelease)` - Creates a prerelease version
- `SemVer(int major, int minor, int patch, String prerelease, String build)` - Creates a version with build metadata

#### Key Methods
- `getMajor()`, `getMinor()`, `getPatch()` - Get version numbers
- `getPrerelease()`, `getBuild()` - Get prerelease/build metadata
- `isStable()` - Check if version is stable (no prerelease)
- `compareTo(SemVer other)` - Compare versions
- `toString()` - Get string representation

### SemVerParser Class

Utility class for parsing version strings.

#### Methods
- `parse(String version)` - Parse a version string
- `isValid(String version)` - Check if string is valid SemVer
- `tryParse(String version)` - Parse with null return on failure

### SemVerComparator Class

Advanced comparison utilities.

#### Methods
- `compare(SemVer v1, SemVer v2)` - Compare two versions
- `isCompatible(SemVer v1, SemVer v2)` - Check compatibility
- `satisfies(String range, SemVer version)` - Check if version satisfies range

### SemVerUtils Class

Additional utility methods.

#### Methods
- `sort(List<SemVer> versions)` - Sort versions
- `getLatest(List<SemVer> versions)` - Get latest version
- `getStableVersions(List<SemVer> versions)` - Filter stable versions

## Building from Source

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Checkout and Build

```bash
# Clone the repository
git clone https://github.com/zoegongyinlu/semver-java.git
cd semver-java/semVerLibrary

# Build the project
mvn clean compile

# Run tests
mvn test

# Generate JAR package
mvn package
```

## Development Commands

### Generate Code Documentation (Javadoc)

```bash
mvn javadoc:javadoc
```

The generated documentation will be available in `target/site/apidocs/`.

### Run All Tests

```bash
mvn test
```

### Generate Test Coverage Reports

```bash
mvn clean test jacoco:report
```

Coverage reports will be generated in `target/site/jacoco/`. The project maintains >70% line and branch coverage.

### Generate Coding Style Reports

```bash
mvn checkstyle:checkstyle
```

Checkstyle reports will be generated in `target/site/checkstyle.html`.

### Generate Static Analysis Reports

#### PMD Analysis
```bash
mvn pmd:pmd
```

#### SpotBugs Analysis
```bash
mvn spotbugs:spotbugs
```

Reports will be generated in `target/site/` directory.

### Generate JAR Package

```bash
mvn clean package
```

This creates:
- `target/semver-java-0.1.0.jar` - Main JAR
- `target/semver-java-0.1.0-sources.jar` - Source JAR
- `target/semver-java-0.1.0-javadoc.jar` - Javadoc JAR

### Complete Build with All Reports

```bash
mvn clean verify site
```

This command runs all tests, generates all reports, and creates the complete documentation site.

## Code Quality

This project maintains high code quality standards:

- **Test Coverage**: >70% line and branch coverage
- **Static Analysis**: PMD, SpotBugs, and Checkstyle
- **Code Style**: Sun Java Code Conventions
- **Documentation**: Complete Javadoc coverage

## Examples

### Version Comparison

```java
SemVer v1 = SemVerParser.parse("1.2.3");
SemVer v2 = SemVerParser.parse("1.2.4");
SemVer v3 = SemVerParser.parse("2.0.0");

// Basic comparison
System.out.println(v1.compareTo(v2)); // -1
System.out.println(v2.compareTo(v1)); // 1
System.out.println(v1.compareTo(v1)); // 0

// Prerelease comparison
SemVer alpha = SemVerParser.parse("1.0.0-alpha");
SemVer beta = SemVerParser.parse("1.0.0-beta");
System.out.println(alpha.compareTo(beta)); // -1 (alpha < beta)
```

### Version Sorting

```java
List<SemVer> versions = Arrays.asList(
    SemVerParser.parse("1.0.0"),
    SemVerParser.parse("1.2.0"),
    SemVerParser.parse("1.1.0"),
    SemVerParser.parse("2.0.0-alpha")
);

List<SemVer> sorted = SemVerUtils.sort(versions);
// Result: [1.0.0, 1.1.0, 1.2.0, 2.0.0-alpha]
```

### Validation

```java
// Valid versions
System.out.println(SemVerParser.isValid("1.0.0"));           // true
System.out.println(SemVerParser.isValid("1.0.0-alpha.1"));   // true
System.out.println(SemVerParser.isValid("1.0.0+build.123")); // true

// Invalid versions
System.out.println(SemVerParser.isValid("1.0"));             // false
System.out.println(SemVerParser.isValid("1.0.0.0"));         // false
System.out.println(SemVerParser.isValid("v1.0.0"));          // false
```

## Error Handling

The library throws `InvalidSemVerException` for invalid version strings:

```java
try {
    SemVer version = SemVerParser.parse("invalid-version");
} catch (InvalidSemVerException e) {
    System.err.println("Invalid version: " + e.getMessage());
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Yinlu Gong

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Author

**Yinlu Gong**
- Email: gong.yinl@northeastern.edu
- GitHub: [@zoegongyinlu](https://github.com/zoegongyinlu)

## Links

- **Maven Central Package**: https://search.maven.org/artifact/io.github.zoegongyinlu/semver-java
- **GitHub Repository**: https://github.com/CS7580-SEA-2025/implement-semver-2-0-library-zoegongyinlu?tab=readme-ov-file
- **SemVer Specification**: https://semver.org/