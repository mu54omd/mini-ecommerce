<p align="center">
  <img src="light-logo.png#gh-light-mode-only" alt="Mini E-Commerce" width="500">
  <img src="dark-logo.png#gh-dark-mode-only" alt="Mini E-Commerce" width="500">
</p>

# 🛒 Mini E-Commerce (Kotlin Multiplatform + Spring Boot)

A modular mini e-commerce project built with:

- **Backend:** Spring Boot + JWT + MariaDB/MySQL  
- **Frontend:** Android (Jetpack Compose), Desktop & Web (Compose Multiplatform)  
- **Shared:** Kotlin Multiplatform (Ktor client, shared logic)

This project demonstrates full-stack development, modular architecture, and cross-platform Kotlin capabilities.


## 📸 Screenshots / Demo


## 🧱 Project Structure

    
## 🚀 Features

- User authentication with JWT  
- CRUD operations for products  
- Shopping cart management  
- Order creation and tracking  
- Admin panel for managing products and orders  
- Modular and clean architecture  
- Cross-platform shared logic (KMP)  

## 📦 Technology Stack

- **Kotlin Multiplatform (Shared module):** Ktor Client, Coroutines, Serialization, Koin, Coil  
- **Android:** Jetpack Compose  
- **Desktop:** Compose for Desktop  
- **Web:** Compose for Web  
- **Backend:** Spring Boot, Spring Security (JWT), MariaDB/MySQL, Swagger UI  

## 🏁 How to Run

### 1️⃣ Backend
```bash
cd backend-maven
mvn spring-boot:run
```
The backend will run at `http://localhost:5050`

### 2️⃣ Frontend
* Android App
`use android-studio to run application`

* Desktop App
`./gradlew composeApp:run`

* Web App
`./gradlew composeApp:wasmJsBrowserDevelopmentRun`

Make sure the backend is running before launching any frontend applications.


🧩 Notes

- All shared logic is in the shared/ module, reused across Android, Desktop, and Web.

- Use Postman or Swagger UI to test API endpoints directly.

- Designed with clean architecture principles (Repository → UseCase → ViewModel → UI).


## 🎯 Future Improvements

