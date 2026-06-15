$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$mobileDir = Join-Path $repoRoot 'gym-mobile-app'

if (-not (Test-Path $mobileDir)) {
  throw "Mobile app directory not found: $mobileDir"
}

Write-Host '[verify-mobile] Running TypeScript checks...' -ForegroundColor Cyan
Push-Location $mobileDir
try {
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
