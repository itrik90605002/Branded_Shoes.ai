# Production Deployment Guide

This guide describes how to deploy the **Branded Shoe Store AI Agent** web application in different environments.

---

## 1. Multi-Profile Environment Configuration
The backend application supports two runtime profiles configured in `src/main/resources`:
* **Development (`dev`)**: Uses an in-memory H2 database. Tables are automatically dropped and re-created using `schema.sql` and `data.sql` on startup. Perfect for zero-config testing.
* **Production (`prod`)**: Uses a MySQL 8 database. Configurations are driven by environment variables to prevent credentials exposure.

---

## 2. Option A: Docker Compose Deployment (Recommended)
Docker Compose compiles the backend code, packages it inside a lightweight JDK container, and sets up a MySQL 8 database instance with volume persistence.

### Setup Steps:
1. Ensure **Docker Desktop** is installed and running on your machine.
2. Open a terminal (PowerShell, Command Prompt, or Bash) in the project root: `D:\Intern_Projects\AGENTIC_AI_PROJECT_MANDELBULB`.
3. Open the `docker-compose.yml` file and insert your Groq API Key under `GROQ_API_KEY` (or configure email credentials).
4. Build and start the containers using:
   ```bash
   docker-compose up -d --build
   ```
5. Docker will build the JAR, start the MySQL database container (`shoestore-mysql`), wait until it is healthy, and launch the Spring Boot app container (`shoestore-agent-backend`).
6. The APIs will be exposed on **`http://localhost:8080`**.
7. Open `frontend/index.html` in your browser to interact with the project.

---

## 3. Option B: Local Production (Manual Run)
To run the production profile locally with a pre-installed MySQL database:

1. Create a MySQL database named `online_store`:
   ```sql
   CREATE DATABASE online_store;
   ```
2. Set the environment variables in your command prompt:
   - **Windows (Command Prompt)**:
     ```cmd
     set DB_HOST=localhost
     set DB_PORT=3306
     set DB_NAME=online_store
     set DB_USER=your_mysql_user
     set DB_PASSWORD=your_mysql_password
     set JWT_SECRET=some_long_secure_custom_secret_key_base64_encoded
     set GROQ_API_KEY=your_groq_api_key
     ```
   - **Windows (PowerShell)**:
     ```powershell
     $env:DB_HOST="localhost"
     $env:DB_PORT="3306"
     $env:DB_NAME="online_store"
     $env:DB_USER="your_mysql_user"
     $env:DB_PASSWORD="your_mysql_password"
     $env:JWT_SECRET="some_long_secure_custom_secret_key_base64_encoded"
     $env:GROQ_API_KEY="your_groq_api_key"
     ```
3. Run the Spring Boot application using Maven:
   ```bash
   cd backend
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

---

## 4. Frontend Production Hosting
Because the frontend is built using static client assets (HTML, Vanilla CSS, and JavaScript), it can be deployed for **free, indefinitely** on high-speed static hosting networks:

* **Netlify**: Drop the `frontend` folder into the Netlify dashboard. Done.
* **Vercel**: Run `vercel` inside the `frontend` folder.
* **GitHub Pages**: Upload the `frontend` folder as a repository and enable GitHub Pages in settings.

> [!IMPORTANT]  
> If you deploy the frontend and backend to different URLs (e.g., frontend on Netlify, backend on Render/AWS), make sure to open `frontend/js/app.js` and change the `API_BASE_URL` constant from `http://localhost:8080` to your deployed backend URL.
