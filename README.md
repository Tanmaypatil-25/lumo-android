 <div align="center">

<img src="https://raw.githubusercontent.com/Tanmaypatil-25/Lumo/main/client/src/assets/branding/lumo-mark.svg" alt="Lumo Logo" width="90" />

<h1>Lumo Android</h1>

<h3>Real-time conversations, natively built for Android.</h3>

A native Android messaging client built with **Kotlin and Jetpack Compose**, connected to the existing Lumo backend for real-time conversations, persistent authentication, media sharing, and synchronized messaging across Android and Web.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![Socket.IO](https://img.shields.io/badge/Real--Time-Socket.IO-010101?style=for-the-badge&logo=socketdotio&logoColor=white)](https://socket.io/)

[![Source Code](https://img.shields.io/badge/Source_Code-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Tanmaypatil-25/lumo-android)
[![Lumo Web](https://img.shields.io/badge/Lumo_Web-8B5CF6?style=for-the-badge&logo=vercel&logoColor=white)](https://lumo-chat-tanmay.vercel.app/)

</div>

---

## About

**Lumo Android** brings the Lumo messaging ecosystem to Android through a native application developed using Kotlin and Jetpack Compose.

Instead of maintaining a separate mobile backend, the application connects to the existing Lumo infrastructure. Both Android and Web share authentication, conversations, message history, media storage, and real-time messaging events.

The application follows a layered, MVVM-style architecture with a focus on separation of concerns, reactive state management, and cross-platform consistency.

## Features

- **Native Android UI** built with Kotlin and Jetpack Compose
- **Authentication** with login, signup, and JWT-based sessions
- **Persistent sessions** with automatic authentication restoration
- **Real-time messaging** with synchronized conversations
- **Message editing and deletion** with backend-backed updates
- **Online presence and typing indicators**
- **Sent and Seen status synchronization**
- **Unread message tracking**
- **Image messaging** with local previews and remote rendering
- **Cloudinary media storage** through the existing backend
- **Android ↔ Web synchronization** using shared infrastructure
- **Reactive UI state** powered by Kotlin Coroutines and StateFlow
- **REST API integration** using Retrofit and Gson

## Tech Stack

### Android Application

| Technology | Purpose |
| --- | --- |
| Kotlin | Primary programming language |
| Jetpack Compose | Native declarative UI |
| MVVM-style architecture | Separation of UI and application logic |
| Kotlin Coroutines | Asynchronous operations |
| StateFlow | Reactive application state |
| Retrofit | REST API communication |
| Gson | JSON serialization |
| Socket.IO Client | Real-time communication |
| Coil | Local preview and remote image loading |
| Android Photo Picker | Image selection |
| SessionManager | Local session persistence |

### Backend & Infrastructure

| Technology | Purpose |
| --- | --- |
| Node.js and Express.js | Backend runtime and REST API |
| MongoDB and Mongoose | Persistent data storage |
| JWT | Authentication |
| Socket.IO | Real-time events |
| Multer | Multipart media processing |
| Cloudinary | Image storage and delivery |
| Vercel | Lumo Web deployment |

The Android client reuses the existing Lumo backend instead of maintaining a separate API or database.

## Architecture

Lumo Android separates presentation, application state, data access, and communication into distinct layers.

```text
┌─────────────────────────────────────┐
│          Jetpack Compose UI         │
│       Login · Home · Chat            │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             ViewModels              │
│       Application Logic · StateFlow  │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             Repositories             │
│     Authentication · Chat Operations │
└───────────────┬───────────────┬─────┘
                │               │
                ▼               ▼
        ┌──────────────┐ ┌──────────────┐
        │  ApiService  │ │SocketManager │
        │   Retrofit   │ │  Socket.IO   │
        └──────┬───────┘ └──────┬───────┘
               │                │
               └────────┬───────┘
                        │
                        ▼
             ┌─────────────────────┐
             │    Lumo Backend     │
             │ Express · MongoDB   │
             │ JWT · Socket.IO     │
             │ Cloudinary          │
             └─────────────────────┘
```

### How the layers work

- **Presentation:** Jetpack Compose renders authentication screens, conversations, messages, and chat interactions.
- **ViewModels:** Coordinate application operations and expose observable state through StateFlow.
- **Repositories:** Separate application logic from networking and coordinate authentication and messaging operations.
- **Networking:** Retrofit handles REST requests, while SocketManager manages real-time communication.
- **Persistence:** SessionManager maintains authentication information across application launches.

## Authentication & Session Management

The application uses the existing Lumo backend for authentication.

- Users can log in or sign up through the Android interface.
- Authenticated API requests use JWT-based authentication.
- Session information is persisted locally.
- Existing sessions are restored when the application launches.
- Logging out clears the local session and resets authenticated application state.

## Real-Time Messaging

Socket.IO enables real-time communication between the Android application and the shared Lumo backend.

Supported messaging capabilities include:

- Real-time message delivery
- Message editing and deletion
- Online and offline presence
- Typing indicators
- Sent and Seen status synchronization
- Unread message counts and state updates
- Conversation synchronization between Android and Web

Message operations are coordinated through the application's repositories and networking layer. Backend-backed changes can be reflected across connected clients through the shared messaging infrastructure.

## Image Messaging

Lumo Android supports image messaging through the Android Photo Picker, Retrofit multipart requests, Cloudinary, and Coil.

The image handling process includes:

1. Select an image using the Android Photo Picker.
2. Preview the selected image locally.
3. Upload the image through a multipart request.
4. Process and store the media through the existing backend and Cloudinary.
5. Persist the associated message and deliver updates through the messaging infrastructure.
6. Render remote images in the Android conversation using Coil.

Users can preview or remove a selected image before sending it. Image messages use the same backend infrastructure as Lumo Web.

## Cross-Platform Messaging

Lumo Android and Lumo Web are independent clients connected to the same messaging platform.

Both clients share:

- User accounts and authentication infrastructure
- Backend APIs and MongoDB data
- Conversation and message history
- Message editing and deletion operations
- Read states and unread message tracking
- Real-time messaging, presence, and typing events
- Cloudinary media storage

This allows conversations to continue across Android and Web without maintaining separate messaging systems.

## Project Structure

The project is organized around data models, local persistence, remote communication, repositories, and UI components.

```text
lumo-android/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/tanmay/lumo/
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   └── SessionManager.kt
│           │   │   ├── model/
│           │   │   │   └── ...
│           │   │   ├── remote/
│           │   │   │   ├── ApiService.kt
│           │   │   │   ├── RetrofitClient.kt
│           │   │   │   └── SocketManager.kt
│           │   │   ├── repository/
│           │   │   │   ├── AuthRepository.kt
│           │   │   │   ├── ChatRepository.kt
│           │   │   │   └── ...
│           │   ├── ui/
│           │   │   ├── auth/
│           │   │   │   ├── AuthViewModel.kt
│           │   │   │   ├── LoginScreen.kt
│           │   │   │   └── SignupScreen.kt
│           │   │   └── chat/
│           │   │       ├── ChatViewModel.kt
│           │   │       ├── HomeScreen.kt
│           │   │       ├── ConversationScreen.kt
│           │   │       └── ...
│           │   └── MainActivity.kt
│           └── AndroidManifest.xml
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

*This tree highlights the main architectural areas; individual files may change as development progresses.*

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- Android SDK compatible with the project's Gradle configuration
- Compatible JDK
- Git
- Access to the Lumo backend

### 1. Clone the Repository

```bash
git clone https://github.com/Tanmaypatil-25/lumo-android.git
cd lumo-android
```

### 2. Open the Project

Open the project in Android Studio and allow Gradle synchronization to finish.

### 3. Configure the Backend URL

Configure the backend base URL through the project's existing build configuration or environment setup.

For local development, make sure the backend is reachable from the Android device or emulator. An Android emulator commonly uses `10.0.2.2` to reach a server running on the host machine, rather than `localhost`.

Use the actual URL and port configured for the development environment.

### 4. Start the Backend

Ensure the Lumo backend is running and configured with the required MongoDB, JWT, Cloudinary, and client-origin settings.

Refer to the [Lumo Web repository](https://github.com/Tanmaypatil-25/Lumo) for the shared backend infrastructure and setup instructions.

### 5. Run the Application

1. Connect an Android device with USB debugging enabled, or start an emulator.
2. Select the device in Android Studio.
3. Build and run the application.

Once the application launches, sign in or create an account using the configured backend.

## Development Status

**Lumo Android is under active development.**

### Implemented

- [x] Native Android application using Kotlin
- [x] Jetpack Compose UI foundation
- [x] MVVM-style layered architecture
- [x] Retrofit networking and Gson serialization
- [x] Build-config-based backend URL
- [x] Login, signup, and JWT authentication
- [x] Persistent sessions and automatic restoration
- [x] User and conversation loading
- [x] Message history and text messaging
- [x] Authenticated Socket.IO connection
- [x] Real-time incoming messages
- [x] Android ↔ Web message synchronization
- [x] Online presence and typing indicators
- [x] Sent and Seen status synchronization
- [x] Unread message tracking
- [x] Image selection and local previews
- [x] Multipart uploads and Cloudinary-backed media
- [x] Remote image rendering
- [x] Message editing and deletion

### Future Improvements

- Extended profile management
- Additional conversation controls
- UI/UX refinement
- Improved connection recovery and edge-case handling
- Broader automated testing
- Further reliability and production hardening

## Related Project

### Lumo Web

The original full-stack messaging application built with the MERN stack and Socket.IO.

- **[View Lumo Web on GitHub →](https://github.com/Tanmaypatil-25/Lumo)**
- **[Try the Lumo Web application →](https://lumo-chat-tanmay.vercel.app/)**

The Web and Android clients share the same backend, enabling cross-platform messaging and synchronized conversation state.

---

## Author

<div align="center">

### Tanmay Patil

Full-Stack Web & Android Developer  
Information Technology Undergraduate

<br />

[![GitHub](https://img.shields.io/badge/GitHub-Tanmaypatil--25-181717?style=for-the-badge&logo=github)](https://github.com/Tanmaypatil-25)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Tanmay_Patil-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/tanmayppatil25/)
[![LeetCode](https://img.shields.io/badge/LeetCode-tanmaypatil__25-FFA116?style=for-the-badge&logo=leetcode&logoColor=black)](https://leetcode.com/u/tanmaypatil_25/)

</div>

---

<div align="center">

<img src="https://raw.githubusercontent.com/Tanmaypatil-25/Lumo/main/client/src/assets/branding/lumo-mark.svg" alt="Lumo Logo" width="45" />

### Lumo Android

**Native Android client built with Kotlin & Jetpack Compose**

<sub>Designed and developed by Tanmay Patil.</sub>

</div>