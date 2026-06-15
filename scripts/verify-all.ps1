param(
  [switch]$SkipInstall,
  [switch]$SkipFrontendBuild
)

$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$results = @()

function Add-Result {
  param(
    [string]$Component,
    [string]$Status,
    [string]$Message
  )
  $script:results += [PSCustomObject]@{
    Component = $Component
    Status = $Status
    Message = $Message
  }
}

function Invoke-Step {
  param(
    [string]$Component,
    [scriptblock]$Script
  )

  try {
    & $Script
    Add-Result -Component $Component -Status 'PASS' -Message 'OK'
  }
  catch {
    Add-Result -Component $Component -Status 'FAIL' -Message $_.Exception.Message
  }
}

function Invoke-StepWarn {
  param(
    [string]$Component,
    [scriptblock]$Script
  )

  try {
    & $Script
    Add-Result -Component $Component -Status 'PASS' -Message 'OK'
  }
  catch {
    Add-Result -Component $Component -Status 'WARN' -Message $_.Exception.Message
  }
}

Write-Host '[verify-all] Starting cross-project validation...' -ForegroundColor Cyan

Invoke-Step -Component 'mobile: typescript' -Script {
  Push-Location (Join-Path $repoRoot 'gym-mobile-app')
  try {
    npx tsc --noEmit
    if ($LASTEXITCODE -ne 0) {
      throw "mobile TypeScript check failed with exit code $LASTEXITCODE"
    }
  }
  finally {
    Pop-Location
  }
}

Invoke-Step -Component 'mobile: expo dependency check' -Script {
  Push-Location (Join-Path $repoRoot 'gym-mobile-app')
  try {
    npx expo install --check
    if ($LASTEXITCODE -ne 0) {
      throw "mobile expo dependency check failed with exit code $LASTEXITCODE"
    }
  }
  finally {
    Pop-Location
  }
}

Invoke-Step -Component 'frontend: tests/build' -Script {
  $frontendArgs = @(
    '-ExecutionPolicy', 'Bypass',
    '-File', (Join-Path $PSScriptRoot 'verify-frontend.ps1')
  )

  if ($SkipInstall) {
    $frontendArgs += '-SkipInstall'
  }
  if ($SkipFrontendBuild) {
    $frontendArgs += '-SkipBuild'
  }

  & powershell @frontendArgs
  if ($LASTEXITCODE -ne 0) {
    throw "verify-frontend.ps1 failed with exit code $LASTEXITCODE"
  }
}

Invoke-Step -Component 'mini-program: js syntax' -Script {
  $wxDir = Join-Path $repoRoot 'GymMaster_wx'
  $files = Get-ChildItem -Path $wxDir -Recurse -File -Filter *.js

  $failed = @()
  foreach ($file in $files) {
    node --check $file.FullName 2>$null
    if ($LASTEXITCODE -ne 0) {
      $failed += $file.FullName
    }
  }

  if ($failed.Count -gt 0) {
    throw ("JavaScript syntax check failed in: " + ($failed -join ', '))
  }
}

Invoke-StepWarn -Component 'backend: gymMaster compile' -Script {
  $gymMasterDir = Join-Path $repoRoot 'gymMaster'
  $wrapperProperties = Join-Path $gymMasterDir '.mvn/wrapper/maven-wrapper.properties'

  if (Test-Path $wrapperProperties) {
    Push-Location $gymMasterDir
    try {
      .\mvnw.cmd -q -DskipTests compile
      if ($LASTEXITCODE -ne 0) {
        throw "gymMaster compile failed with exit code $LASTEXITCODE"
      }
    }
    finally {
      Pop-Location
    }
    return
  }

  if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn -f (Join-Path $gymMasterDir 'pom.xml') -q -DskipTests compile
    if ($LASTEXITCODE -ne 0) {
      throw "gymMaster compile failed with exit code $LASTEXITCODE"
    }
    return
  }

  throw 'Maven unavailable and gymMaster wrapper metadata missing (.mvn/wrapper).'
}

Invoke-StepWarn -Component 'backend: gym-management-system-master compile' -Script {
  if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    throw 'Maven unavailable (mvn not found in PATH).'
  }

  mvn -f (Join-Path $repoRoot 'gym-management-system-master/pom.xml') -q -DskipTests compile
  if ($LASTEXITCODE -ne 0) {
    throw "gym-management-system-master compile failed with exit code $LASTEXITCODE"
  }
}

Write-Host ''
Write-Host '[verify-all] Summary' -ForegroundColor Cyan
$results | Format-Table -AutoSize

$hardFailures = @($results | Where-Object { $_.Status -eq 'FAIL' })
if ($hardFailures.Count -gt 0) {
  Write-Host '[verify-all] Completed with failures.' -ForegroundColor Red
  exit 1
}

Write-Host '[verify-all] Completed successfully (warnings may remain).' -ForegroundColor Green
