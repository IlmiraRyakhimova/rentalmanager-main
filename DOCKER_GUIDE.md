# 🐳 Docker Compose - Полный стек Rental Manager

## 📦 Что включено

Docker Compose теперь запускает все три сервиса:

1. **PostgreSQL** (порт 5432) - База данных
2. **Backend** (порт 8080) - Spring Boot API
3. **Frontend** (порт 80) - Vue 3 приложение через Nginx

## 🚀 Быстрый старт

### 1. Создайте .env файл в корне проекта

```bash
# Database
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION=86400000

# Email (для верификации)
EMAIL_ADDRESS=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### 2. Запустите все сервисы

```bash
# Из корня проекта
docker-compose up -d --build
```

Флаг `--build` нужен при первом запуске или после изменений кода.

### 3. Проверьте статус

```bash
docker-compose ps
```

Вы должны увидеть 3 запущенных контейнера:
- `rental-postgres`
- `rental-backend`
- `rental-frontend`

### 4. Откройте приложение

**Frontend:** http://localhost

**Backend API:** http://localhost:8080

**Database:** localhost:5432

## 📋 Команды управления

### Запуск всех сервисов
```bash
docker-compose up -d
```

### Пересборка и запуск (после изменений)
```bash
docker-compose up -d --build
```

### Запуск только фронтенда
```bash
docker-compose up -d frontend-service --build
```

### Остановка всех сервисов
```bash
docker-compose down
```

### Остановка с удалением volumes (БД будет очищена!)
```bash
docker-compose down -v
```

### Просмотр логов
```bash
# Все сервисы
docker-compose logs -f

# Только фронтенд
docker-compose logs -f frontend-service

# Только бэкенд
docker-compose logs -f backend-service

# Только БД
docker-compose logs -f postgres-db
```

### Перезапуск сервиса
```bash
# Перезапустить фронтенд
docker-compose restart frontend-service

# Перезапустить бэкенд
docker-compose restart backend-service
```

## 🏗️ Архитектура

### Frontend (Nginx + Vue)
- **Образ:** Node 20 Alpine (build) + Nginx Alpine (runtime)
- **Порт:** 80
- **Функции:**
  - Раздает статические файлы Vue приложения
  - Проксирует `/api/*` запросы на backend
  - Gzip сжатие
  - Кэширование статики

### Backend (Spring Boot)
- **Образ:** Maven + OpenJDK
- **Порт:** 8080
- **Функции:**
  - REST API
  - JWT аутентификация
  - Подключение к PostgreSQL

### Database (PostgreSQL)
- **Образ:** PostgreSQL 15
- **Порт:** 5432
- **Healthcheck:** проверка готовности

## 🔧 Nginx конфигурация

Frontend использует Nginx с:
- Роутингом для Vue Router (SPA)
- Проксированием API на backend
- Кэшированием статики (1 год)
- Gzip сжатием
- Security headers

## 📁 Структура файлов Docker

```
rentalmanager-main/
├── docker-compose.yml          # Оркестрация всех сервисов
├── .env                        # Переменные окружения
├── main-service/
│   └── Dockerfile             # Backend образ
└── front/
    ├── Dockerfile             # Frontend образ (multi-stage)
    ├── nginx.conf             # Nginx конфигурация
    ├── .dockerignore          # Исключения для Docker
    └── .env.production        # Production переменные
```

## 🌐 Доступ к сервисам

После запуска `docker-compose up -d`:

| Сервис | URL | Описание |
|--------|-----|----------|
| Frontend | http://localhost | Vue приложение |
| Backend API | http://localhost:8080 | Spring Boot API |
| Database | localhost:5432 | PostgreSQL |

**Важно:** Фронтенд теперь на порту 80, а не 5173!

## 🔄 API проксирование

Когда фронтенд запущен через Docker:
- Запросы к `/api/*` автоматически проксируются на `backend-service:8080`
- Не нужно указывать полный URL в `.env.production`
- CORS настроен в Nginx

## 🐛 Troubleshooting

### Фронтенд не загружается
```bash
# Проверьте логи
docker-compose logs -f frontend-service

# Пересоберите
docker-compose up -d frontend-service --build
```

### Backend не отвечает
```bash
# Проверьте логи
docker-compose logs -f backend-service

# Проверьте подключение к БД
docker-compose logs -f postgres-db
```

### Ошибка "port already in use"
```bash
# Порт 80 занят
sudo lsof -i :80
sudo kill -9 <PID>

# Порт 8080 занят
sudo lsof -i :8080
sudo kill -9 <PID>
```

### Очистка и перезапуск
```bash
# Остановить все
docker-compose down

# Удалить volumes
docker-compose down -v

# Удалить образы
docker-compose down --rmi all

# Запустить заново
docker-compose up -d --build
```

## 🔒 Production готовность

### Что настроено
✅ Multi-stage build для фронтенда (оптимизация размера)
✅ Nginx для production
✅ Gzip сжатие
✅ Кэширование статики
✅ Security headers
✅ Health checks для БД
✅ Зависимости между сервисами

### Для production дополнительно нужно
- [ ] Настроить HTTPS (Let's Encrypt)
- [ ] Использовать секреты вместо .env
- [ ] Настроить reverse proxy (Traefik/Nginx Proxy)
- [ ] Добавить мониторинг (Prometheus/Grafana)
- [ ] Настроить backup БД
- [ ] Использовать Docker Swarm или Kubernetes

## 📊 Ресурсы

### Размеры образов
- Frontend: ~50 MB (Nginx Alpine + статика)
- Backend: ~200-300 MB (JDK + приложение)
- Database: ~150 MB (PostgreSQL)

### Использование памяти
- Frontend: ~10-20 MB
- Backend: ~512 MB - 1 GB
- Database: ~100-200 MB

## 🎯 Тестирование Docker окружения

### 1. Запустите все сервисы
```bash
docker-compose up -d --build
```

### 2. Подождите ~30 секунд (инициализация БД и backend)

### 3. Откройте браузер
```
http://localhost
```

### 4. Зарегистрируйте агента
```
Имя: Docker Test
Email: docker@test.com
Телефон: +79991234567
Пароль: TestPass123
```

### 5. Проверьте логи
```bash
# Убедитесь что нет ошибок
docker-compose logs -f
```

## ✅ Готово!

Теперь весь Rental Manager можно запустить одной командой:

```bash
docker-compose up -d --build
```

И открыть в браузере: **http://localhost**

Всё работает в изолированных контейнерах с автоматическим проксированием API запросов! 🎉
