# Spring Framework Application

Это корпоративное приложение демонстрирует современные подходы разработки с использованием Spring Boot. Приложение реализует RESTful API для управления продуктами и торговыми периодами, обеспечивает безопасность с помощью JWT, применяет аспектно-ориентированное программирование для сквозного логирования и построено по принципам инверсии управления, внедрения зависимостей и модульности.

---

## Содержание

1. [Введение](#1-введение)
2. [Функциональные возможности](#2-функциональные-возможности)
3. [Архитектура приложения](#3-архитектура-приложения)
4. [Стек технологий](#4-стек-технологий)
5. [Установка и запуск](#5-установка-и-запуск)
6. [Конфигурация приложения](#6-конфигурация-приложения)
7. [Тестирование](#7-тестирование)
8. [Безопасность](#8-безопасность)
9. [Логирование и мониторинг](#9-логирование-и-мониторинг)
10. [Дополнительные возможности](#10-дополнительные-возможности)
11. [Контакты](#11-контакты)

---

## 1. Введение

Приложение построено на Spring Boot и демонстрирует, как создавать масштабируемые и безопасные корпоративные решения. Основные функции включают управление продуктами и торговыми периодами через RESTful API, обработку транзакций с помощью Spring Data JPA (Hibernate), JWT-аутентификацию и авторизацию, а также аспектно-ориентированное логирование.

---

## 2. Функциональные возможности

- **Управление данными:** CRUD-операции для продуктов и торговых периодов с бизнес-валидацией.
- **RESTful API:** Стандартизированные эндпоинты для интеграции с внешними системами.
- **Безопасность:** JWT-аутентификация, авторизация через Spring Security и кастомный PermissionEvaluator.
- **Логирование:** Аспектное логирование вызовов сервисов с использованием кастомной аннотации `@Loggable`.
- **Модульная архитектура:** Четкое разделение ответственности между контроллерами, сервисами, репозиториями и сущностями.
- **Конфигурация для разных сред:** Поддержка профилей `dev` и `prod` с использованием PostgreSQL.

---

## 3. Архитектура приложения

Приложение реализовано по многослойной архитектуре, что обеспечивает гибкость, масштабируемость и лёгкость сопровождения.

### 3.1. Слой данных
- **Сущности (Entity):**  
  - **Пакет:** `ru.education.entity`  
  - **Назначение:** Определение доменных объектов (Product, SalesPeriod) с маппингом на таблицы базы данных посредством JPA-аннотаций.
  - **Особенности:** Автоматическая генерация идентификаторов для торговых периодов через последовательности.

- **Репозитории (DAO):**  
  - **Пакеты:** `ru.education.jpa` (основной), `ru.education.jdbc` (пример)  
  - **Назначение:** Абстракция операций CRUD. Реализованы через расширение `JpaRepository`, что позволяет избежать написания шаблонного кода.
  - **Реализация:** Hibernate используется в качестве провайдера JPA для ORM.

### 3.2. Бизнес-логика
- **Сервисный слой:**  
  - **Пакеты:** `ru.education.service` и `ru.education.service.impl`  
  - **Назначение:** Инкапсуляция бизнес-правил (например, проверки целостности, валидации данных, управление торговыми периодами).  
  - **Примеры:**  
    - `DefaultProductService` — управление продуктами.  
    - `DefaultSalesPeriodService` — управление торговыми периодами с проверками на наличие открытых периодов и выбросом соответствующих исключений.

### 3.3. Веб-слой
- **Контроллеры:**  
  - **Пакет:** `ru.education.controllers`  
  - **Назначение:** Обработка HTTP-запросов и маршрутизация их к сервисному слою.  
  - **Особенности:** Методы контроллеров используют аннотации `@RestController`, `@RequestMapping` и `@PreAuthorize` для реализации RESTful API и контроля доступа.

### 3.4. Безопасность
- **Пакеты:** `ru.education.security` и `ru.education.jwt`  
- **Назначение:**  
  - Реализация аутентификации и авторизации через JWT (фильтр `JWTLoginFilter` и сервис `TokenAuthenticationService`).  
  - Управление правами доступа с помощью кастомного `PermissionEvaluator` (`DefaultPermissionEvaluator`) и загрузки пользовательских данных через `SecurityUserDetailsManager`.

### 3.5. Конфигурация
- **Пакет:** `ru.education.config`  
- **Назначение:** Централизованное хранение настроек (безопасность, база данных, сканирование компонентов).  
- **Особенности:** Поддержка нескольких профилей (`dev`, `prod`) для гибкой настройки среды выполнения.

### 3.6. Сквозные возможности
- **AOP:**  
  - **Пакет:** `ru.education.aspects`  
  - **Функционал:** Аспект `WebServiceLogger` перехватывает вызовы публичных методов в `ProductService`, если они помечены `@Loggable`, и логирует входящие параметры и результаты, что помогает в отладке и мониторинге.
- **Кастомные аннотации и форматтеры:**  
  - **Пакеты:** `ru.education.annotation`, `ru.education.model` и `ru.education.model.impl`  
  - **Функционал:** Определяют собственные аннотации и обеспечивают единообразное форматирование данных.

---

## 4. Стек технологий

- **Java 1.8:** Основная платформа разработки.
- **Spring Boot 2.2.2:** Быстрая конфигурация и запуск приложения.
- **Spring Boot Starter Web, Data JPA, JDBC, Security:** Поддержка REST API, работы с данными через Hibernate, обеспечения безопасности.
- **Project Lombok:** Сокращение шаблонного кода.
- **JWT (jjwt 0.7.0):** Реализация JSON Web Token для безопасности.
- **PostgreSQL:** Основная СУБД для профилей `dev` и `prod`.
- **H2 Database:** Встраиваемая БД для тестирования.
- **Maven:** Управление зависимостями и сборка проекта.
- **Spring Boot Maven Plugin:** Упрощённый запуск приложения.
- **Spring Profiles:** Настройка различных сред (dev, prod).

---

## 5. Установка и запуск

### 5.1. Предварительные требования
- **JDK 1.8** (или выше)
- **Maven**
- **IDE** (например, IntelliJ IDEA)

### 5.2. Сборка проекта
Убедитесь, что все зависимости актуальны, и выполните:
```sh
mvn clean install
```

### 5.3. Запуск приложения
Запустите приложение командой:

```sh
mvn spring-boot:run
```
Параметры конфигурации задаются в application.properties или application.yml.

### 5.4. Настройка базы данных
Приложение использует PostgreSQL для обоих профилей (dev и prod). Для инициализации базы данных выполните SQL-дампы:

```sh
psql -U postgres -h localhost -p 5432 -d dev_db -f db/dev_db_dump.sql
psql -U postgres -h localhost -p 5432 -d prod_db -f db/prod_db_dump.sql
```

## 6. Конфигурация приложения
Приложение поддерживает несколько профилей Spring (dev и prod), что позволяет адаптировать настройки для различных сред. Основные параметры (подключение к базе данных, включение логирования SQL, настройки безопасности и т.д.) определяются в application.yml.

### 6.1. Профиль dev:
Использует базу данных **dev_db**, включено логирование SQL.

### 6.2. Профиль prod:
Использует базу данных **prod_db**, SQL-логирование отключено.

## 7. Тестирование
### 7.1. Юнит-тесты:
Покрывают отдельные компоненты (сервисы, контроллеры, аспекты) с использованием JUnit и, при необходимости, Mockito для мока зависимостей.

### 7.2. Интеграционные тесты:
Используются @SpringBootTest, @ContextConfiguration и @Transactional для поднятия полного контекста Spring, а также in-memory база H2 для ускорения тестирования.

### 7.3. Mock-тестирование:
Mockito применяется для подмены зависимостей в сервисном слое, позволяя изолировать бизнес-логику.

### 7.4. Запуск тестов:
```sh
mvn clean test
```

## 8. Безопасность
### 8.1. JWT-аутентификация:
Фильтр JWTLoginFilter обрабатывает запросы на /login, а TokenAuthenticationService генерирует и проверяет JWT-токены. Токен передается клиенту в заголовке Authorization и используется для последующей авторизации.

### 8.2. Permission Evaluation:
Кастомный DefaultPermissionEvaluator и SecurityUserDetailsManager загружают данные пользователя и проверяют права доступа на основе заданных разрешений (например, product.read, product.create).

### 8.3. Настройка Spring Security:
Класс SecurityConfig управляет настройками безопасности, определяя правила доступа для REST-эндпоинтов и интегрируя JWT-фильтры.

## 9. Логирование и мониторинг
### 9.1. Аспектное логирование:
Аспект WebServiceLogger (в пакете ru.education.aspects) перехватывает вызовы методов сервиса, помеченных аннотацией @Loggable, и логирует имя метода, параметры и возвращаемое значение.

### 9.2. Настройка логгирования:
Используются **SLF4J** с Logback для удобного и конфигурируемого вывода логов.

## 10. Дополнительные возможности и расширения
### 10.1. Кастомные форматтеры:
Реализации FooFormatter и BarFormatter в пакетах ru.education.model и ru.education.model.impl обеспечивают стандартизированное форматирование данных.

### 10.2. Обработка исключений:
Приложение использует собственные исключения (например, EntityAlreadyExistsException, EntityConflictException, EntityNotFoundException) для единообразной обработки ошибок.

### 10.3. Расширяемость:
Благодаря модульной архитектуре, добавление новых функциональных возможностей или аспектов (например, аудит, валидация) происходит без нарушения существующего кода.

## 11. Контакты
### Telegram 
@kolobook146
