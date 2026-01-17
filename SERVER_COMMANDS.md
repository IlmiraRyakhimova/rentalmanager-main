# 📋 Шпаргалка команд для сервера

Полезные команды для управления приложением на сервере **74.119.193.196**.

## 🔐 Подключение к серверу

```bash
ssh root@74.119.193.196
# Пароль: Nhy2qV9hOF5h (СМЕНИТЕ ПОСЛЕ ПЕРВОГО ВХОДА!)
```

## 📂 Навигация

```bash
# Перейти в директорию проекта
cd /root/rentalmanager-main

# Переключиться на ветку deploy
git checkout deploy
```

## 🚀 Управление приложением

### Запуск/Остановка

```bash
# Запустить все сервисы
docker-compose up -d

# Остановить все сервисы (данные БД сохранятся!)
docker-compose down

# Перезапустить все сервисы
docker-compose restart

# Перезапустить только один сервис
docker-compose restart backend-service
```

### Деплой и обновление

```bash
# Полный деплой с пересборкой
./deploy.sh

# Или вручную:
git pull origin deploy
docker-compose build --no-cache
docker-compose up -d
```

## 📊 Мониторинг

### Готовые скрипты

```bash
# Проверка статуса всех сервисов
./status.sh

# Просмотр логов
./logs.sh          # Все логи
./logs.sh backend  # Только backend
./logs.sh frontend # Только frontend
./logs.sh db       # Только база данных
```

### Ручные команды

```bash
# Статус контейнеров
docker-compose ps

# Логи всех сервисов
docker-compose logs -f

# Логи конкретного сервиса (последние 100 строк)
docker-compose logs -f --tail=100 backend-service
docker-compose logs -f --tail=100 frontend-service
docker-compose logs -f --tail=100 postgres-db

# Использование ресурсов в реальном времени
docker stats
```

## 💾 Управление базой данных

### Бэкап

```bash
# Автоматический бэкап
./backup-db.sh

# Или вручную
docker exec rental-postgres pg_dump -U rental_user rental_manager > backup.sql

# Бэкап с датой
docker exec rental-postgres pg_dump -U rental_user rental_manager > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Восстановление

```bash
# Восстановить из бэкапа
docker exec -i rental-postgres psql -U rental_user rental_manager < backup.sql
```

### Подключение к БД

```bash
# Войти в консоль PostgreSQL
docker exec -it rental-postgres psql -U rental_user rental_manager

# Внутри PostgreSQL:
\dt              # Показать все таблицы
\d users         # Описание таблицы users
SELECT * FROM users LIMIT 10;  # Выбрать первые 10 пользователей
\q               # Выход
```

## 🔍 Диагностика проблем

### Проверка контейнеров

```bash
# Список всех контейнеров (включая остановленные)
docker ps -a

# Инспекция контейнера
docker inspect rental-backend

# Войти внутрь контейнера
docker exec -it rental-backend bash
```

### Проверка сети

```bash
# Проверка портов
netstat -tulpn | grep LISTEN

# Проверка доступности портов снаружи
curl http://localhost:3000  # Frontend
curl http://localhost:8080  # Backend
```

### Проверка логов Docker

```bash
# Системные логи Docker
journalctl -u docker -f

# Логи конкретного контейнера
docker logs rental-backend
docker logs rental-frontend
docker logs rental-postgres
```

## 🗑️ Очистка

```bash
# Удалить остановленные контейнеры
docker container prune

# Удалить неиспользуемые образы
docker image prune

# Удалить всё неиспользуемое (кроме volumes!)
docker system prune

# ⚠️ ОПАСНО: Удалить всё включая volumes (УДАЛИТ ДАННЫЕ БД!)
# НЕ ЗАПУСКАЙТЕ эту команду без крайней необходимости!
# docker system prune -a --volumes
```

## 📦 Управление Docker Volumes

```bash
# Список всех volumes
docker volume ls

# Информация о volume с данными БД
docker volume inspect rentalmanager-main_postgres-data

# Резервное копирование volume
docker run --rm -v rentalmanager-main_postgres-data:/data -v $(pwd):/backup ubuntu tar czf /backup/postgres-backup.tar.gz /data

# ⚠️ ОПАСНО: Удаление volume (УДАЛИТ ВСЕ ДАННЫЕ БД!)
# docker volume rm rentalmanager-main_postgres-data
```

## 🔄 Git операции

```bash
# Получить последние изменения
git fetch origin deploy

# Посмотреть что изменилось
git log HEAD..origin/deploy --oneline

# Применить изменения
git pull origin deploy

# Сбросить все локальные изменения
git reset --hard origin/deploy
```

## 🔐 Безопасность

```bash
# Сменить пароль пользователя root
passwd

# Настроить firewall
ufw allow 22/tcp    # SSH
ufw allow 3000/tcp  # Frontend
ufw allow 8080/tcp  # Backend
ufw enable
ufw status

# Просмотр активных подключений
netstat -an | grep ESTABLISHED
```

## 📈 Обновление системы

```bash
# Обновить пакеты системы
apt update
apt upgrade -y

# Обновить Docker
apt update
apt install --only-upgrade docker.io

# Обновить Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

## 🆘 Экстренное восстановление

### Если контейнеры не запускаются:

```bash
# 1. Остановить всё
docker-compose down

# 2. Проверить логи
docker-compose logs

# 3. Пересобрать всё с нуля
docker-compose build --no-cache

# 4. Запустить
docker-compose up -d
```

### Если приложение не работает после обновления:

```bash
# 1. Вернуться к предыдущей версии
git log --oneline  # Найти хэш предыдущего коммита
git reset --hard <commit-hash>

# 2. Пересобрать
./deploy.sh

# 3. Проверить логи
./logs.sh
```

## 📞 Полезные адреса

- **Frontend:** http://74.119.193.196:3000
- **Backend API:** http://74.119.193.196:8080
- **API Docs:** http://74.119.193.196:8080/swagger-ui.html (если настроено)

---

**💡 Совет:** Добавьте эти команды в `.bash_history` для быстрого доступа!
