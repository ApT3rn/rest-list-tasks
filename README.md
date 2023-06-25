## REST-приложение "To do list"

Приложение имеет возможность регистрации и авторизации пользователей, выдавая доступ по JWT токену. Также хранит список задач и принимает HTTP запросы:

- POST /api/v1/users/register - регистрация пользователя
- POST /api/v1/users/authenticate - авторизировать пользователя
- POST /api/v1/users/token - получить access токен
- POST /api/v1/users/refresh - обновить refresh токен и получить его вместе с новым access токеном
