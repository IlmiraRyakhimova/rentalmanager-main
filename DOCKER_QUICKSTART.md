# 🐳 Docker Compose - Быстрый старт

## ✅ Что добавлено

Docker Compose теперь запускает **ВСЕ ТРИ СЕРВИСА**:
1. PostgreSQL (база данных)
2. Backend (Spring Boot API)
3. **Frontend (Vue 3 + Nginx)** ⭐ НОВОЕ!

## 🚀 Как запустить

### 1. Убедитесь что Docker установлен
```bash
docker --version
docker-compose --version
```

### 2. Создайте .env файл в корне проекта
```env
DB_USERNAME=postgres
DB_PASSWORD=mypassword
JWT_SECRET=my-super-secret-jwt-key-must-be-at-least-256-bits
JWT_EXPIRATION=86400000
EMAIL_ADDRESS=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### 3. Запустите все сервисы
```bash
docker-compose up -d --build
```

### 4. Откройте браузер
**Frontend:** http://localhost

**Backend:** http://localhost:8080

## 📋 Полезные команды

```bash
# Проверить статус
docker-compose ps

# Посмотреть логи
docker-compose logs -f

# Остановить все
docker-compose down

# Перезапустить фронтенд
docker-compose restart frontend-service

# Пересобрать только фронтенд
docker-compose up -d frontend-service --build
```

## 🎯 Что изменилось

### Новые файлы
- `front/Dockerfile` - multi-stage build (Node → Nginx)
- `front/nginx.conf` - конфигурация Nginx с проксированием API
- `front/.dockerignore` - исключения для Docker
- `front/.env.production` - production переменные

### Обновленные файлы
- `docker-compose.yml` - добавлен `frontend-service`
- `README.md` - инструкции по Docker
- `DOCKER_GUIDE.md` - подробная документация

## 🌐 Архитектура

```
Browser (http://localhost)
    ↓
Nginx (frontend-service:80)
    ↓ статика: раздает Vue app
    ↓ /api/*: проксирует →
    ↓
Spring Boot (backend-service:8080)
    ↓
PostgreSQL (postgres-db:5432)
```

## ✨ Фичи Nginx

- ✅ Раздает Vue приложение
- ✅ Проксирует `/api/*` на backend
- ✅ Gzip сжатие
- ✅ Кэширование статики (1 год)
- ✅ Security headers
- ✅ SPA роутинг (Vue Router)

## 🔧 Как это работает

1. **Frontend запрашивает** http://localhost
2. **Nginx отдает** `index.html` и статику
3. **Vue делает запрос** к `/api/auth/sign-up`
4. **Nginx проксирует** на `backend-service:8080/api/auth/sign-up`
5. **Backend обрабатывает** и возвращает ответ
6. **Nginx передает** ответ фронтенду

**Нет проблем с CORS!** Все через один origin.

## 📦 Размеры

- Frontend образ: ~50 MB (Nginx + статика)
- Backend образ: ~200-300 MB
- PostgreSQL: ~150 MB

## 🐛 Troubleshooting

### Порт 80 занят
```bash
# Linux
sudo lsof -i :80
sudo kill -9 <PID>

# Или измените порт в docker-compose.yml
ports:
  - "8081:80"  # теперь на порту 8081
```

### Фронтенд не загружается
```bash
# Проверьте логи
docker-compose logs frontend-service

# Пересоберите
docker-compose up -d frontend-service --build
```

### Ошибки API
```bash
# Проверьте что backend запущен
docker-compose logs backend-service

# Проверьте nginx конфигурацию
docker-compose exec frontend-service cat /etc/nginx/conf.d/default.conf
```

## 📚 Полная документация

См. [DOCKER_GUIDE.md](DOCKER_GUIDE.md) для деталей.

## ✅ Готово!

Теперь **весь стек** запускается одной командой:

```bash
docker-compose up -d --build
```

И доступен на **http://localhost** 🎉
