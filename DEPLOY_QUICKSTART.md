# 🚀 Быстрый старт деплоя

## Настройка GitHub Secrets (один раз)

1. GitHub репозиторий → Settings → Secrets and variables → Actions
2. Добавьте 3 секрета:
   - `SERVER_IP` = `74.119.193.196`
   - `SERVER_USER` = `root`
   - `SERVER_PASSWORD` = `Nhy2qV9hOF5h` (смените пароль на сервере и обновите этот секрет!)

## Подготовка сервера (один раз)

```bash
# Подключитесь к серверу
ssh root@74.119.193.196

# Установите необходимое ПО
apt update && apt install -y docker.io docker-compose git

# Клонируйте репозиторий
cd /root
git clone https://github.com/IlmiraRyakhimova/rentalmanager-main.git
cd rentalmanager-main
git checkout deploy

# Создайте .env файл (скопируйте из .env.example и заполните)
cp .env.example .env
nano .env

# Запустите первый деплой
chmod +x deploy.sh
./deploy.sh
```

## Использование

После настройки просто пушьте в ветку `deploy`:

```bash
git add .
git commit -m "Ваши изменения"
git push origin deploy
```

Деплой произойдёт автоматически! 🎉

## Доступ к приложению

- Frontend: http://74.119.193.196:3000
- Backend: http://74.119.193.196:8080

## База данных защищена! ✅

- Данные БД хранятся в Docker volume
- При обновлении контейнеров данные НЕ удаляются
- Volume: `postgres-data`

Подробнее: см. `DEPLOY_SETUP.md`
