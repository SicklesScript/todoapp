# SicklesScript

SicklesScript is a simple Java blog app with CRUD functionality and a green terminal-inspired design.

## Stack

- Java 17
- Spring Boot (Web + Thymeleaf)
- Spring Data JPA + H2 database

## Features

- Create blog posts
- Read/list all posts
- View a single post
- Update existing posts
- Delete posts
- Basic validation for title/content
- Persistent storage in H2 (file-based)

## Project Structure

- `src/main/java/com/sicklesscript/blog` - Java source code
- `src/main/resources/templates` - Thymeleaf views
- `src/main/resources/static/styles.css` - Terminal-green styling
- `pom.xml` - Maven build configuration

## Run Locally

From the project root:

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080/posts](http://localhost:8080/posts).

## Deploy to Render (Free)

### 1) Push this repo to GitHub

Render deploys from a Git repo.

### 2) Create a new Blueprint service on Render

1. In Render, click **New** -> **Blueprint**
2. Connect your GitHub repo
3. Render will detect `render.yaml`
4. Click **Apply**

When deploy finishes, open:

- `https://<your-render-service>.onrender.com/posts`

### Important note about free persistence

This app defaults to H2 file storage. On Render free instances, filesystem persistence is limited, so data may reset after restarts/redeploys.

For durable production data, set PostgreSQL env vars on Render:

- `JDBC_DATABASE_URL`
- `JDBC_DATABASE_DRIVER=org.postgresql.Driver`
- `JDBC_DATABASE_USERNAME`
- `JDBC_DATABASE_PASSWORD`
- `JPA_DDL_AUTO=update`
- `H2_CONSOLE_ENABLED=false`

## Deploy to Azure App Service

### 1) Install and sign in to Azure CLI

```bash
brew install azure-cli
az login
```

### 2) Deploy

Use the helper script:

```bash
./deploy-azure-appservice.sh <resource-group> <app-name> <region> [plan-name]
```

Example:

```bash
./deploy-azure-appservice.sh sickles-rg sicklesscript-blog eastus sickles-plan
```

Then open:

- `https://<app-name>.azurewebsites.net/posts`

### 3) (Recommended) Move to PostgreSQL on Azure

The app is already ready for PostgreSQL through environment variables.  
Set these app settings in Azure App Service:

- `JDBC_DATABASE_URL` (for example `jdbc:postgresql://<host>:5432/<db>?sslmode=require`)
- `JDBC_DATABASE_DRIVER=org.postgresql.Driver`
- `JDBC_DATABASE_USERNAME`
- `JDBC_DATABASE_PASSWORD`
- `JPA_DDL_AUTO=update`
- `H2_CONSOLE_ENABLED=false`

Without these settings, the app defaults to local H2 file storage.

## Next Simple Upgrade Ideas

- Switch from H2 to PostgreSQL/MySQL
- Add author field and timestamps in UI
- Add search/filter on the posts list
- Add API endpoints (`/api/posts`) for frontend integration
