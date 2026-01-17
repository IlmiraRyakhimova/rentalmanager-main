# 🎯 Автоматический деплой - Оглавление

## 📚 Документация

Вся документация по деплою и управлению сервером:

### Быстрый старт
1. **[DEPLOY_QUICKSTART.md](DEPLOY_QUICKSTART.md)** - Быстрая настройка деплоя (5 минут)
2. **[DEPLOY_SETUP.md](DEPLOY_SETUP.md)** - Полная инструкция по настройке

### Справочники
3. **[GITHUB_SECRETS.md](GITHUB_SECRETS.md)** - Настройка секретов GitHub
4. **[SERVER_COMMANDS.md](SERVER_COMMANDS.md)** - Шпаргалка команд для сервера

## 🛠️ Скрипты

В корне проекта доступны следующие скрипты:

| Скрипт | Описание | Использование |
|--------|----------|---------------|
| `deploy.sh` | Деплой приложения | `./deploy.sh` |
| `status.sh` | Проверка состояния сервисов | `./status.sh` |
| `logs.sh` | Просмотр логов | `./logs.sh [backend\|frontend\|db]` |
| `backup-db.sh` | Создание бэкапа БД | `./backup-db.sh` |

## 🚀 Как это работает

1. **Разработчик** делает изменения и пушит в ветку `deploy`
2. **GitHub Actions** автоматически:
   - Подключается к серверу по SSH
   - Загружает последние изменения
   - Запускает скрипт деплоя
3. **Скрипт деплоя** на сервере:
   - Останавливает контейнеры
   - Пересобирает Docker образы
   - Запускает обновленные контейнеры
   - **Сохраняет данные БД** в volume

## ✅ Гарантии безопасности

- ✅ База данных в Docker volume `postgres-data`
- ✅ Redis данные в Docker volume `redis-data`
- ✅ Volumes НЕ удаляются при `docker-compose down`
- ✅ Автоматические бэкапы БД (скрипт `backup-db.sh`)
- ✅ Логи доступны в любой момент

## 🎬 Последовательность действий

### Первая настройка (один раз):

1. Настроить GitHub Secrets ([GITHUB_SECRETS.md](GITHUB_SECRETS.md))
2. Подготовить сервер ([DEPLOY_SETUP.md](DEPLOY_SETUP.md))
3. Создать `.env` файл на сервере
4. Запустить первый деплой

### Ежедневная работа:

```bash
# Локально:
git add .
git commit -m "Описание изменений"
git push origin deploy

# GitHub Actions автоматически задеплоит на сервер!
```

### Мониторинг (на сервере):

```bash
ssh root@74.119.193.196
cd /root/rentalmanager-main
./status.sh  # Проверить состояние
./logs.sh    # Посмотреть логи
```

## 🌐 Доступ к приложению

После успешного деплоя:

- **Frontend:** http://74.119.193.196:3000
- **Backend:** http://74.119.193.196:8080

## 🆘 Поддержка

Если что-то пошло не так:

1. Проверьте логи GitHub Actions
2. Подключитесь к серверу и проверьте логи: `./logs.sh`
3. Проверьте статус: `./status.sh`
4. См. [SERVER_COMMANDS.md](SERVER_COMMANDS.md) для диагностики

## 📂 Структура файлов деплоя

```
rentalmanager-main/
├── .github/
│   └── workflows/
│       └── deploy.yml          # GitHub Actions workflow
├── deploy.sh                   # Скрипт деплоя на сервере
├── backup-db.sh                # Скрипт бэкапа БД
├── logs.sh                     # Скрипт просмотра логов
├── status.sh                   # Скрипт проверки состояния
├── .env.example                # Пример переменных окружения
├── docker-compose.yml          # Конфигурация Docker
├── DEPLOY_QUICKSTART.md        # Быстрый старт
├── DEPLOY_SETUP.md             # Полная инструкция
├── GITHUB_SECRETS.md           # Настройка секретов
└── SERVER_COMMANDS.md          # Шпаргалка команд
```

## 🔐 Важные файлы

### На сервере должны быть:
- `.env` - переменные окружения (НЕ коммитить!)
- Данные БД в Docker volume `postgres-data`

### В GitHub должны быть настроены:
- Secret: `SERVER_IP`
- Secret: `SERVER_USER`
- Secret: `SERVER_PASSWORD`

---

**Готово!** Теперь деплой происходит автоматически при каждом push в ветку `deploy`. 🎉
