# Contributing to TesseractLib

First off, thank you for considering contributing to TesseractLib! It's people like you that make TesseractLib such a great tool for temporal and spatial data modeling.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Workflow](#development-workflow)
- [Code Style Guidelines](#code-style-guidelines)
- [Testing](#testing)
- [Documentation](#documentation)
- [Pull Request Process](#pull-request-process)
- [Community](#community)

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 11 or higher** - TesseractLib is built with Java 11
- **Maven 3.6+** - For building and dependency management
- **Git** - For version control
- **IDE** (recommended):
  - IntelliJ IDEA (recommended for Lombok support)
  - Eclipse with Lombok plugin
  - VS Code with Java extensions

### Fork and Clone

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/TesseractLib.git
   cd TesseractLib
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/fieldcode/TesseractLib.git
   ```

## Development Setup

### Initial Build

Build the project to ensure everything is set up correctly:

```bash
mvn clean install
```

This will:
- Compile all modules
- Run all tests
- Install artifacts to your local Maven repository

### IDE Setup

#### IntelliJ IDEA

1. Open the project using `File > Open` and select the root `pom.xml`
2. Enable annotation processing:
   - Go to `Settings > Build, Execution, Deployment > Compiler > Annotation Processors`
   - Check "Enable annotation processing"
3. Install Lombok plugin:
   - Go to `Settings > Plugins`
   - Search for "Lombok" and install
4. Restart IntelliJ IDEA

#### Eclipse

1. Import as Maven project: `File > Import > Existing Maven Projects`
2. Install Lombok:
   - Download `lombok.jar` from [projectlombok.org](https://projectlombok.org/)
   - Run `java -jar lombok.jar` and point to your Eclipse installation
3. Restart Eclipse

### Project Structure

```
TesseractLib/
├── tesseract-api/          # Core interfaces and contracts
├── tesseract-core/         # Core implementations
├── tesseract-jackson-datatype/  # Jackson serialization support
└── docs/                   # Documentation
```

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates.

When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the issue
- **Expected behavior** vs. actual behavior
- **Code sample** or test case demonstrating the issue
- **Environment details** (Java version, OS, etc.)
- **Stack traces** if applicable

Use the bug report template when available.

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide detailed description** of the proposed functionality
- **Explain why this enhancement would be useful** to TesseractLib users
- **Provide examples** of how the feature would be used
- **Consider alternative approaches** if applicable

### Your First Code Contribution

Unsure where to begin? Look for issues labeled:

- `good first issue` - Suitable for newcomers
- `help wanted` - Extra attention needed
- `documentation` - Documentation improvements

## Development Workflow

### 1. Create a Feature Branch

Always create a new branch for your work:

```bash
git checkout -b feature/my-new-feature
# or
git checkout -b fix/issue-123
```

Branch naming conventions:
- `feature/` - New features
- `fix/` - Bug fixes
- `docs/` - Documentation changes
- `refactor/` - Code refactoring
- `test/` - Test improvements

### 2. Make Your Changes

- Write clean, maintainable code following our style guidelines
- Add tests for new functionality
- Update documentation as needed
- Keep commits logical and atomic

### 3. Commit Your Changes

Write clear, descriptive commit messages:

```bash
git add .
git commit -m "Add support for interval splitting in scheduler

- Implement SplittableIntervalScheduler
- Add tests for split interval scenarios
- Update documentation with examples

Closes #123"
```

Commit message guidelines:
- Use present tense ("Add feature" not "Added feature")
- Use imperative mood ("Move cursor to..." not "Moves cursor to...")
- First line should be 50 characters or less
- Reference issues and pull requests when applicable

### 4. Keep Your Branch Updated

Regularly sync with upstream:

```bash
git fetch upstream
git rebase upstream/main
```

### 5. Push to Your Fork

```bash
git push origin feature/my-new-feature
```

## Code Style Guidelines

### Java Code Style

TesseractLib follows standard Java conventions with some specific guidelines:

#### General Principles

- **Immutability**: Prefer immutable objects using Immutables or Lombok's `@Value`
- **Null Safety**: Use `Optional` instead of returning null
- **Functional Style**: Use streams and functional interfaces where appropriate
- **Type Safety**: Leverage generics for type safety

#### Code Formatting

- **Indentation**: 2 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Braces**: Always use braces for if/else/for/while blocks
- **Imports**: No wildcard imports, organize alphabetically

Example:

```java
public class MyClass {
  
  private final String name;
  private final int value;
  
  public Optional<String> processData(List<String> items) {
    return items.stream()
        .filter(item -> item.length() > 5)
        .findFirst();
  }
}
```

#### Naming Conventions

- **Classes**: `PascalCase` (e.g., `IntervalScheduler`)
- **Methods**: `camelCase` (e.g., `findAvailableSlot`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `MAX_RETRY_COUNT`)
- **Variables**: `camelCase` (e.g., `intervalCollection`)
- **Packages**: lowercase (e.g., `com.fieldcode.tesseract`)

#### Use of Annotations

- **Lombok**: Use `@Value`, `@Builder`, `@Getter`, `@Slf4j` where appropriate
- **Immutables**: Use `@Value.Immutable` for complex immutable data structures
- **Null Annotations**: Use `@NonNull` where applicable

### JavaDoc

All public APIs must have JavaDoc:

```java
/**
 * Schedules a task within the given interval considering floating breaks.
 * <p>
 * This method attempts to find a suitable time slot for the task while
 * avoiding conflicts with the specified breaks.
 *
 * @param task the task to schedule
 * @param interval the time window for scheduling
 * @param breaks the floating breaks to consider
 * @return the scheduling result, or empty if scheduling failed
 */
public Optional<IntervalSchedulerResult> schedule(
    Task task, 
    Interval interval, 
    List<FloatingBreak> breaks) {
  // implementation
}
```

JavaDoc requirements:
- Describe what the method does
- Document all parameters with `@param`
- Document return value with `@return`
- Document exceptions with `@throws`
- Include usage examples for complex APIs

## Testing

### Writing Tests

TesseractLib uses **JUnit 5** with **AssertJ** for assertions. Mockito is available but should be used sparingly — reach for it only when a collaborator genuinely needs mocking; prefer real, shared test fixtures over hand-rolled mocks when a module already provides them.

- **Always assert with AssertJ** (`assertThat(...)`) — never JUnit's `assertEquals`/`assertTrue`.
- **Always format chained AssertJ calls across multiple lines** — `assertThat(x)` on its own line, each chained method (`.isEqualTo(...)`, `.isSameAs(...)`, etc.) on the following line(s) — even a single trailing call, regardless of how short the chain is.

#### Test Structure

Follow the AAA (Arrange-Act-Assert) pattern, with each part marked by a comment:

- **Arrange** — set up the inputs, collaborators, and preconditions the test needs; state in the comment what's being set up
- **Act** — invoke the single method or behavior under test; state in the comment what's being executed
- **Assert** — verify the outcome matches what's expected; state in the comment what's being checked

```java
@Test
@DisplayName("should successfully schedule task with floating breaks")
void schedule_WithFloatingBreaks_Success() {
  // Arrange: a 2-hour task, a 9-17 interval, and a lunch break from 12 to 13
  var task = createTask(Duration.ofHours(2));
  var interval = Intervals.interval(9, 17);
  var breaks = List.of(createBreak(12, 13));

  // Act: schedule the task within the interval, around the break
  var result = scheduler.schedule(task, interval, breaks);

  // Assert: scheduling succeeds
  assertThat(result)
      .isPresent();
  assertThat(result.get().isSuccessful())
      .isTrue();
}
```

#### Test Guidelines

- **One assertion per test** (when possible)
- **Always annotate the test class and every test method with `@DisplayName`**, phrasing method-level names in "should..." style (e.g. `"should recompute after a new best solution"`)
- **Test edge cases** and error conditions
- **Avoid test interdependencies**

#### Test Naming Convention

Method name: `methodName_condition_expectedBehavior`

Example: `schedule_WithOverlappingBreaks_ThrowsException`

#### Running Tests

```bash
# Root build (all modules, includes running every test)
mvn clean install

# Run tests for a single module (module artifactIds match their directory names)
mvn -pl tesseract-core -am test

# Run a single test class
mvn -pl tesseract-core -am test -Dtest=IntervalSchedulerTest
```

### Test Coverage

- The root `coverage` Maven profile (JaCoCo `prepare-agent`) is **active by default**, not opt-in — every build above is already instrumented, no extra flag needed
- The aggregate report lands under an aggregator module's `target/site/jacoco-aggregate/jacoco.xml` (feeds Sonar); if unsure which module aggregates, run `find . -path '*/site/jacoco-aggregate/jacoco.xml'` after a build
- Aim for **80%+ code coverage** for new code
- All public APIs must have tests
- Critical business logic should have extensive tests

## Documentation

### Code Documentation

- **JavaDoc** for all public APIs
- **Inline comments** for complex logic
- **Package-info.java** for package-level documentation

### Markdown Documentation

Update relevant documentation in the `docs/` folder:

- `temporal-primitives.md` - Temporal data structures
- `spatial-primitives.md` - Spatial data structures
- `interval-collection.md` - Interval collections
- `presence-collection.md` - Presence collections
- `helper-notation.md` - Test helper utilities

### Updating README

If your changes affect usage, update `README.md` with:
- New features
- API changes
- Installation instructions
- Usage examples

## Pull Request Process

### Before Submitting

Checklist before creating a PR:

- [ ] Code follows the style guidelines
- [ ] All tests pass locally (`mvn test`)
- [ ] New tests added for new functionality
- [ ] JavaDoc added/updated for public APIs
- [ ] Documentation updated (if applicable)
- [ ] CHANGELOG.md updated (for significant changes)
- [ ] Commits are clean and well-described
- [ ] Branch is up to date with main

### Submitting the PR

1. **Push your branch** to your fork
2. **Create Pull Request** on GitHub
3. **Fill out the PR template** completely
4. **Link related issues** (e.g., "Closes #123")

### PR Title Format

Use conventional commit format:

```
feat: Add interval splitting support
fix: Correct timezone handling in Moments
docs: Update scheduling examples
test: Add tests for DirectionMatrix
refactor: Simplify IntervalCollection implementation
```

### PR Description

Include:
- **What**: What changes does this PR introduce?
- **Why**: Why are these changes needed?
- **How**: How did you implement the changes?
- **Testing**: How was this tested?
- **Screenshots**: If UI-related (not applicable for this library)

### Review Process

1. **Automated checks** must pass (CI build, tests)
2. **Code review** by at least one maintainer
3. **Address feedback** by pushing additional commits
4. **Approval** from maintainer(s)
5. **Merge** (maintainers will merge approved PRs)

### After Your PR is Merged

- **Delete your branch** (GitHub will prompt you)
- **Update your local repository**:
  ```bash
  git checkout main
  git pull upstream main
  git push origin main
  ```
- **Celebrate!** 🎉 You've contributed to TesseractLib!

## Community

### Getting Help

- **GitHub Issues**: For bug reports and feature requests
- **Discussions**: For questions and general discussions
- **Documentation**: Check the `docs/` folder

### Staying Updated

- **Watch** the repository for notifications
- **Star** the repository to show support
- **Follow** releases for new versions

### Recognition

Contributors are recognized in:
- GitHub Contributors page
- Release notes
- CHANGELOG.md (for significant contributions)

## Questions?

Don't hesitate to ask questions! There are no stupid questions. Everyone was a beginner at some point.

Open an issue or discussion if you need clarification on any aspect of contributing.

---

Thank you for contributing to TesseractLib! Your efforts help make temporal and spatial data modeling easier for everyone.

