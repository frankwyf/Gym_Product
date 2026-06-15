$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$miniDir = Join-Path $repoRoot 'GymMaster_wx'

if (-not (Test-Path $miniDir)) {
  throw "Mini-program directory not found: $miniDir"
}

Write-Host '[verify-miniapp] Checking JavaScript syntax...' -ForegroundColor Cyan
$jsFiles = Get-ChildItem -Path $miniDir -Recurse -File -Filter *.js
$failedJs = @()
foreach ($file in $jsFiles) {
  node --check $file.FullName 2>$null
  if ($LASTEXITCODE -ne 0) {
    $failedJs += $file.FullName
  }
}
if ($failedJs.Count -gt 0) {
  throw ('JavaScript syntax check failed: ' + ($failedJs -join ', '))
}

Write-Host '[verify-miniapp] Validating JSON files...' -ForegroundColor Cyan
$jsonFiles = Get-ChildItem -Path $miniDir -Recurse -File -Filter *.json
$failedJson = @()
foreach ($file in $jsonFiles) {
  node -e "const fs=require('fs'); JSON.parse(fs.readFileSync(process.argv[1], 'utf8'));" "$($file.FullName)" 2>$null
  if ($LASTEXITCODE -ne 0) {
    $failedJson += $file.FullName
  }
}
if ($failedJson.Count -gt 0) {
  throw ('JSON parse failed: ' + ($failedJson -join ', '))
}

Write-Host '[verify-miniapp] Completed successfully.' -ForegroundColor Green
