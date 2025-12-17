# Отладка проблемы с токенами

## Проблема
403 Forbidden при запросах к API, хотя токен есть в localStorage.

## Диагностика

### Шаг 1: Очистите localStorage
Откройте консоль браузера (F12) и выполните:
```javascript
localStorage.clear()
console.log('localStorage cleared')
```

### Шаг 2: Перезагрузите страницу
Нажмите F5 или Ctrl+R

### Шаг 3: Войдите в систему
После входа в консоли должны появиться логи:

```
[Auth] Login response: { fullData: {...}, accessToken: "eyJ...", refreshToken: "eyJ...", ... }
[Auth] saveAuthData called with: { accessToken: "eyJ...", refreshToken: "eyJ...", ... }
[Auth] Saving to localStorage: { accessToken: "eyJ...", refreshToken: "eyJ...", user: {...} }
[Auth] Saved to localStorage. Verifying...
[Auth] localStorage accessToken: eyJhbGciOiJIUzI1NiJ9...
[Auth] localStorage refreshToken: eyJhbGciOiJIUzI1NiJ9...
[Auth] After save: { accessToken: "eyJhbGciOiJIUzI1NiJ9...", ... }
```

### Шаг 4: Проверьте localStorage вручную
В консоли выполните:
```javascript
console.log('accessToken:', localStorage.getItem('accessToken'))
console.log('refreshToken:', localStorage.getItem('refreshToken'))
console.log('user:', localStorage.getItem('user'))
```

### Шаг 5: Проверьте запрос к API
После попытки загрузить апартаменты должен появиться лог:
```
[API Request] GET /api/apartments { hasToken: true, tokenPreview: "eyJhbGciOiJIUzI1NiJ9..." }
```

## Ожидаемые результаты

✅ **Правильно:**
- `tokenPreview: "eyJhbGciOiJIUzI1NiJ9..."` (начинается с "eyJ")
- `hasToken: true`
- Токен НЕ равен `"null"` или `"undefined"`

❌ **Неправильно:**
- `tokenPreview: "null..."` — токен сохранился как строка "null"
- `tokenPreview: "undefined..."` — токен сохранился как строка "undefined"
- `hasToken: false` — токен не найден

## Возможные причины 403 ошибки

1. **Токен сохраняется как "null"** — проблема в `saveAuthData()`
2. **Токен не валидный** — проблема в бэкенде (JWT генерация)
3. **Токен истёк** — нужно обновить через refresh token
4. **CORS проблема** — токен не отправляется в заголовках
5. **Неправильный формат токена** — должен быть `Bearer eyJ...`

## Если проблема сохраняется

Проверьте в Network вкладке браузера (F12 → Network):
1. Найдите запрос к `/api/apartments`
2. Откройте вкладку "Headers"
3. Проверьте `Authorization` заголовок
4. Должно быть: `Bearer eyJhbGciOiJIUzI1NiJ9...`

Если заголовок отсутствует или неправильный — проблема в `axios.js` интерцепторе.

