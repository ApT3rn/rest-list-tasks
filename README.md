## REST-приложение "To do list"

Приложение имеет возможность регистрации и авторизации пользователей, выдавая доступ по JWT токену. Также хранит список задач.

## HTTP-запросы

Запросы доступные всем:
- POST /api/v1/users/register - регистрация пользователя, принимает в тело запроса (username, password);
- POST /api/v1/users/authenticate - авторизировать пользователя, принимает в тело запроса (username, password);
- POST /api/v1/users/token - получить access токен, принимает в тело запроса (refreshToken);
- POST /api/v1/users/refresh - обновить refresh токен и получить его вместе с новым access токеном, принимает в тело запроса (refreshToken).

Запросы для авторизированных пользователей:
- GET /api/v1/tasks - возвращает список всех доступных задач;
- POST /api/v1/tasks - добавляет новую задачу, принимает в тело запроса (details);
- GET /api/v1/tasks/{id} - возвращает задачу с переданным id в запросе;
- POST /api/v1/tasks/{id} - изменяет задачу с переданным id в запросе, принимает в тело запроса (details);
- GET /api/v1/tasks/created - возвращает список всех задач созданных пользователем;
- GET /api/v1/tasks/progress - возвращает список всех задач взятых в работу пользователем;
- GET /api/v1/tasks/completed - возращает список всех задач завершенных пользователем;
- POST /api/v1/tasks/{id}/progress - позволяет пользователю взять задачу в работу с переданным id в запросе;
- POST /api/v1/tasks/{id}/complete - завершает задачу с переданным id в запросе.

## Стек:
Kotlin, Spring Framework (Boot, Web, Security, Data), NoSQL (MongoDB, Redis), jwt

## Инструкция для запуска:

Перейдите по пути /src/main/resources/, после чего откройте файл application.properties 
и заполните свои значения.
