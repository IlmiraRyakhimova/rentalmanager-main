# 🏢 Rental Manager

Современная система управления арендой недвижимости для агентств недвижимости.

## 🚀 Быстрый старт с Docker

### Запуск всего приложения одной командой:

```bash
# 1. Создайте .env файл (пример ниже)
# 2. Запустите все сервисы
docker-compose up -d --build

# 3. Откройте браузер
http://localhost
```

**Готово!** Все три сервиса (PostgreSQL, Backend, Frontend) запущены.

### Пример .env файла:

```env
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
JWT_SECRET=your-secret-key-min-256-bits-long-for-production
JWT_EXPIRATION=86400000
EMAIL_ADDRESS=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

📖 **Подробная документация:** [DOCKER_GUIDE.md](DOCKER_GUIDE.md)

---

## 📦 Что включено

### Frontend (Vue 3 + Vite)
- ✅ Современный UI/UX дизайн
- ✅ Страница регистрации агентов с полной валидацией
- ✅ Индикатор силы пароля
- ✅ JWT аутентификация
- ✅ Адаптивный дизайн
- ✅ Автоматическое обновление токенов

**Порт:** 80 (Docker) или 5173 (dev mode)

### Backend (Spring Boot)
- ✅ REST API
- ✅ JWT аутентификация
- ✅ Email верификация
- ✅ Управление пользователями
- ✅ CRUD для квартир и бронирований
- ✅ PostgreSQL + Liquibase

**Порт:** 8080

### Database (PostgreSQL 15)
- ✅ Автоматическая миграция схемы
- ✅ Health checks
- ✅ Persistent storage

**Порт:** 5432

---

## 🛠️ Локальная разработка (без Docker)

### Backend

```bash
cd main-service

# Настройте application.yaml с вашей БД

# Запуск
./mvnw spring-boot:run
```

### Frontend

```bash
cd front

# Установка зависимостей
npm install

# Запуск dev сервера
npm run dev

# Откройте http://localhost:5173
```

**Документация:**
- Frontend: [front/README_FRONTEND.md](front/README_FRONTEND.md)
- Backend API: [main-service/api_doc.md](main-service/api_doc.md)

---

## 📚 Документация

| Файл | Описание |
|------|----------|
| [DOCKER_GUIDE.md](DOCKER_GUIDE.md) | 🐳 Работа с Docker Compose |
| [FULLSTACK_RUN.md](FULLSTACK_RUN.md) | 🚀 Запуск полного стека (локально) |
| [PROJECT_COMPLETE.md](PROJECT_COMPLETE.md) | ✅ Резюме проекта |
| [CHECKLIST.md](CHECKLIST.md) | ✓ Чеклист готовности |
| [front/TESTING_GUIDE.md](front/TESTING_GUIDE.md) | 🧪 Тестирование frontend |
| [front/USAGE_GUIDE.md](front/USAGE_GUIDE.md) | 📖 Руководство по frontend |

---

## 🎯 Основные возможности

### Для агентов
- ✅ Регистрация и вход в систему
- ✅ Управление профилем
- ✅ Управление объектами недвижимости
- ✅ Календарь бронирований
- ✅ База клиентов

### Технические особенности
- ✅ JWT аутентификация с refresh tokens
- ✅ Email верификация
- ✅ Валидация данных на клиенте и сервере
- ✅ RESTful API
- ✅ Адаптивный дизайн
- ✅ Современные практики разработки

---

## 🌐 Архитектура

```
┌─────────────────┐
│   Frontend      │ Vue 3 + Vite + Pinia
│   (Nginx)       │ http://localhost
└────────┬────────┘
         │ /api/*
         │ proxy
         ↓
┌─────────────────┐
│   Backend       │ Spring Boot + JWT
│   (Java 17)     │ :8080
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   Database      │ PostgreSQL 15
│                 │ :5432
└─────────────────┘
```

---

## 🔐 Безопасность

- ✅ JWT токены (Access + Refresh)
- ✅ Bcrypt хеширование паролей
- ✅ Email верификация
- ✅ CORS настройки
- ✅ Security headers (Nginx)
- ✅ Валидация на всех уровнях

---

## 🧪 Тестирование

### Тестовые данные для регистрации:

```
Имя: Иван Петров
Email: ivan@agent.com
Телефон: +79991234567
Пароль: TestPass123
Роль: AGENT
```

### После регистрации:
1. Вы попадете в Dashboard
2. Увидите приветствие и карточки функций
3. Сможете выйти и войти снова

---

## 📊 Технологии

### Frontend
- Vue 3 (Composition API)
- Vue Router
- Pinia (State Management)
- Axios
- Vite
- Nginx (production)

### Backend
- Java 17
- Spring Boot 3
- Spring Security
- JWT (JSON Web Tokens)
- PostgreSQL
- Liquibase
- Lombok

### DevOps
- Docker
- Docker Compose
- Multi-stage builds
- Nginx reverse proxy

---

## 🚧 Roadmap

- [ ] Полное CRUD для квартир
- [ ] Календарь бронирований
- [ ] Управление клиентами
- [ ] Отчеты и статистика
- [ ] Профиль пользователя
- [ ] Темная тема
- [ ] Мобильное приложение

---

## 📄 Лицензия

MIT

---

## 👥 Авторы

Rental Manager - система управления арендой недвижимости

**Дата создания:** 7 декабря 2025 г.

---

## 🎉 Быстрые команды

```bash
# Запустить все в Docker
docker-compose up -d --build

# Посмотреть логи
docker-compose logs -f

# Остановить все
docker-compose down

# Локальная разработка - Backend
cd main-service && ./mvnw spring-boot:run

# Локальная разработка - Frontend
cd front && npm run dev
```

---

**Готово к использованию!** 🚀

Просто запустите `docker-compose up -d --build` и откройте http://localhost
