#!/bin/bash

# Скрипт для создания бэкапа базы данных
# Использование: ./backup-db.sh

set -e

BACKUP_DIR="./backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/backup_$TIMESTAMP.sql"

# Создание директории для бэкапов
mkdir -p "$BACKUP_DIR"

echo "🔄 Создание бэкапа базы данных..."
echo "📁 Файл: $BACKUP_FILE"

# Создание бэкапа
docker exec rental-postgres pg_dump -U rental_user rental_manager > "$BACKUP_FILE"

# Проверка размера файла
FILE_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)

echo "✅ Бэкап создан успешно!"
echo "📊 Размер: $FILE_SIZE"
echo "📂 Путь: $BACKUP_FILE"

# Удаление старых бэкапов (старше 7 дней)
find "$BACKUP_DIR" -name "backup_*.sql" -mtime +7 -delete
echo "🧹 Старые бэкапы (>7 дней) удалены"

echo ""
echo "Для восстановления используйте:"
echo "docker exec -i rental-postgres psql -U rental_user rental_manager < $BACKUP_FILE"
