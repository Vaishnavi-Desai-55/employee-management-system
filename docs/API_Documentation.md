# Employee Management & Attendance System API Documentation

## Base URL

http://localhost:8080

## Authentication

The application uses JWT Bearer Token authentication.

Login using:

POST /auth/login

After successful login, copy the JWT token and use:

Authorization: Bearer <JWT_TOKEN>

---

# 1. Authentication APIs

## Login

**POST** `/auth/login`

### Request Body

```json
{
  "email": "ananya@gmail.com",
  "password": "ananya123"
}