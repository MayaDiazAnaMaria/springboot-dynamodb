Write-Host "🚀 Parando WSL completamente..."
wsl --shutdown

Write-Host "🚀 Reiniciando Docker Desktop..."
Start-Process "Docker Desktop"

Write-Host "⏳ Esperando a que Docker arranque..."
Start-Sleep -Seconds 15

docker info
