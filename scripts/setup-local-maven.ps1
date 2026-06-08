$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $PSScriptRoot
$toolsDir = Join-Path $root '.tools'
$mavenVersion = '3.9.9'
$mavenHome = Join-Path $toolsDir ("apache-maven-" + $mavenVersion)
$mvnCmd = Join-Path $mavenHome 'bin\mvn.cmd'

if (Test-Path $mvnCmd) {
    Write-Host "Maven already available at $mavenHome"
    & $mvnCmd -v
    exit 0
}

New-Item -ItemType Directory -Path $toolsDir -Force | Out-Null
$zipPath = Join-Path $toolsDir ("apache-maven-" + $mavenVersion + "-bin.zip")
$downloadUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"

Write-Host "Downloading Maven from: $downloadUrl"
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath

Write-Host "Extracting Maven to: $toolsDir"
Expand-Archive -Path $zipPath -DestinationPath $toolsDir -Force

& $mvnCmd -v
