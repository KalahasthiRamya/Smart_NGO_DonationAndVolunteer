# Smart NGO Platform - Production & Cloud Deployment Guide

The **Smart NGO Donation and Volunteer Management Platform** is a cloud-based web application developed using Java, Spring Boot, Spring MVC, Hibernate/JPA, MySQL, Bootstrap, and Chart.js. It provides centralized donor management, volunteer coordination, and real-time impact analytics. The application is designed for secure Internet-based access, allowing administrators, donors, and volunteers to use the platform from anywhere through a web browser.

---

## 🏗️ Cloud Deployment Architecture

```
                       [ PUBLIC INTERNET ]
                               |
                               v
                  [ HTTPS / SSL (Let's Encrypt) ]
                               |
                               v
                  [ CUSTOM DOMAIN / DNS ROUTER ]
                   (e.g., smartngo.example.com)
                               |
                               v
            +------------------------------------+
            |      CLOUD CONTAINER / RUNTIME     |
            |     (Render / Railway / AWS ECS)   |
            |                                    |
            |   Spring Boot Web App (Port 8080)   |
            |    - Spring Security (BCrypt)      |
            |    - Spring MVC + Thymeleaf        |
            |    - Health Probe (/actuator/health)|
            +-----------------+------------------+
                              |
                     JDBC Connection Pool
                              |
                              v
             +----------------------------------+
             |       CLOUD MYSQL DATABASE       |
             |  (AWS RDS / PlanetScale / Aiven) |
             |                                  |
             |   Persistent Data: Users, Donors,|
             |   Volunteers, Donations, Tasks   |
             +----------------------------------+
```

---

## 📋 Production Environment Variables

Configure these environment variables in your cloud hosting provider dashboard:

| Variable Name | Purpose | Example Value |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Activates production configuration profile | `prod` |
| `PORT` | Assigned web port by cloud host | `8080` |
| `DB_HOST` | Hostname of your Cloud MySQL database | `db-mysql-smartngo.aivencloud.com` |
| `DB_PORT` | Port of your Cloud MySQL database | `14123` or `3306` |
| `DB_NAME` | MySQL database name | `smart_ngo` |
| `DB_USERNAME` | MySQL database user | `smart_user` |
| `DB_PASSWORD` | MySQL database password | `Secure_DB_Password_2026!` |
| `ADMIN_EMAIL` | Production admin email | `admin@smartngo.com` |
| `ADMIN_PASSWORD` | Production admin password | `Strong_Admin_Pass_2026!` |
| `PAYMENT_MODE` | Payment mode (`mock` or `razorpay`) | `mock` |
| `NOTIFICATION_MODE` | Notification mode (`mock` or `email`) | `mock` |
| `APP_BASE_URL` | Public HTTPS domain URL | `https://smartngo.example.com` |

---

## 🚀 Step-by-Step Deployment Instructions

### Method 1: Deploying to Render / Railway (Recommended for Ease of Use)

1. **Push Code to GitHub**:
   Ensure your code is pushed to a private or public GitHub repository.

2. **Provision Cloud MySQL Database**:
   - Go to [Aiven](https://aiven.io/) or [Railway](https://railway.app/).
   - Create a new **MySQL** database service.
   - Run initial schema initialization using `database/schema.sql` via MySQL Workbench, DBeaver, or cloud console:
     ```bash
     mysql -h <DB_HOST> -P <DB_PORT> -u <DB_USERNAME> -p <DB_NAME> < database/schema.sql
     ```

3. **Deploy Web Application Service**:
   - Create a new **Web Service** in Render/Railway and connect your GitHub repository.
   - Set Build Command: `mvn clean package -DskipTests` (or select Docker deploy option).
   - Set Start Command: `java -jar target/smart-ngo-platform-1.0.0.jar`.
   - Add all **Production Environment Variables** listed above.

4. **Configure Health Check Endpoint**:
   - Path: `/actuator/health`
   - Expected status: `200 OK` / `{"status":"UP"}`

---

## 🌐 Custom Domain & HTTPS Setup

1. **DNS Record Mapping**:
   - In your domain provider (Cloudflare, GoDaddy, Namecheap), add a CNAME record:
     - **Type**: `CNAME`
     - **Name**: `smartngo` (or `@` for root domain)
     - **Target**: `your-app-name.onrender.com` (or Railway domain)

2. **Automatic SSL / HTTPS Enforcement**:
   - Platform providers automatically issue free Let's Encrypt SSL certificates.
   - Verify access via **`https://smartngo.example.com`**.

---

## 💾 Database Backup & Restore Procedure

### Database Backup Command
Run this cron command or manual backup script:
```bash
mysqldump -h $DB_HOST -P $DB_PORT -u $DB_USERNAME -p$DB_PASSWORD $DB_NAME > backup_smart_ngo_$(date +%F).sql
```

### Database Restore Command
To restore from a backup:
```bash
mysql -h $DB_HOST -P $DB_PORT -u $DB_USERNAME -p$DB_PASSWORD $DB_NAME < backup_smart_ngo_2026-08-24.sql
```

---

## ✅ Production Acceptance Verification Checklist

- [x] Spring Boot application builds without errors.
- [x] Automated test suite passes 100% (`mvn clean test`).
- [x] Multi-environment profiles active (`application-prod.properties`).
- [x] Environment variable secret management enabled (`.env.example`, `.gitignore`).
- [x] Multi-stage `Dockerfile` and `docker-compose.yml` created.
- [x] Actuator health check endpoint `/actuator/health` exposed and permitted.
- [x] Database performance indexes added on frequently queried columns.
- [x] Production admin initialization configured via environment variables.
- [x] Deployment guide `DEPLOYMENT.md` completed.
