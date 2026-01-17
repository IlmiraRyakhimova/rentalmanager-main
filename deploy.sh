#!/bin/bash

# Скрипт для безопасного деплоя приложения с сохранением базы данных
# ВАЖНО: База данных использует Docker volumes и не будет удалена

set -e  # Остановка при ошибке

echo "🚀 Начало деплоя приложения..."

# Проверка наличия .env файла
if [ ! -f .env ]; then
    echo "❌ ОШИБКА: Файл .env не найден!"
    echo "Создайте файл .env с необходимыми переменными окружения"
    exit 1
fi

echo "✓ Файл .env найден"

# Остановка и пересборка ТОЛЬКО backend и frontend (БД и Redis НЕ трогаем!)
echo "📦 Остановка и удаление контейнеров backend и frontend..."
docker-compose stop backend-service frontend-service
docker-compose rm -f backend-service frontend-service

echo "🔨 Пересборка Docker образов backend и frontend..."
docker-compose build --no-cache backend-service frontend-service

echo "🚀 Запуск обновленных контейнеров backend и frontend..."
docker-compose up -d backend-service frontend-service

echo "⏳ Ожидание готовности сервисов..."
sleep 10

# Проверка состояния контейнеров
echo "📊 Проверка состояния контейнеров:"
docker-compose ps

# Проверка логов backend для выявления возможных ошибок
echo "📝 Последние логи backend:"
docker-compose logs --tail=50 backend-service

echo "✅ Деплой завершен успешно!"
echo ""
echo "📍 Приложение доступно по адресу:"
echo "   Frontend: http://74.119.193.196:3000"
echo "   Backend API: http://74.119.193.196:8080"
echo ""
echo "💾 База данных сохранена в Docker volume: postgres-data"
echo "   Данные НЕ будут удалены при обновлении контейнеров"
