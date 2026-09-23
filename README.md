# Lumo Android

> Native Android client for **Lumo**, a real-time messaging platform, built with Kotlin and Jetpack Compose.

Lumo Android brings the existing Lumo messaging ecosystem to Android through a native client backed by the same REST API, authentication system, database, and real-time infrastructure used by the Lumo web application.

The project follows a layered architecture designed to keep networking, application state, and UI concerns separated as the application grows.

---

## Tech Stack

| Layer          | Technology                      |
| -------------- | ------------------------------- |
| Language       | Kotlin                          |
| UI             | Jetpack Compose                 |
| Architecture   | MVVM-style layered architecture |
| Networking     | Retrofit                        |
| Serialization  | Gson                            |
| Async / State  | Kotlin Coroutines + StateFlow   |
| Backend        | Node.js + Express               |
| Database       | MongoDB                         |
| Authentication | JWT                             |
| Real-time      | Socket.IO                       |
| Media          | Cloudinary                      |

---

## Architecture

The Android client follows a layered data flow:

```text
┌───────────────────────────┐
│      Jetpack Compose      │
│            UI             │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│         ViewModel         │
│   UI State / StateFlow    │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│        Repository         │
│    Data orchestration     │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│        ApiService         │
│      Retrofit APIs        │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│       Lumo Backend        │
│ Express · MongoDB · JWT   │
│        Socket.IO          │
└───────────────────────────┘
```

This keeps the Compose UI independent from direct network access and provides a clean path for introducing persistence, caching, and real-time state later.

---

## Project Structure

```text
com.tanmay.lumo/
│
├── data/
│   ├── model/
│   │   ├── AuthResponse.kt
│   │   ├── ErrorResponse.kt
│   │   ├── LoginRequest.kt
│   │   └── User.kt
│   │
│   ├── remote/
│   │   ├── ApiService.kt
│   │   └── RetrofitClient.kt
│   │
│   └── repository/
│       └── AuthRepository.kt
│
└── ui/
    └── auth/
        └── AuthViewModel.kt
```

The structure will expand as messaging, session persistence, navigation, and additional application features are introduced.

---

## Networking

REST communication is handled using **Retrofit**.

The networking layer currently supports:

```text
GET  /api/status
POST /api/auth/login
```

Retrofit uses:

* `ScalarsConverterFactory` for plain-text responses
* `GsonConverterFactory` for JSON API responses

The backend URL is provided through the application's build configuration rather than being hardcoded directly into the networking layer.

---

## Authentication Flow

The current login pipeline is:

```text
User credentials
      │
      ▼
AuthViewModel
      │
      ▼
AuthRepository
      │
      ▼
ApiService
      │
      ▼
POST /api/auth/login
      │
      ▼
Lumo Backend
      │
      ▼
AuthResponse
      │
      ▼
StateFlow
      │
      ▼
Compose UI
```

Successful authentication responses contain the authenticated user, JWT token, and server response message.

API error responses are represented separately so backend messages such as invalid credentials can be surfaced appropriately by the application.

---

## Current Development Status

### Implemented

* Native Android project using Kotlin
* Jetpack Compose configuration
* Environment-based backend URL configuration
* Retrofit networking layer
* Backend health/status request
* Gson JSON deserialization
* Authentication request and response models
* Login API integration
* Repository-based authentication data access
* Authentication ViewModel
* Reactive authentication state using StateFlow
* API error response model

### In Progress / Planned

* Authentication UI
* Signup flow
* Login loading and error states
* JWT persistence
* Automatic session restoration
* App navigation
* User discovery
* Conversation interface
* Message history
* Socket.IO integration
* Real-time message delivery
* Online/offline presence
* Profile management
* Image sharing
* Connection recovery and socket lifecycle handling

---

## Backend Integration

Lumo Android does not maintain a separate backend.

It connects to the existing **Lumo backend**, allowing the web and Android clients to share the same:

* User accounts
* Authentication system
* MongoDB database
* Message history
* Media infrastructure
* Socket.IO real-time layer

```text
                    ┌─────────────────┐
                    │  Lumo Backend   │
                    │                 │
                    │ Express         │
                    │ MongoDB         │
                    │ JWT             │
                    │ Socket.IO       │
                    │ Cloudinary      │
                    └────────┬────────┘
                             │
                  REST API + WebSocket
                             │
               ┌─────────────┴─────────────┐
               │                           │
               ▼                           ▼
        ┌──────────────┐             ┌──────────────┐
        │   Lumo Web   │             │ Lumo Android │
        │ React + Vite │             │ Kotlin +     │
        │              │             │ Compose      │
        └──────────────┘             └──────────────┘
```

---

## Development Roadmap

```text
Networking Foundation        ✅
        │
Authentication Data Layer    ✅
        │
Authentication State         ✅
        │
Authentication UI            ◉
        │
Session Persistence          ○
        │
Navigation                   ○
        │
Messaging Data Layer         ○
        │
Socket.IO Integration        ○
        │
Real-Time Chat               ○
        │
Media & Profile Features     ○
```

`✅ Complete`    `◉ Next`    `○ Planned`

---

## Project Status

Lumo Android is currently under active development.

Development is being approached incrementally, beginning with networking and authentication infrastructure before moving into persistent sessions, application navigation, and the real-time messaging layer.

---

## Related Project

**Lumo Web** — the original MERN-based web client for the Lumo real-time messaging platform.

---

## License

This project is currently maintained as part of the Lumo application ecosystem.
