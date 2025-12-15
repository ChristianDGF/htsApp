htsApp

A Spring Boot application to manage users, empresas and content pages with Thymeleaf views and Tailwind CSS styling.

Quick start

Prerequisites
- Java 17+ (or the version configured in build.gradle)
- Gradle (or use the included Gradle wrapper)
- MySQL with a database named `hts`

Setup
1. Update src/main/resources/application.properties with your DB credentials.
2. Create the database if needed: CREATE DATABASE hts CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Run
- Windows: gradlew bootRun
- Linux/macOS: ./gradlew bootRun
- Or build and run jar: ./gradlew bootJar && java -jar build/libs/htsApp-<version>.jar

Useful commands
- Build: ./gradlew build
- Run tests: ./gradlew test

Notes
- Application port: 8080 (configurable in application.properties)
- Tailwind: templates use Tailwind classes. If you want a Tailwind build step, place generated CSS under src/main/resources/static/css and reference it in templates.


