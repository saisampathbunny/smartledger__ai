# SmartLedger AI

SmartLedger AI is a full-stack bank transaction monitoring application that combines a Java Spring Boot backend, MySQL database, JWT-based authentication, rule-based fraud detection, and Google Gemini AI.

The application allows users to securely create an account, log in, submit bank transactions, analyze their fraud risk, and receive an AI-generated explanation for suspicious transactions.

---

## Overview

Financial transactions can contain different signals that may indicate suspicious activity.

SmartLedger AI evaluates submitted transactions using multiple fraud indicators and generates a fraud score.

When a transaction is classified as suspicious, Google Gemini is used to provide a simple natural-language explanation of why the transaction was flagged.

The application also provides user authentication so that each user's transaction data remains associated with their own account.

---

## Key Features

### User Authentication

- User registration
- User login
- Email and password authentication
- Password confirmation during registration
- Age validation
- Password hashing
- JWT-based authentication
- Protected REST APIs
- Logout functionality

### Transaction Management

- Add bank transactions
- Store transactions in MySQL
- Retrieve transactions for the authenticated user
- Associate transactions with their respective users
- View transaction information through the dashboard

### Fraud Detection

- Backend transaction analysis
- Multiple fraud indicators
- Fraud score calculation
- Safe/Suspicious classification
- Fraud explanation generation

### Generative AI

- Google Gemini API integration
- Natural-language explanations for suspicious transactions
- Makes fraud results easier to understand

### Frontend

- User registration page
- User login page
- Transaction dashboard
- Transaction submission form
- Transaction history
- Logout functionality

---

# Application Workflow

```text
                    User
                     |
                     v
             +---------------+
             | Registration  |
             +---------------+
                     |
                     v
              MySQL Database
                     |
                     v
             +---------------+
             |     Login     |
             +---------------+
                     |
                     v
              JWT Generated
                     |
                     v
             +---------------+
             |  Dashboard    |
             +---------------+
                     |
                     v
             Submit Transaction
                     |
                     v
             Spring Boot API
                     |
          +----------+----------+
          |                     |
          v                     v
   Fraud Detection        User Validation
          |
          v
      Fraud Score
          |
          v
   Safe / Suspicious
          |
          v
    Google Gemini
          |
          v
   AI Explanation
          |
          v
       MySQL
          |
          v
      Dashboard
