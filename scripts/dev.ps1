$defaultSecret = "dev-secret-key-change-me-32-chars"

if (-not $env:JWT_SECRET -or $env:JWT_SECRET.Trim() -eq "") {
    $env:JWT_SECRET = $defaultSecret
    Write-Host "JWT_SECRET not set; using a local default for development."
}

mvn clean package
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

mvn spring-boot:run

