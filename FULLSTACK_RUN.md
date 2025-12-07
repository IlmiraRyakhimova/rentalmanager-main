# 🚀 Rental Manager - Полный запуск приложения

## Предварительные требования

### Backend
- Java 17+
- Maven
- PostgreSQL

### Frontend
- Node.js >= 20.19.0
- npm

## Запуск Backend

```bash
# Перейти в директорию бэкенда
cd main-service

# Запустить приложение (Maven автоматически скачает зависимости)
./mvnw spring-boot:run

# Или для Windows
mvnw.cmd spring-boot:run
```

Backend будет доступен на: **http://localhost:8080**

### Проверка API
```bash
# Проверить что backend запущен
curl http://localhost:8080/api/auth/sign-up
```

## Запуск Frontend

```bash
# Перейти в директорию фронтенда
cd front

# Установить зависимости (только первый раз)
npm install

# Запустить dev сервер
npm run dev
```

Frontend будет доступен на: **http://localhost:5173**

## Полный цикл тестирования

### 1. Откройте браузер
Перейдите на http://localhost:5173

### 2. Регистрация нового агента
- Нажмите "Зарегистрироваться"
- Заполните форму:
  ```
  Имя: Иван Петров
  Email: ivan@agent.com
  Телефон: +79991234567
  Пароль: TestPass123
  Подтверждение: TestPass123
  ```
- Нажмите "Зарегистрироваться"
- Вы будете перенаправлены на Dashboard

### 3. Выход и повторный вход
- Нажмите "Выйти" в Dashboard
- Нажмите "Войти как агент" на главной странице
- Введите:
  ```
  Email: ivan@agent.com
  Пароль: TestPass123
  ```
- Вы снова попадете в Dashboard

## Структура проекта

```
rentalmanager-main/
├── main-service/          # Backend (Spring Boot)
│   ├── src/
│   ├── pom.xml
│   └── mvnw
├── front/                 # Frontend (Vue 3)
│   ├── src/
│   ├── package.json
│   └── .env
└── docker-compose.yml     # Docker конфигурация
```

## API Endpoints

### Аутентификация (не требуют токена)
```
POST /api/auth/sign-up      - Регистрация
POST /api/auth/sign-in      - Вход
POST /api/auth/refresh-token - Обновление токена
```

### Защищенные endpoints (требуют Bearer token)
```
GET  /api/users             - Список пользователей
POST /api/users             - Создать пользователя
GET  /api/apartments        - Список квартир
POST /api/bookings          - Создать бронирование
```

## Environment Variables

### Frontend (.env)
```env
VITE_API_URL=http://localhost:8080
```

### Backend (application.yaml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/rental_db
    username: postgres
    password: your_password
```

## Troubleshooting

### Frontend не подключается к Backend
1. Проверьте что Backend запущен на порту 8080
2. Проверьте `.env` файл во frontend
3. Проверьте CORS настройки в Backend

### Ошибка CORS
Убедитесь что в `SecurityConfig.java` разрешены запросы с localhost:5173:
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:5173");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    return source;
}
```

### База данных
Создайте БД если её нет:
```sql
CREATE DATABASE rental_db;
```

## Docker (опционально)

```bash
# Запустить все сервисы
docker-compose up -d

# Остановить
docker-compose down
```

## Production Build

### Frontend
```bash
cd front
npm run build

# Файлы будут в front/dist/
```

### Backend
```bash
cd main-service
./mvnw clean package

# JAR файл будет в target/
```

## Полезные команды

### Frontend
```bash
npm run dev      # Разработка
npm run build    # Production сборка
npm run preview  # Просмотр production
npm run lint     # Проверка кода
```

### Backend
```bash
./mvnw spring-boot:run    # Запуск
./mvnw clean package      # Сборка JAR
./mvnw test              # Тесты
```

## Порты

- **Frontend**: 5173
- **Backend**: 8080
- **Database**: 5432

## Логи

### Frontend
Логи в браузерной консоли (F12 → Console)

### Backend
Логи в терминале где запущен Spring Boot

## Готово! 🎉

Теперь у вас запущен полный стек:
- ✅ Vue 3 Frontend на порту 5173
- ✅ Spring Boot Backend на порту 8080
- ✅ PostgreSQL Database на порту 5432

Можете тестировать регистрацию и вход агентов!
