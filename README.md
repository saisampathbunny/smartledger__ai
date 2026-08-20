# smartledger__ai
AI-powered fraud detection system with secure user authentication
# SmartLedger AI

SmartLedger AI is a bank transaction monitoring application built with Java Spring Boot.

## Features

- User registration and login
- JWT-based authentication
- User-specific transaction access
- Bank transaction storage
- Rule-based fraud detection
- Fraud score generation
- Google Gemini API integration for fraud explanations
- Simple HTML and JavaScript dashboard

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- REST APIs

### Frontend
- HTML
- JavaScript

### AI
- Google Gemini API

## Architecture

```text
Frontend
   |
   | REST API
   v
Spring Boot Backend
   |
   +-- Spring Security + JWT
   |
   +-- Fraud Detection
   |
   +-- Gemini API
   |
   v
MySQL Database
