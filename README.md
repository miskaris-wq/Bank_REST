# 🚀 Система управления банковскими картами

Spring Boot REST API для работы с банковскими картами.  
Проект собирается с помощью Maven и может запускаться локально или в Docker-контейнерах (приложение(app) + PostgreSQL + pgAdmin).

---

## 📑 Содержание

1. [Предварительные требования](#предварительные-требования)
2. [Функциональность](#функциональность)
3. [Структура проекта](#структура-проекта)
4. [Быстрый старт с Docker Compose](#быстрый-старт-с-docker-compose)
5. [Локальный запуск без Docker](#локальный-запуск-без-docker)
6. [Документация API](#документация-api)
7. [Учётные записи по умолчанию](#учётные-записи-по-умолчанию)
8. [Примеры запросов](#примеры-запросов)

---

## ⚙️ Предварительные требования

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Для локального запуска: **Java 17+** и **Maven 3.6+**

---

## 🧾 Функциональность

### 👤 Роли и доступ

- **ADMIN**: управление пользователями и всеми картами
- **USER**: операции только со своими картами

### 💳 Карты

- Создание и удаление (**ADMIN**)
- Блокировка и активация (**ADMIN**)
- Просмотр карт пользователя (**USER**)
- Запрос блокировки карты (**USER**)
- Баланс карты (с маскированием номера)

### 💸 Переводы

- Переводы между картами пользователя
- История переводов с пагинацией
- Проверка активного статуса карт

### 🔐 Безопасность
- Аутентификация через JWT
- Маскирование номеров карт
- Валидация запросов

---

## 📂 Структура проекта

```
Bank_REST
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── docs/
│   └── openapi.yaml
└── src/
    ├── main/
    │   ├── java/com/example/bankcards/… 
    │   └── resources/
    │       ├── application.yml
    │       └── application-dev.yml
    └── test/
        └── java/…
```

---

## 🐳 Быстрый старт с Docker Compose

1. **Клонировать репозиторий**
   ```bash
   git clone <URL_репозитория>
   cd Bank_REST
    ```
---

2. **Собрать и запустить контейнеры**

   ```bash
   docker-compose up --build
   ```

3. **Сервисы после запуска**

    * API: [http://localhost:8060](http://localhost:8060)
    * Swagger UI: [http://localhost:8060/swagger-ui.html](http://localhost:8060/swagger-ui.html)
    * PostgreSQL: порт `5432`
    * pgAdmin: [http://localhost:8050](http://localhost:8050)
      Логин: `admin@admin.com`
      Пароль: `admin`

4. **Остановка и очистка**

   ```bash
   docker-compose down -v --rmi all
   ```

---

## 💻 Локальный запуск без Docker

1. Установить:
    * Java 17+
    * Maven 3.6+
    * PostgreSQL

2. Создать пользователя:

    ```sql
   CREATE USER "BankRest" WITH PASSWORD 'BankRest';
   ```
3. Создать базу данных:

   ```sql
   CREATE DATABASE BankRest;
   ```

4. Прописать настройки подключения в `src/main/resources/application-dev.yml`.

5. Запустить приложение:

   ```bash
   ./mvnw clean package
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

---

## 📖 Документация API

* Swagger UI: [http://localhost:8060/swagger-ui.html](http://localhost:8060/swagger-ui.html)
* OpenAPI спецификация: `docs/openapi.yaml`

---

## 👥 Учётные записи по умолчанию

В систему предзагружены тестовые пользователи:

### 🔑 Admin

* Логин: `admin`
* Пароль: `admin`

### 👤 User

* Логин: `user`
* Пароль: `user`

---

## 📌 Примеры запросов

### Авторизация

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "userName": "user",
  "password": "user"
}
```

### Получение всех карт (ADMIN)

```http
GET /api/v1/cards/all
Authorization: Bearer <JWT>
```

### Пополнение карты

```http
POST /api/v1/cards/replenish/1
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "amount": 500
}
```

### Перевод между картами

```http
POST /api/v1/transfer/user/2
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "fromCardId": 1,
  "toCardId": 2,
  "amount": 100
}
```
