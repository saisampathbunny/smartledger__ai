# SmartLedger AI

SmartLedger AI is a full-stack bank transaction monitoring application built with Java Spring Boot, MySQL, Spring Security, JWT authentication, and Google Gemini API.

The application allows users to create an account, securely log in, submit bank transactions, analyze their fraud risk, and receive AI-generated explanations for suspicious transactions.

## Features

- User registration and login
- JWT-based authentication
- Password hashing with Spring Security
- Age validation during registration
- Protected REST APIs
- User-specific transaction access
- Bank transaction storage using MySQL
- Rule-based fraud detection
- Fraud score calculation
- Safe/Suspicious classification
- Google Gemini AI explanations
- Transaction history
- Simple HTML and JavaScript dashboard

## How It Works

```text
User
 |
 +--> Register
 |
 +--> Login
 |      |
 |      +--> JWT Token
 |
 +--> Dashboard
        |
        +--> Submit Transaction
                |
                v
        Spring Boot Backend
                |
                +--> Authentication
                |
                +--> Fraud Detection
                |
                +--> Gemini AI Explanation
                |
                v
             MySQL
                |
                v
          Transaction Result
