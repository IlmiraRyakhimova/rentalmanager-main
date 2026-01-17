# ✅ Автоматический деплой настроен!

## 🎉 Что готово

Для вашего проекта настроен полноценный автоматический деплой на VPS сервер:

- ✅ **GitHub Actions workflow** - деплой при push в ветку `deploy`
- ✅ **Скрипты управления** - deploy.sh, status.sh, logs.sh, backup-db.sh
- ✅ **Защита базы данных** - данные сохраняются при обновлениях
- ✅ **Полная документация** - 8 файлов с инструкциями

---

## 🚀 Что делать дальше

### Шаг 1: Настройте GitHub Secrets (2 минуты)

1. Откройте ваш репозиторий на GitHub: https://github.com/IlmiraRyakhimova/rentalmanager-main
2. Перейдите в **Settings** → **Secrets and variables** → **Actions**
3. Нажмите **"New repository secret"** и добавьте:

| Имя | Значение |
|-----|----------|
| `SERVER_IP` | `74.119.193.196` |
| `SERVER_USER` | `root` |
| `SERVER_PASSWORD` | `Nhy2qV9hOF5h` |

### Шаг 2: Подготовьте сервер (10 минут)

Подключитесь к серверу и выполните команды:

```bash
# Подключение
ssh root@74.119.193.196
# Пароль: Nhy2qV9hOF5h

# Установка Docker
apt update && apt install -y docker.io docker-compose git

# Клонирование проекта
cd /root
git clone https://github.com/IlmiraRyakhimova/rentalmanager-main.git
cd rentalmanager-main
git checkout deploy

# Создание .env файла
cp .env.example .env
nano .env
# Заполните все переменные и сохраните (Ctrl+O, Enter, Ctrl+X)

# Первый запуск
chmod +x *.sh
./deploy.sh
```

### Шаг 3: Проверьте работу

После успешного деплоя откройте в браузере:
- **Frontend:** http://74.119.193.196:3000
- **Backend:** http://74.119.193.196:8080

### Шаг 4: Тестовый деплой

```bash
# На вашем компьютере
git add .
git commit -m "Test auto deploy"
git push origin deploy

# Проверьте GitHub Actions: репозиторий → вкладка Actions
```

---

## 📚 Документация

Вся документация находится в корне проекта:

### Для начала работы:
- **[DEPLOY_INDEX.md](DEPLOY_INDEX.md)** - Полный указатель всей документации
- **[DEPLOY_QUICKSTART.md](DEPLOY_QUICKSTART.md)** - Быстрый старт (5 минут)
- **[DEPLOY_CHECKLIST.md](DEPLOY_CHECKLIST.md)** - Чеклист из 69 пунктов

### Подробные инструкции:
- **[DEPLOY_SETUP.md](DEPLOY_SETUP.md)** - Полная инструкция по настройке
- **[DEPLOY_DIAGRAM.md](DEPLOY_DIAGRAM.md)** - Визуальные схемы процесса
- **[GITHUB_SECRETS.md](GITHUB_SECRETS.md)** - Настройка секретов GitHub
- **[SERVER_COMMANDS.md](SERVER_COMMANDS.md)** - Все команды для сервера

### Справочники:
- **[DEPLOY_README.md](DEPLOY_README.md)** - Оглавление деплоя
- **[DEPLOY_SUMMARY.md](DEPLOY_SUMMARY.md)** - Краткое резюме

---

## 🛠️ Скрипты на сервере

После настройки доступны команды:

```bash
./deploy.sh      # Деплой приложения
./status.sh      # Проверка состояния всех сервисов
./logs.sh        # Просмотр логов (./logs.sh backend/frontend/db)
./backup-db.sh   # Создание бэкапа базы данных
```

---

## 🔐 Защита базы данных

**ВАЖНО:** База данных полностью защищена!

- ✅ Используется Docker volume `postgres-data`
- ✅ Данные НЕ удаляются при `docker-compose down`
- ✅ Все обновления происходят БЕЗ потери данных
- ✅ Данные сохраняются между деплоями

---

## 🎯 Рекомендации

### После первого входа на сервер:

1. **Смените пароль:**
   ```bash
   passwd
   ```

2. **Обновите секрет в GitHub:**
   - Settings → Secrets → `SERVER_PASSWORD` → Update

3. **Настройте firewall (опционально):**
   ```bash
   ufw allow 22/tcp
   ufw allow 3000/tcp
   ufw allow 8080/tcp
   ufw enable
   ```

4. **Настройте автоматические бэкапы:**
   ```bash
   crontab -e
   # Добавьте: 0 2 * * * cd /root/rentalmanager-main && ./backup-db.sh
   ```

---

## 📊 Как это работает

```
git push origin deploy
      ↓
GitHub Actions
      ↓
SSH → Сервер
      ↓
git pull
      ↓
docker-compose down    (контейнеры удаляются, volumes остаются!)
      ↓
docker-compose build   (пересборка образов)
      ↓
docker-compose up -d   (запуск с теми же данными БД)
      ↓
✅ Деплой завершен!
```

---

## 🆘 Если что-то не работает

1. **Проверьте GitHub Actions:**
   - Репозиторий → Actions → последний workflow
   - Посмотрите логи

2. **На сервере проверьте логи:**
   ```bash
   ssh root@74.119.193.196
   cd /root/rentalmanager-main
   ./logs.sh
   ```

3. **Проверьте статус:**
   ```bash
   ./status.sh
   ```

4. **Посмотрите шпаргалку:**
   - [SERVER_COMMANDS.md](SERVER_COMMANDS.md)

---

## ✨ Что дальше

После настройки вам нужно только:

```bash
git add .
git commit -m "Ваши изменения"
git push origin deploy
```

**Всё остальное произойдёт автоматически!** 🚀

---

## 📝 Создано

- 📂 `.github/workflows/deploy.yml` - GitHub Actions
- 🔧 `deploy.sh` - скрипт деплоя
- 📊 `status.sh` - проверка статуса
- 📝 `logs.sh` - просмотр логов  
- 💾 `backup-db.sh` - бэкап БД
- 📄 `.env.example` - пример конфигурации
- 📚 8 файлов документации

**Всё готово к работе!** 🎉

---

**Информация о сервере:**
- IP: 74.119.193.196
- User: root
- Password: Nhy2qV9hOF5h (смените после первого входа!)
- Frontend: http://74.119.193.196:3000
- Backend: http://74.119.193.196:8080
