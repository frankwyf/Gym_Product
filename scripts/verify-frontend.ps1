param(
  [switch]$SkipInstall
)

$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$frontendDir = Join-Path $repoRoot 'gym-ui'

Write-Host '[verify-frontend] Start frontend quality checks...' -ForegroundColor Cyan

if (-not $SkipInstall) {
  Write-Host '[verify-frontend] Installing dependencies with npm ci...' -ForegroundColor Yellow
  npm --prefix $frontendDir ci --legacy-peer-deps --no-audit --no-fund
}

Write-Host '[verify-frontend] Running unit tests...' -ForegroundColor Yellow
npm --prefix $frontendDir run test:unit

Write-Host '[verify-frontend] Running smoke e2e...' -ForegroundColor Yellow
npm --prefix $frontendDir run test:e2e:smoke

Write-Host '[verify-frontend] Running production build...' -ForegroundColor Yellow
$env:NODE_OPTIONS = '--openssl-legacy-provider'
npm --prefix $frontendDir run build:prod

Write-Host '[verify-frontend] Completed successfully.' -ForegroundColor Green
