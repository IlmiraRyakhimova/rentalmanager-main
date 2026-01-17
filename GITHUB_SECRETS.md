# GitHub Secrets Configuration

Этот файл содержит список всех необходимых секретов для GitHub Actions.
**НЕ вводите реальные значения в этот файл!** Добавьте их в GitHub Settings.

## Как добавить секреты в GitHub:

1. Перейдите в ваш репозиторий на GitHub
2. Settings → Secrets and variables → Actions
3. Нажмите "New repository secret"
4. Добавьте каждый секрет из списка ниже

## Обязательные секреты для деплоя:

### SERVER_IP
- **Описание:** IP адрес вашего VPS сервера
- **Текущее значение:** 74.119.193.196
- **Где используется:** GitHub Actions для подключения к серверу

### SERVER_USER
- **Описание:** Имя пользователя для SSH подключения
- **Текущее значение:** root
- **Где используется:** GitHub Actions для подключения к серверу

### SERVER_PASSWORD
- **Описание:** Пароль для SSH подключения
- **Текущее значение:** Nhy2qV9hOF5h (СМЕНИТЕ ПОСЛЕ ПЕРВОГО ВХОДА!)
- **Где используется:** GitHub Actions для подключения к серверу
- **⚠️ ВАЖНО:** После смены пароля на сервере обновите этот секрет!

## Рекомендация по безопасности:

Вместо пароля лучше использовать SSH ключи. Для этого:

1. Создайте SSH ключ на сервере
2. Используйте секрет `SERVER_SSH_KEY` вместо `SERVER_PASSWORD`
3. Обновите `.github/workflows/deploy.yml`:
   ```yaml
   with:
     host: ${{ secrets.SERVER_IP }}
     username: ${{ secrets.SERVER_USER }}
     key: ${{ secrets.SERVER_SSH_KEY }}
   ```

## Проверка секретов:

После добавления всех секретов в GitHub:
1. Перейдите в Settings → Secrets and variables → Actions
2. Вы должны увидеть 3 секрета:
   - SERVER_IP
   - SERVER_USER
   - SERVER_PASSWORD (или SERVER_SSH_KEY)
