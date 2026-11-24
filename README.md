# Employee Management & Attendance System

A full-stack Employee Management & Attendance System built with Spring Boot and a web-based frontend. The project provides employee management, attendance tracking, leave management, department and designation management, JWT-based authentication, and role-based access control.

The project was built as a mentor/learning project to practice developing a secured REST API and integrating it with a functional web application.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- BCrypt
- MySQL
- Bean Validation
- HTML
- CSS
- JavaScript
- Swagger / OpenAPI
- Maven

## Features

### Employee Management

- Create, view, update, and delete employees
- View an employee by ID

### Search and Filtering

- Search by name
- Filter by department
- Filter by minimum salary
- Pagination and sorting

### Authentication

- Employee login
- Passwords hashed with BCrypt
- JWT authentication with stateless security

### Role-Based Access

- Three roles: `ADMIN`, `MANAGER`, `EMPLOYEE`
- Role-based access to protected operations
- Employees can access their own attendance and leave records
- Admins and managers can perform management operations according to their roles

### Attendance

- Check-in and check-out
- Working hours calculated automatically
- View attendance by employee
- Attendance report by date
- Duplicate attendance prevention

### Leave Management

- Apply for leave
- View leaves of an employee
- View leaves by status
- Approve or reject a leave
- Statuses: `PENDING`, `APPROVED`, `REJECTED`
- Employee ownership validation for leave operations

### Department Management

- Create departments
- View departments
- Update departments
- Delete departments

### Designation Management

- Create designations
- View designations
- Update designations
- Delete designations

### Other

- Bean validation
- Global exception handling
- REST API documentation using Swagger/OpenAPI
- Web-based frontend integrated with backend REST APIs

## Architecture

```text
Web Application / Postman / Swagger
                ↓
           Controller
                ↓
             Service
                ↓
           Repository
                ↓
        JPA / Hibernate
                ↓
             MySQL
```

## Database

Database name: `employee_system`

### Tables

- `employee`
- `attendance`
- `employee_leave`
- `department`
- `designation`

### Relationships

- `employee` → `attendance` (1:N)
- `employee` → `employee_leave` (1:N)
- `department` and `designation` are currently standalone tables. The employee's department is stored as a plain text column in the `employee` table, so there is no foreign key to either of them.

### Attendance Constraint

The `attendance` table has the following unique constraint:

```sql
UNIQUE(employee_id, attendance_date)
```

This ensures that an employee cannot have more than one attendance record for the same date.

The full table structure is available in `docs/Database_Structure.pdf`.

The ER diagram is available in the `docs` folder.

## Setup

### Prerequisites

- Java 17
- Maven
- MySQL

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <project-folder>
```

### 2. Create the Database

Create the database in MySQL:

```sql
CREATE DATABASE employee_system;
```

### 3. Configure Database Connection

Open:

```text
src/main/resources/application.properties
```

Configure your MySQL credentials and required environment variables.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_system
spring.datasource.username=your_mysql_username
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}

spring.jpa.hibernate.ddl-auto=update
```

Set the required environment variables before starting the application.

Do not commit your actual MySQL password or JWT secret to GitHub.

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

### 5. Open the Web Application

```text
http://localhost:8080/
```

### 6. Open Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

To use secured endpoints, log in through `/auth/login` and use the returned JWT token as a Bearer token.

## API Documentation

### Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Specification

```text
http://localhost:8080/v3/api-docs
```

Detailed API documentation is available in:

```text
docs/API_Documentation.md
```

## Endpoint Summary

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Employee login |

### Employees

| Method | Endpoint | Description |
|---|---|---|
| POST | `/employees` | Create employee |
| GET | `/employees` | Get all employees |
| GET | `/employees/{id}` | Get employee by ID |
| PUT | `/employees/{id}` | Update employee |
| DELETE | `/employees/{id}` | Delete employee |
| GET | `/employees/search?name=...` | Search by name |
| GET | `/employees/department/{department}` | Filter by department |
| GET | `/employees/salary?min=...` | Filter by minimum salary |
| GET | `/employees/page?page=0&size=5&sortBy=id` | Pagination and sorting |

### Attendance

| Method | Endpoint | Description |
|---|---|---|
| POST | `/attendance/check-in/{employeeId}` | Check-in |
| PUT | `/attendance/check-out/{employeeId}` | Check-out |
| GET | `/attendance/employee/{employeeId}` | Attendance of an employee |
| GET | `/attendance/date/{date}` | Attendance report by date |

### Leave

| Method | Endpoint | Description |
|---|---|---|
| POST | `/employees/apply/{employeeId}` | Apply for leave |
| GET | `/employees/employee/{employeeId}` | Leaves of an employee |
| GET | `/employees/status/{status}` | Leaves by status |
| PUT | `/employees/{leaveId}/status?status=APPROVED` | Approve or reject leave |

### Department

| Method | Endpoint | Description |
|---|---|---|
| POST | `/departments` | Create department |
| GET | `/departments` | Get all departments |
| PUT | `/departments/{id}` | Update department |
| DELETE | `/departments/{id}` | Delete department |

### Designation

| Method | Endpoint | Description |
|---|---|---|
| POST | `/designations` | Create designation |
| GET | `/designations` | Get all designations |
| PUT | `/designations/{id}` | Update designation |
| DELETE | `/designations/{id}` | Delete designation |

## Security

- Passwords are hashed with BCrypt before being stored.
- Login returns a JWT token, which is used to authenticate subsequent requests.
- Access to endpoints is controlled by role: `ADMIN`, `MANAGER`, and `EMPLOYEE`.
- The application uses stateless security, so no server-side session is maintained.
- Attendance and leave operations include ownership checks so employees cannot access another employee's records.
- Protected API requests require a valid JWT Bearer token.

## Testing

The application was tested manually using the web application, Swagger UI, and Postman.

The main scenarios tested include:

- Employee CRUD operations
- Input validation
- JWT authentication
- Role-based access control
- Duplicate attendance prevention
- Working-hours calculation
- Leave application and approval/rejection
- Employee leave ownership validation
- Search and filtering
- Pagination and sorting
- Exception handling
- Department management
- Designation management
- Frontend integration with REST APIs
- Login and logout flow

## Web Application Screenshots

### Login

![Login](screenshots/01-login.png)

### Dashboard

![Dashboard](screenshots/02-dashboard.png)

### Attendance

![Attendance](screenshots/04-attendance.png)

### Leave Management

![Leave Management](screenshots/05-leave-management.png)

### Departments

![Departments](screenshots/departments.png)

### Designations

![Designations](screenshots/07-designations.png)

## API Screenshots

Swagger and API testing screenshots are stored in:

```text
screenshots/
```

These screenshots demonstrate the REST API endpoints and backend testing performed using Swagger/OpenAPI and Postman.

## Project Documentation

```text
docs/
├── API_Documentation.md
├── Database_Structure.pdf
└── ER_Diagram.png
```

Screenshots of the web application and API testing are stored in:

```text
screenshots/
```

## Project Structure

```text
employee-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── docs/
│   ├── API_Documentation.md
│   ├── Database_Structure.pdf
│   └── ER_Diagram.png
├── screenshots/
├── pom.xml
└── README.md
```

## Author

**Vaishnavi Desai**