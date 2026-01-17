#!/bin/bash

# Скрипт для удобного просмотра логов
# Использование: 
#   ./logs.sh          - все логи
#   ./logs.sh backend  - только backend
#   ./logs.sh frontend - только frontend
#   ./logs.sh db       - только база данных

SERVICE=${1:-"all"}

case $SERVICE in
  backend)
    echo "📝 Логи Backend сервиса..."
    docker-compose logs -f --tail=100 backend-service
    ;;
  frontend)
    echo "📝 Логи Frontend сервиса..."
    docker-compose logs -f --tail=100 frontend-service
    ;;
  db|database)
    echo "📝 Логи базы данных..."
    docker-compose logs -f --tail=100 postgres-db
    ;;
  redis)
    echo "📝 Логи Redis..."
    docker-compose logs -f --tail=100 redis
    ;;
  all)
    echo "📝 Логи всех сервисов..."
    docker-compose logs -f --tail=50
    ;;
  *)
    echo "❌ Неизвестный сервис: $SERVICE"
    echo ""
    echo "Использование: $0 [backend|frontend|db|redis|all]"
    echo ""
    echo "Примеры:"
    echo "  $0          - все логи"
    echo "  $0 backend  - только backend"
    echo "  $0 frontend - только frontend"
    echo "  $0 db       - только база данных"
    echo "  $0 redis    - только redis"
    exit 1
    ;;
esac
