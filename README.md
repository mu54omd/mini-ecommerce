# 🛒 Mini E-Commerce (Kotlin Multiplatform + Spring Boot)

A modular mini e-commerce project built with:

- **Backend:** Spring Boot + JWT + MongoDB/MySQL  
- **Frontend:** Android (Jetpack Compose), Desktop & Web (Compose Multiplatform)  
- **Shared:** Kotlin Multiplatform (Ktor client, SQLDelight, shared logic)

This project demonstrates full-stack development, modular architecture, and cross-platform Kotlin capabilities.

---

## 📸 Screenshots / Demo

---

## 🧱 Project Structure

├── backend-maven/ → Spring Boot backend (Maven)
└── frontend-gradle/ → Frontend and shared KMP module
    ├── androidApp/ → Android application (Jetpack Compose)
    ├── desktopApp/ → Desktop application (Compose Desktop)
    ├── webApp/ → Web application (Compose Web)
    └── shared/ → Shared Kotlin Multiplatform module (logic & API client)
    
---

## 🚀 Features

- User authentication with JWT  
- CRUD operations for products  
- Shopping cart management  
- Order creation and tracking  
- Modular and clean architecture  
- Cross-platform shared logic (KMP)  

---

## 📦 Technology Stack

- **Kotlin Multiplatform (Shared module):** Ktor Client, Coroutines, Flow, SQLDelight, Serialization  
- **Android:** Jetpack Compose, Hilt/Koin, Coil  
- **Desktop:** Compose for Desktop  
- **Web:** Compose for Web  
- **Backend:** Spring Boot, Spring Security (JWT), MongoDB/MySQL, Swagger UI  

---

## 🏁 How to Run

### 1️⃣ Backend
```bash
cd backend-maven
mvn spring-boot:run
```
The backend will run at `http://localhost:8080`

### 2️⃣ Frontend
* Android App
`./gradlew frontend-gradle:androidApp:run`

* Desktop App
`./gradlew frontend-gradle:desktopApp:run`


* Web App
`./gradlew frontend-gradle:webApp:browserProductionRun`

Make sure the backend is running before launching any frontend applications.

---

🧩 Notes

- All shared logic is in the shared/ module, reused across Android, Desktop, and Web.

- Use Postman or Swagger UI to test API endpoints directly.

- Designed with clean architecture principles (Repository → UseCase → ViewModel → UI).

---

🎯 Future Improvements

- Admin panel for managing products and orders

- Payment integration (Stripe/PayPal)

- Dark mode & UI animations

- CI/CD with GitHub Actions

- Deployment of backend on cloud (Render, Railway, etc.)
