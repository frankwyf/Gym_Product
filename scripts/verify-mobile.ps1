$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$mobileDir = Join-Path $repoRoot 'gym-mobile-app'
$mobileNodeModules = Join-Path $mobileDir 'node_modules'
$mobileJestTypes = Join-Path $mobileNodeModules '@types\jest'
$mobileLockFile = Join-Path $mobileDir 'package-lock.json'

if (-not (Test-Path $mobileDir)) {
  throw "Mobile app directory not found: $mobileDir"
}

Write-Host '[verify-mobile] Running TypeScript checks...' -ForegroundColor Cyan
Push-Location $mobileDir
try {
  if (-not (Test-Path $mobileNodeModules) -or -not (Test-Path $mobileJestTypes)) {
    Write-Host '[verify-mobile] Installing dependencies with npm ci...' -ForegroundColor Yellow
    if (Test-Path $mobileLockFile) {
      npm ci --no-audit --no-fund --legacy-peer-deps
    }
    else {
      npm install --no-audit --no-fund --legacy-peer-deps
    }
    if ($LASTEXITCODE -ne 0) {
      throw "Mobile dependency install failed with exit code $LASTEXITCODE"
    }
  }

  npx tsc --noEmit
  if ($LASTEXITCODE -ne 0) {
    throw "TypeScript check failed with exit code $LASTEXITCODE"
  }

  Write-Host '[verify-mobile] Checking Expo dependency compatibility...' -ForegroundColor Cyan
  npx expo install --check
  if ($LASTEXITCODE -ne 0) {
    throw "Expo dependency check failed with exit code $LASTEXITCODE"
  }
}
finally {
  Pop-Location
}

Write-Host '[verify-mobile] Completed successfully.' -ForegroundColor Green
