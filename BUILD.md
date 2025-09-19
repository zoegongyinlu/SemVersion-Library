# Build Documentation

This document provides comprehensive instructions for building, testing, and generating reports for the SemVer Java Library.

## Prerequisites

- **Java**: JDK 21 or higher
- **Maven**: 3.6 or higher
- **Git**: For version control

## Project Structure

```
implement-semver-2-0-library-zoegongyinlu/
├── pom.xml
├── checkstyle.xml
├── src/
│   ├── main/java/com/neu/semver/
│   └── test/java/com/neu/semver/
├── target/
├── README.md
├── BUILD.md
└── QUICKSTART.md
```

## Build Commands

### Basic Build

```bash
# Clean and compile
mvn clean compile

# Clean, compile, and run tests
mvn clean test

# Clean, compile, test, and package
mvn clean package
```

### Complete Build with All Reports

```bash
# Generate all reports and documentation
mvn clean verify site
```

This command will:
- Clean previous builds
- Compile source code
- Run all tests
- Generate test coverage reports
- Run static analysis tools
- Generate Javadoc documentation
- Create the complete Maven site

## Test Coverage

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report

# Run tests and check coverage thresholds
mvn clean test jacoco:check
```

### Coverage Reports

Coverage reports are generated in `target/site/jacoco/` and include:

- **Line Coverage**: Percentage of lines executed
- **Branch Coverage**: Percentage of branches taken
- **Method Coverage**: Percentage of methods called
- **Class Coverage**: Percentage of classes used

**Coverage Requirements**: The project maintains >70% line and branch coverage.

### Viewing Coverage Reports

```bash
# Generate and open coverage report
mvn clean test jacoco:report
open target/site/jacoco/index.html  # macOS
xdg-open target/site/jacoco/index.html  # Linux
```

## Code Documentation (Javadoc)

### Generate Javadoc

```bash
# Generate Javadoc documentation
mvn javadoc:javadoc

# Generate Javadoc JAR
mvn javadoc:jar
```

### Javadoc Output

- **HTML Documentation**: `target/site/apidocs/`
- **Javadoc JAR**: `target/semver-java-0.1.0-javadoc.jar`

### Viewing Javadoc

```bash
# Generate and open Javadoc
mvn javadoc:javadoc
open target/site/apidocs/index.html  # macOS
xdg-open target/site/apidocs/index.html  # Linux
```

## Static Analysis Reports

### Checkstyle (Code Style)

```bash
# Run Checkstyle analysis
mvn checkstyle:checkstyle

# Check code style during build
mvn validate
```

**Output**: `target/site/checkstyle.html`

**Configuration**: Uses `checkstyle.xml` with Sun Java Code Conventions.

### PMD (Code Quality)

```bash
# Run PMD analysis
mvn pmd:pmd

# Run PMD during build
mvn verify
```

**Output**: `target/site/pmd.html`

**Rulesets**:
- Best Practices
- Code Style
- Error Prone
- Performance
- Security

### SpotBugs (Bug Detection)

```bash
# Run SpotBugs analysis
mvn spotbugs:spotbugs

# Run SpotBugs during build
mvn verify
```

**Output**: `target/site/spotbugs.html`

**Configuration**: Maximum effort, low threshold for comprehensive analysis.

## JAR Package Generation

### Generate JAR Files

```bash
# Generate all JAR files
mvn clean package
```

This creates:
- `target/semver-java-0.1.0.jar` - Main JAR file
- `target/semver-java-0.1.0-sources.jar` - Source code JAR
- `target/semver-java-0.1.0-javadoc.jar` - Javadoc JAR

### JAR Contents

#### Main JAR (`semver-java-0.1.0.jar`)
- Compiled class files
- No dependencies (standalone library)

#### Sources JAR (`semver-java-0.1.0-sources.jar`)
- Complete source code
- Useful for IDEs and debugging

#### Javadoc JAR (`semver-java-0.1.0-javadoc.jar`)
- Complete API documentation
- Can be attached to IDEs

## Maven Site Generation

### Generate Complete Site

```bash
# Generate Maven site with all reports
mvn site

# Generate site and open in browser
mvn site
open target/site/index.html  # macOS
xdg-open target/site/index.html  # Linux
```

### Site Contents

The Maven site includes:
- Project information
- Test reports
- Coverage reports
- Static analysis reports
- Javadoc documentation
- Dependencies information

## Continuous Integration

### GitHub Actions (Recommended)

Create `.github/workflows/ci.yml`:

```yaml
name: CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    - name: Run tests with coverage
      run: mvn clean test jacoco:report
    - name: Generate site
      run: mvn site
    - name: Upload coverage reports
      uses: codecov/codecov-action@v3
```

### Local CI Simulation

```bash
# Simulate CI pipeline
mvn clean verify site
```

## Troubleshooting

### Common Issues

#### Java Version Mismatch
```bash
# Check Java version
java -version
javac -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/jdk21
```

#### Maven Version Issues
```bash
# Check Maven version
mvn -version

# Update Maven if needed
# (Use package manager or download from Apache Maven)
```

#### Memory Issues
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512m"
```

### Build Failures

#### Test Failures
```bash
# Run specific test class
mvn test -Dtest=SemVerTest

# Run tests with debug output
mvn test -X
```

#### Coverage Threshold Failures
```bash
# Check current coverage
mvn clean test jacoco:report
# View target/site/jacoco/index.html

# Temporarily disable coverage check
mvn clean test -Djacoco.skip=true
```

#### Static Analysis Failures
```bash
# Skip static analysis temporarily
mvn clean package -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

## Development Workflow

### Recommended Development Process

1. **Make Changes**
   ```bash
   # Edit source code
   vim src/main/java/com/neu/semver/SemVer.java
   ```

2. **Run Tests**
   ```bash
   mvn test
   ```

3. **Check Code Style**
   ```bash
   mvn checkstyle:checkstyle
   ```

4. **Run Static Analysis**
   ```bash
   mvn pmd:pmd spotbugs:spotbugs
   ```

5. **Generate Documentation**
   ```bash
   mvn javadoc:javadoc
   ```

6. **Full Build**
   ```bash
   mvn clean verify site
   ```

### Pre-commit Checklist

- [ ] All tests pass
- [ ] Code coverage >70%
- [ ] No Checkstyle violations
- [ ] No PMD violations
- [ ] No SpotBugs issues
- [ ] Javadoc is complete
- [ ] JAR builds successfully

## Performance

### Build Time Optimization

```bash
# Skip tests for faster builds (development only)
mvn clean package -DskipTests
```

### Memory Optimization

```bash
# Use Maven daemon
mvn clean package
```

## Release Process

### Preparing for Release

1. **Update Version**
   ```bash
   # Update version in pom.xml
   mvn versions:set -DnewVersion=0.2.0
   ```

2. **Run Full Build**
   ```bash
   mvn clean verify site
   ```

3. **Generate Release JARs**
   ```bash
   mvn clean package source:jar javadoc:jar
   ```

### Publishing to Maven Central
```bash
# Sign and deploy to Maven Central
mvn clean deploy -P release 
```
**Note**: Requires GPG signing and Sonatype credentials configured.
