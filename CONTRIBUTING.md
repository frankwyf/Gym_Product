# Contributing

Thank you for contributing to Gym Product.

## Development Setup

1. Install prerequisites: JDK 17, Maven 3.8+, Node.js 20, MySQL, Redis.
2. Build repository:

```powershell
mvn -f pom.xml -DskipTests compile
```

3. Run validations before commit:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1
```

## Commit Style

- Use small, focused commits.
- Prefix examples: `fix:`, `feat:`, `refactor:`, `docs:`, `ci:`.

## Pull Requests

- Describe the problem and the solution clearly.
- Include test/build evidence.
- Keep unrelated changes out of the same PR.

## Code Quality

- Prefer fixing warnings in small batches.
- Do not break existing runtime behavior for style-only changes.
