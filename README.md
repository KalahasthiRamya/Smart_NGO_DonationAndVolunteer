# Smart NGO Donation and Volunteer Management Platform

A full-stack, enterprise-grade Java Spring Boot web application designed for comprehensive NGO operational management, donor relationship tracking, volunteer coordination, campaign financial management, and real-time impact analytics.

---

## 🌟 Key Features

1. **Role-Based Security & Access Control**:
   - **ADMIN**: Complete system oversight, user management, donor/volunteer management, campaign creation, task assignment, attendance recording, impact analytics, CSV report exports.
   - **DONOR**: Self-service registration, profile management, browsing campaigns, executing donations via mock/live payment gateway, downloading official tax-deductible receipts, viewing personal contribution history.
   - **VOLUNTEER**: Skill registration, browsing opportunities, viewing assigned tasks, updating task statuses (`ASSIGNED` -> `IN_PROGRESS` -> `COMPLETED`), tracking attendance percentage.

2. **Core Modules**:
   - **Donor Management**: Add, edit, deactivate, search, filter, track total donations, last contribution dates.
   - **Volunteer Coordination**: Skill allocation, task assignment, attendance logging, contribution metrics.
   - **Campaign Management**: Real-time progress bar tracking (`Collected Amount` vs `Target Goal`), category classification (Education, Health, Environment).
   - **Payment Gateway Abstraction**: Supports `payment.mode=mock` out-of-the-box with automatic receipt generation and extensible Razorpay/Stripe API hooks.
   - **Impact Dashboard**: Dynamic Chart.js visualizations for *Donations Over Time* (Line Chart) and *Donations by Category* (Doughnut Chart), KPI metric cards, live activity feed.
   - **Reports & CSV Export**: One-click downloadable CSV reports for Donations, Volunteers, and Impact summaries (`/reports/donations/export`, `/reports/volunteers/export`, `/reports/impact/export`).
   - **Notification Service**: Automated email/SMS alert logging (`notification.mode=mock`).

---

## 🛠️ Mandatory Technology Stack

- **Language**: Java 8 (Compatible with Java 8, 11, 17, 21)
- **Framework**: Spring Boot 2.7.18, Spring MVC, Spring Security
- **Persistence**: Hibernate, JPA, Spring Data JPA
- **Database**: MySQL 8.0+ (with seamless H2 in-memory auto-fallback for out-of-the-box local execution)
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5, Chart.js, Thymeleaf View Engine
- **Build Tool**: Apache Maven (`pom.xml`)
- **Testing**: JUnit 5, Mockito, Spring Boot Test

---

## 🔑 Demo Credentials

The system automatically initializes full demo seed data upon startup:

| Role | Email | Password | Access URL |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@smartngo.com` | `admin123` | `http://localhost:8080/admin/dashboard` |
| **DONOR** | `rahul@gmail.com` | `donor123` | `http://localhost:8080/donor/dashboard` |
| **VOLUNTEER** | `sneha@gmail.com` | `volunteer123` | `http://localhost:8080/volunteer/dashboard` |

---

## 🚀 Quick Start & How to Run

### 1. Prerequisites
- **Java JDK**: 1.8 or higher installed.
- **Maven**: Installed or accessible via system PATH.

### 2. Running Locally with Maven
Open PowerShell/Terminal in the project root directory and execute:

```powershell
mvn spring-boot:run
```

Or using the direct Maven binary path if Maven is not in system PATH:
```powershell
& "C:\Users\ramya\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" spring-boot:run
```

### 3. Accessing the Application
Open your web browser and navigate to:
```
http://localhost:8080
```

---

## 🗄️ Database Setup (MySQL vs H2)

### Default Mode (Zero-Config H2 Fallback)
By default, the application runs using an embedded in-memory H2 database (`jdbc:h2:mem:smartngo_db`). It automatically creates all schema tables and seeds demo data on launch. You do not need to install or start MySQL to verify the full application!

### Enabling MySQL Database
If you wish to use a local MySQL server:
1. Create a database named `smart_ngo`:
   ```sql
   CREATE DATABASE smart_ngo;
   ```
2. Update `src/main/resources/application.properties` or set environment variables:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/smart_ngo?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   ```
3. SQL schema and seed scripts are located under `database/schema.sql` and `database/data.sql`.

---

## 🧪 Testing Execution & Results

Run the full automated test suite using Maven:

```powershell
& "C:\Users\ramya\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" clean test
```

### Test Case Execution Summary Table

| Test Suite | Test Case | Expected Result | Status |
| :--- | :--- | :--- | :---: |
| `DonorServiceTest` | `shouldReturnAllDonors()` | Retrieves all donor profiles accurately | **PASS** |
| `DonorServiceTest` | `shouldCalculateTotalDonations()` | Calculates total donation sum correctly | **PASS** |
| `DonationServiceTest` | `shouldCreateDonation()` | Processes donation, updates campaign goal | **PASS** |
| `VolunteerServiceTest` | `shouldUpdateVolunteerSkills()` | Updates volunteer skills & status | **PASS** |
| `AuthenticationIntegrationTest` | `shouldRegisterUserSuccessfully()` | Persists new user and role entity | **PASS** |
| `AuthenticationIntegrationTest` | `shouldRejectDuplicateEmail()` | Throws exception on duplicate registration | **PASS** |

---

## 📁 Package & Directory Architecture

```
c:\Users\ramya\OneDrive\Desktop\Java - A\Implementation
├── database/
│   ├── schema.sql
│   └── data.sql
├── src/
│   ├── main/
│   │   ├── java/com/smartngo/
│   │   │   ├── SmartNgoApplication.java
│   │   │   ├── config/ (SecurityConfig, DataInitializer)
│   │   │   ├── controller/ (Auth, Admin, Donor, Volunteer, Report, ApiDashboard, Payment)
│   │   │   ├── dto/ (UserRegistrationDto, DonationDto, TaskDto, PaymentRequest/Response)
│   │   │   ├── entity/ (User, Donor, Volunteer, Campaign, Donation, Task, Attendance, Notification)
│   │   │   ├── enums/ (Role, PaymentMethod, CampaignCategory, TaskPriority, etc.)
│   │   │   ├── exception/ (GlobalExceptionHandler, ResourceNotFoundException)
│   │   │   ├── repository/ (JPA Repositories)
│   │   │   ├── security/ (CustomUserDetails, UserDetailsService, SuccessHandler)
│   │   │   ├── service/ & service.impl/ (Core Business Logic Services)
│   │   │   └── util/ (CsvExportUtil)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/ (css/style.css, js/dashboard-charts.js)
│   │       └── templates/ (fragments/, auth/, admin/, donor/, volunteer/, error/)
│   └── test/java/com/smartngo/ (Unit & Integration Tests)
├── pom.xml
└── README.md
```
