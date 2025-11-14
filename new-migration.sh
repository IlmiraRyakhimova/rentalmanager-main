#!/bin/bash

# Переменные путей
MASTER_XML="main-service/src/main/resources/db/changelog/master.xml"
SQL_DIR="main-service/src/main/resources/db/changelog/sql"

mkdir -p "$SQL_DIR"

timestamp=$(date +%Y%m%d%H%M%S)
echo "Введите описание миграции:"
read -r description

filename="${timestamp}-${description// /-}.sql"
full_path="$SQL_DIR/$filename"

cat > "$full_path" << EOF
--liquibase formatted sql

--changeset $(whoami):${timestamp}-1
-- Ваши SQL команды здесь

EOF

if [ -f "$MASTER_XML" ]; then
    include_line="    <include file=\"db/changelog/sql/$filename\"/>"

    if grep -q "</databaseChangeLog>" "$MASTER_XML"; then
        sed -i.tmp "/<\/databaseChangeLog>/i $include_line" "$MASTER_XML"
        rm -f "$MASTER_XML.tmp"
    else
        echo "$include_line" >> "$MASTER_XML"
        echo "</databaseChangeLog>" >> "$MASTER_XML"
    fi

    echo "✅ Файл создан: $full_path"
    echo "✅ Include автоматически добавлен в master.xml"
else
    echo "✅ Файл создан: $full_path"
    echo "⚠️  master.xml не найден, добавьте вручную:"
    echo "   <include file=\"db/changelog/sql/$filename\"/>"
fi