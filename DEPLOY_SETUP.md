# Настройка автоматического деплоя

## 🎯 Что сделано

Настроен автоматический деплой на сервер **74.119.193.196** при изменениях в ветке `deploy`.

### ✅ Гарантии безопасности данных

**ВАЖНО:** База данных защищена от удаления:
- Используется Docker volume `postgres-data`
- При команде `docker-compose down` удаляются только контейнеры, НЕ volumes
- Данные БД сохраняются между обновлениями
- Redis данные также защищены volume `redis-data`

---

## 📋 Инструкция по настройке GitHub Secrets

Для работы автоматического деплоя необходимо добавить секреты в GitHub:

1. Перейдите в репозиторий на GitHub
2. Settings → Secrets and variables → Actions
3. Добавьте следующие секреты (кнопка "New repository secret"):

### Обязательные секреты:

| Имя секрета | Значение | Описание |
|------------|----------|----------|
| `SERVER_IP` | `74.119.193.196` | IP адрес вашего сервера |
| `SERVER_USER` | `root` | Имя пользователя для SSH |
| `SERVER_PASSWORD` | `Nhy2qV9hOF5h` | Пароль от сервера |

---

## 🚀 Как использовать

### Автоматический деплой

1. Сделайте изменения в коде
2. Закоммитьте и запушьте в ветку `deploy`:
   ```bash
   git add .
   git commit -m "Описание изменений"
   git push origin deploy
   ```
3. GitHub Actions автоматически:
   - Подключится к серверу
   - Загрузит последние изменения
   - Запустит деплой скрипт
   - Пересоберёт Docker образы
   - Перезапустит контейнеры
   - **СОХРАНИТ все данные в БД**

### Мониторинг деплоя

- Статус деплоя можно отслеживать в разделе **Actions** на GitHub
- Логи деплоя будут доступны в деталях workflow

---

## 🛠️ Подготовка сервера (выполнить один раз)

После первого входа на сервер выполните:

```bash
# 1. Подключитесь к серверу
ssh root@74.119.193.196
# Пароль: Nhy2qV9hOF5h

# 2. Смените пароль (как рекомендовано)
passwd

# 3. Установите Docker и Docker Compose
apt update
apt install -y docker.io docker-compose git

# 4. Клонируйте репозиторий
cd /root
git clone https://github.com/IlmiraRyakhimova/rentalmanager-main.git
cd rentalmanager-main
git checkout deploy

# 5. Создайте файл .env с настройками
nano .env
```

### Содержимое .env файла:

```env
# Database
DB_USERNAME=rental_user
DB_PASSWORD=your_secure_db_password_here

# JWT
JWT_SECRET=your_very_long_secret_key_at_least_256_bits
JWT_EXPIRATION=86400000

# Email
EMAIL_ADDRESS=your_email@example.com
EMAIL_PASSWORD=your_email_app_password
```

```bash
# 6. Сделайте скрипт деплоя исполняемым
chmod +x deploy.sh

# 7. Первый запуск
./deploy.sh
```

---

## 🔍 Проверка работы

После деплоя приложение будет доступно:

- **Frontend:** http://74.119.193.196:3000
- **Backend API:** http://74.119.193.196:8080

### Полезные команды на сервере:

```bash
# Посмотреть статус контейнеров
docker-compose ps

# Посмотреть логи
docker-compose logs -f

# Посмотреть только логи backend
docker-compose logs -f backend-service

# Посмотреть только логи frontend
docker-compose logs -f frontend-service

# Посмотреть Docker volumes (где хранятся данные БД)
docker volume ls

# Посмотреть информацию о volume с БД
docker volume inspect rentalmanager-main_postgres-data
```

---

## 🆘 Команды для восстановления

### Если что-то пошло не так:

```bash
# Остановить все контейнеры
docker-compose down

# Запустить заново
docker-compose up -d

# Пересобрать образы
docker-compose build --no-cache
docker-compose up -d
```

### Если нужно сделать бэкап БД:

```bash
# Создать бэкап
docker exec rental-postgres pg_dump -U rental_user rental_manager > backup_$(date +%Y%m%d_%H%M%S).sql

# Восстановить из бэкапа
docker exec -i rental-postgres psql -U rental_user rental_manager < backup_20250117_120000.sql
```

---

## ⚠️ ВАЖНО: Защита данных

### Что защищает данные БД:

1. **Docker volumes** - данные хранятся отдельно от контейнеров
2. **`docker-compose down`** - удаляет только контейнеры, НЕ volumes
3. Чтобы УДАЛИТЬ данные, нужно явно указать: `docker-compose down -v` (НЕ ДЕЛАЙТЕ ТАК!)

### Данные БД останутся при:
- Перезапуске контейнеров
- Пересборке образов
- Обновлении кода
- Команде `docker-compose down`

### Данные БД будут удалены ТОЛЬКО при:
- Явной команде `docker-compose down -v` (с флагом -v)
- Удалении volume: `docker volume rm rentalmanager-main_postgres-data`

---

## 🔐 Безопасность

### Рекомендации:

1. **Смените пароль сервера** после первого входа
2. Обновите секрет `SERVER_PASSWORD` в GitHub после смены пароля
3. Используйте сильные пароли в `.env` файле
4. Не коммитьте `.env` файл в репозиторий (уже в .gitignore)
5. Настройте firewall на сервере:
   ```bash
   ufw allow 22/tcp    # SSH
   ufw allow 3000/tcp  # Frontend
   ufw allow 8080/tcp  # Backend
   ufw enable
   ```

---

## 📞 Поддержка

При возникновении проблем проверьте:
1. Логи GitHub Actions
2. Логи на сервере: `docker-compose logs`
3. Статус контейнеров: `docker-compose ps`
4. Наличие и корректность `.env` файла на сервере
