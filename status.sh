#!/bin/bash

# Скрипт для проверки состояния всех сервисов
# Использование: ./status.sh

echo "════════════════════════════════════════════════════════"
echo "   🔍 Состояние сервисов Rental Manager"
echo "════════════════════════════════════════════════════════"
echo ""

# Проверка Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не установлен!"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose не установлен!"
    exit 1
fi

echo "✅ Docker и Docker Compose установлены"
echo ""

# Статус контейнеров
echo "📦 Статус контейнеров:"
echo "────────────────────────────────────────────────────────"
docker-compose ps
echo ""

# Использование ресурсов
echo "💻 Использование ресурсов:"
echo "────────────────────────────────────────────────────────"
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" rental-postgres rental-backend rental-frontend rental-redis 2>/dev/null || echo "Контейнеры не запущены"
echo ""

# Проверка volumes
echo "💾 Docker Volumes (данные БД и Redis):"
echo "────────────────────────────────────────────────────────"
docker volume ls | grep rentalmanager-main || echo "Volumes не найдены"
echo ""

# Проверка портов
echo "🌐 Открытые порты:"
echo "────────────────────────────────────────────────────────"
echo "Frontend:  http://74.119.193.196:3000"
echo "Backend:   http://74.119.193.196:8080"
echo "Postgres:  74.119.193.196:5432"
echo "Redis:     74.119.193.196:6379"
echo ""

# Проверка доступности сервисов
echo "🔌 Проверка доступности:"
echo "────────────────────────────────────────────────────────"

# Backend health check
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null | grep -q "200"; then
    echo "✅ Backend: Доступен"
else
    echo "⚠️  Backend: Недоступен или не имеет health endpoint"
fi

# Frontend check
if curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 2>/dev/null | grep -q "200"; then
    echo "✅ Frontend: Доступен"
else
    echo "❌ Frontend: Недоступен"
fi

# PostgreSQL check
if docker exec rental-postgres pg_isready -U rental_user &>/dev/null; then
    echo "✅ PostgreSQL: Доступна"
else
    echo "❌ PostgreSQL: Недоступна"
fi

# Redis check
if docker exec rental-redis redis-cli ping &>/dev/null | grep -q "PONG"; then
    echo "✅ Redis: Доступен"
else
    echo "❌ Redis: Недоступен"
fi

echo ""
echo "════════════════════════════════════════════════════════"
echo "Для просмотра логов используйте: ./logs.sh [сервис]"
echo "Для деплоя используйте: ./deploy.sh"
echo "Для бэкапа БД используйте: ./backup-db.sh"
echo "════════════════════════════════════════════════════════"
