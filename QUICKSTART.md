# Quick Start Guide

This guide provides quick commands for common development tasks.

## Essential Commands

### Build and Test
```bash
# Quick build and test
mvn clean test

# Full build with all reports
mvn clean verify site

# Generate JAR package
mvn clean package
```

### Documentation
```bash
# Generate Javadoc
mvn javadoc:javadoc

# View Javadoc (macOS)
open target/site/apidocs/index.html

# View Javadoc (Linux)
xdg-open target/site/apidocs/index.html
```

### Code Quality
```bash
# Run all static analysis
mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs

# Check code style only
mvn checkstyle:checkstyle

# View Checkstyle report (macOS)
open target/site/checkstyle.html
```

### Test Coverage
```bash
# Generate coverage report
mvn clean test jacoco:report

# View coverage report (macOS)
open target/site/jacoco/index.html

# Check coverage thresholds
mvn clean test jacoco:check
```

## Development Workflow

### 1. Make Changes
```bash
# Edit source files
vim src/main/java/com/neu/semver/SemVer.java
```

### 2. Test Changes
```bash
# Run tests
mvn test

# Run specific test
mvn test -Dtest=SemVerTest
```

### 3. Check Quality
```bash
# Quick quality check
mvn checkstyle:checkstyle
```

### 4. Generate Documentation
```bash
# Update Javadoc
mvn javadoc:javadoc
```

### 5. Full Build
```bash
# Complete build with all reports
mvn clean verify site
```

## Troubleshooting

### Common Issues

#### Build Fails
```bash
# Check Java version
java -version  # Should be 21+

# Clean and retry
mvn clean compile
```

#### Tests Fail
```bash
# Run with debug output
mvn test -X

# Run specific test class
mvn test -Dtest=SemVerTest
```

#### Coverage Too Low
```bash
# Check current coverage
mvn clean test jacoco:report
# View target/site/jacoco/index.html
```

#### Static Analysis Issues
```bash
# Skip analysis temporarily
mvn clean package -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

## File Locations

### Generated Files
- **JAR**: `target/semver-java-0.1.0.jar`
- **Javadoc**: `target/site/apidocs/`
- **Coverage**: `target/site/jacoco/`
- **Checkstyle**: `target/site/checkstyle.html`
- **PMD**: `target/site/pmd.html`
- **SpotBugs**: `target/site/spotbugs.html`

### Configuration Files
- **Maven**: `pom.xml`
- **Checkstyle**: `checkstyle.xml`
- **Source**: `src/main/java/`
- **Tests**: `src/test/java/`

## Quick Reference

| Task | Command |
|------|---------|
| Build | `mvn clean compile` |
| Test | `mvn test` |
| Package | `mvn clean package` |
| Javadoc | `mvn javadoc:javadoc` |
| Coverage | `mvn clean test jacoco:report` |
| Checkstyle | `mvn checkstyle:checkstyle` |
| PMD | `mvn pmd:pmd` |
| SpotBugs | `mvn spotbugs:spotbugs` |
| Full Build | `mvn clean verify site` |
| Skip Tests | `mvn package -DskipTests` |
| Skip Analysis | `mvn package -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true` |

## IDE Integration

### IntelliJ IDEA
1. Import as Maven project
2. Enable annotation processing
3. Configure code style to use Checkstyle rules
4. Install plugins: Checkstyle-IDEA, SpotBugs

### Eclipse
1. Import as Maven project
2. Install Checkstyle plugin
3. Configure PMD plugin
4. Install SpotBugs plugin

### VS Code
1. Install Java Extension Pack
2. Install Checkstyle extension
3. Configure Maven integration
4. Install PMD extension
