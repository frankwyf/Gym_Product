$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $PSScriptRoot
$mvnCmd = Join-Path $root '.tools\apache-maven-3.9.9\bin\mvn.cmd'

if (!(Test-Path $mvnCmd)) {
    Write-Host 'Local Maven not found. Running setup-local-maven.ps1 first...'
    & (Join-Path $PSScriptRoot 'setup-local-maven.ps1')
}

& $mvnCmd -f (Join-Path $root 'gym-management-system-master\pom.xml') -DskipTests org.codehaus.mojo:exec-maven-plugin:3.5.0:java "-Dexec.mainClass=com.gym.GymApplication"
