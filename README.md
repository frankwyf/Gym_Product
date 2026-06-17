# Gym Product

Open-source multi-client gym management platform with two Java backends, one Vue admin panel, one React Native mobile app, and one WeChat mini-program.

## Documentation

- English (this file)
- Chinese: [docs/README.zh-CN.md](docs/README.zh-CN.md)
- Japanese: [docs/README.ja.md](docs/README.ja.md)

## Project Structure

- `gymMaster`: Spring Boot business backend
- `gym-management-system-master`: Spring Boot admin backend
- `gym-ui`: Vue 2 admin frontend
- `gym-mobile-app`: React Native mobile client
- `GymMaster_wx`: WeChat mini-program client
- `scripts`: local setup and verification scripts

## Quick Start (Windows)

### Prerequisites

- JDK 17
- Maven 3.8+
- Node.js 20
- MySQL and Redis (required for runtime)

### Build

```powershell
mvn -f pom.xml -DskipTests compile
```

### Run Frontend

```powershell
Set-Location gym-ui
npm install
npm run dev
```

### Run Backends

```powershell
Set-Location ..\gymMaster
mvn spring-boot:run -DskipTests

Set-Location ..\gym-management-system-master
mvn spring-boot:run -DskipTests
```

If MySQL or Redis is not available, backend startup can fail during datasource/cache initialization.

## Verification Commands

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-frontend.ps1 -SkipInstall -SkipBuild
```

## CI/CD

- CI workflow: `.github/workflows/ci.yml`
- Release workflow: `.github/workflows/cd-release.yml`

## Open Source Project Files

- [LICENSE](LICENSE)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- [SECURITY.md](SECURITY.md)
- [SUPPORT.md](SUPPORT.md)

## Notes on IDE Problems Count

The large number of Problems (e.g. 540) is mostly from strict static analysis warnings over legacy code (raw generics, unused imports, style rules), not from compile-breaking errors. CI/build status is the reliability baseline; warnings are being reduced in incremental cleanup commits.