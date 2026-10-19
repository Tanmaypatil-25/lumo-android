# Lumo Android

<div align="center">

<img src="https://raw.githubusercontent.com/Tanmaypatil-25/Lumo/main/client/src/assets/branding/lumo-mark.svg" alt="Lumo Logo" width="90" />

# Lumo Android

### Real-time conversations, natively built for Android.

A native Android messaging client built with **Kotlin and Jetpack Compose**, connected to the existing Lumo backend to deliver real-time conversations, persistent authentication, media sharing, message management, and synchronized messaging across Android and Web.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![Socket.IO](https://img.shields.io/badge/Real--Time-Socket.IO-010101?style=for-the-badge&logo=socketdotio&logoColor=white)](https://socket.io/)

[![Source Code](https://img.shields.io/badge/Source_Code-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Tanmaypatil-25/lumo-android)
[![Related Project](https://img.shields.io/badge/Lumo_Web-8B5CF6?style=for-the-badge&logo=vercel&logoColor=white)](https://lumo-chat-tanmay.vercel.app/)

</div>

---

## About Lumo Android

**Lumo Android** brings the Lumo messaging ecosystem to Android through a native application built using Kotlin and Jetpack Compose.

Rather than introducing a separate mobile backend, the Android client communicates with the existing Lumo infrastructure. Authentication, conversations, message history, media storage, and real-time events are shared with the web application.

The application follows a layered, MVVM-style architecture that separates the UI, application state, repositories, networking, local session persistence, and real-time communication.

The goal is to build a maintainable native Android client while keeping the messaging experience consistent across platforms.

---

## Features

- Native Android interface built with Jetpack Compose
- Login and signup with JWT authentication
- Persistent sessions and automatic authentication restoration
- Real-time one-to-one messaging
- Message editing and deletion
- Online presence and typing indicators
- Sent and Seen status synchronization
- Unread message tracking
- Image messaging with local previews
- Cloudinary-backed media storage
- Android ↔ Web real-time synchronization
- Reactive UI state using Kotlin StateFlow
- Retrofit-based networking with Gson serialization

<details>
<summary><strong>View complete feature list</strong></summary>

### Authentication & Session Management

- User login and signup
- JWT-based authentication
- Authenticated API requests
- Persistent local session storage
- Automatic session restoration on application launch
- Reactive authentication state using StateFlow
- Logout and local session cleanup
- Backend API error handling

### Real-Time Messaging

- One-to-one conversations
- Real-time text message delivery
- Conversation history retrieval
- Android-to-Web message synchronization
- Web-to-Android message synchronization
- Real-time message updates
- Message editing and deletion
- Sent and Seen state synchronization
- Unread message counts
- Unread-state reset when opening a conversation
- Paginated conversation state

### Presence & Typing

- Online/offline presence indicators
- Cached presence state for UI listeners
- Real-time typing indicators
- Debounced typing events
- `typing` and `stopTyping` event handling
- Authenticated Socket.IO connections
- Socket lifecycle management
- Reconnection handling

### Media Messaging

- Android Photo Picker integration
- Local image selection using content URIs
- Image preview using Coil
- Image removal before sending
- Multipart image uploads using Retrofit
- Backend media processing through Multer
- Cloudinary-powered image storage
- Remote image rendering using Coil
- Real-time image message delivery
- Image messages shared between Android and Web

### Message Management

- Editing previously sent messages
- Deleting previously sent messages
- Backend-backed message operations
- Synchronization of message changes across connected clients
- Repository-driven message operations
- Reactive conversation state updates

### Architecture & State Management

- MVVM-style layered architecture
- Repository-based data access
- Kotlin Coroutines
- StateFlow-driven UI updates
- Separation of Compose UI from networking logic
- Centralized Retrofit configuration
- Build-configuration-based backend URL
- Shared backend infrastructure with Lumo Web

</details>

---

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
| Gson | JSON serialization and deserialization |
| Socket.IO Client | Real-time communication |
| Coil | Local preview and remote image loading |
| Android Photo Picker | Image selection |
| Android persistence layer | Session persistence |

### Backend & Infrastructure

| Technology | Purpose |
| --- | --- |
| Node.js | Server runtime |
| Express.js | REST API |
| MongoDB | Persistent application data |
| Mongoose | MongoDB object modeling |
| JWT | Authentication |
| Socket.IO | Real-time events |
| Multer | Multipart media processing |
| Cloudinary | Image storage and delivery |
| Vercel | Lumo Web deployment |

The Android client reuses the existing Lumo backend rather than maintaining a separate API or database.

---

## Architecture

Lumo Android separates presentation, application state, data access, networking, and real-time communication into distinct layers.

```text
┌───────────────────────────────────────┐
│           Jetpack Compose UI          │
│                                       │
│     Login · Signup · Home · Chat      │
└───────────────────┬───────────────────┘
                    │
                    ▼
┌───────────────────────────────────────┐
│              ViewModels               │
│                                       │
│      Application Logic · StateFlow    │
└───────────────────┬───────────────────┘
                    │
                    ▼
┌───────────────────────────────────────┐
│              Repositories             │
│                                       │
│     Authentication · Chat Operations  │
└───────────────┬───────────────┬───────┘
                │               │
                ▼               ▼
       ┌────────────────┐ ┌────────────────┐
       │   ApiService   │ │  SocketManager │
       │                │ │                │
       │ Retrofit APIs  │ │ Socket.IO      │
       └────────┬───────┘ └────────┬───────┘
                │                  │
                └────────┬─────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │    Lumo Backend     │
              │                     │
              │ Express · MongoDB   │
              │ JWT · Socket.IO     │
              │ Cloudinary          │
              └─────────────────────┘
```

### Presentation Layer

Jetpack Compose provides the native interface for authentication, conversation navigation, message display, and chat interactions.

The UI observes state exposed by ViewModels rather than performing networking or socket operations directly.

### ViewModel Layer

ViewModels coordinate application operations and expose observable state through Kotlin StateFlow.

This allows Compose to react to authentication changes, incoming messages, presence updates, and conversation changes.

### Repository Layer

Repositories coordinate data operations and separate application logic from the underlying networking implementation.

Authentication and chat operations are handled through dedicated repository classes.

### Networking Layer

Retrofit handles REST operations such as authentication, user discovery, conversation retrieval, message operations, and media uploads.

SocketManager encapsulates the Socket.IO connection for real-time events and synchronization.

### Persistence Layer

The local session manager maintains authentication state between application launches, allowing the application to restore an existing session without requiring the user to log in again.

---

## Authentication Flow

Authentication uses the existing Lumo backend and JWT-based session management.

```text
User Credentials
       │
       ▼
 Login / Signup Screen
       │
       ▼
   AuthViewModel
       │
       ▼
  AuthRepository
       │
       ▼
 ApiService · Retrofit
       │
       ▼
    Lumo Backend
       │
       ▼
    JWT + User Data
       │
       ▼
   SessionManager
       │
       ▼
 Authenticated Application
```

After successful authentication, session information is persisted locally.

On subsequent launches, the application restores the saved session and initializes the authenticated application flow.

Logging out clears the local session and resets the authenticated application state.

---

## Real-Time Communication

Socket.IO enables immediate synchronization between the Android application and the Lumo backend.

Current real-time capabilities include:

```text
Authenticated Socket Connection
             │
             ├── New Messages
             │
             ├── Message Updates
             │
             ├── Message Deletion
             │
             ├── Online Presence
             │
             ├── Typing Indicators
             │
             └── Seen State
```

### Message Delivery

When a message is sent, the backend handles message persistence and real-time delivery. Connected clients receive updates through the existing messaging infrastructure.

### Message Editing & Deletion

The Android client supports editing and deleting previously sent messages.

Message operations are coordinated through the application’s chat and networking layers, with updates synchronized through the shared backend and real-time infrastructure.

### Presence & Typing

Online presence and typing indicators are synchronized through Socket.IO.

Typing events are debounced to reduce unnecessary event traffic, while presence state is maintained so UI listeners can access the latest known online-user information.

### Read Receipts & Unread Messages

The application tracks unread messages and synchronizes Seen state with the backend.

Opening a conversation resets its unread state, allowing the conversation list and active chat to reflect the latest messaging activity.

---

## Image Messaging Pipeline

Lumo Android supports image messaging through the Android Photo Picker, Retrofit multipart requests, Cloudinary, and Coil.

```text
Android Photo Picker
         │
         ▼
    Selected URI
         │
         ▼
   Local Coil Preview
         │
         ▼
  Multipart Upload
         │
         ▼
  Retrofit + Repository
         │
         ▼
     Express API
         │
         ▼
       Multer
         │
         ▼
     Cloudinary
         │
         ▼
 Persisted Message
         │
         ▼
     Socket.IO
         │
         ▼
   Recipient Client
         │
         ▼
  Remote Coil Rendering
```

Users can select an image and preview it locally before sending.

The selected image is uploaded through a multipart request to the existing backend. The backend processes the upload, stores the media through Cloudinary, persists the associated message, and distributes the resulting message through the real-time messaging infrastructure.

Coil handles local previews and remote image rendering within the Android application.

---

## Cross-Platform Synchronization

Lumo Android and Lumo Web are independent clients of the same messaging platform.

```text
             ┌──────────────────────┐
             │    Lumo Backend      │
             │                      │
             │ REST API             │
             │ MongoDB              │
             │ JWT Authentication   │
             │ Socket.IO            │
             │ Cloudinary           │
             └──────────┬───────────┘
                        │
                  REST + Socket.IO
                        │
             ┌──────────┴───────────┐
             │                      │
             ▼                      ▼
    ┌────────────────┐     ┌────────────────┐
    │    Lumo Web    │◄───►│  Lumo Android  │
    │                │     │                │
    │ React + Vite   │     │ Kotlin         │
    │                │     │ Jetpack Compose│
    └────────────────┘     └────────────────┘
```

Both clients share the same:

- User accounts and authentication infrastructure
- Backend and MongoDB database
- Conversation and message history
- Message read state
- Real-time messaging infrastructure
- Presence and typing events
- Cloudinary media storage

This architecture enables conversations to continue across Web and Android without maintaining separate messaging systems.

---

## State Management

Kotlin StateFlow is used to expose reactive application state from ViewModels to the Compose UI.

```text
REST Response / Socket Event
             │
             ▼
         Repository
             │
             ▼
          ViewModel
             │
             ▼
          StateFlow
             │
             ▼
       Compose UI Update
```

This approach keeps the interface driven by observable state rather than direct network operations.

Conversation state includes information such as:

- Selected conversation
- Message history
- Unread counts
- Presence information
- Typing state
- Message updates and deletions
- Pagination state

The separation of concerns makes the application easier to maintain as new features are introduced.

---

## Project Structure

The Android application follows a package structure organized around data access, remote communication, persistence, repositories, and UI.

```text
lumo-android/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/tanmay/lumo/
│           │   │
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   └── SessionManager.kt
│           │   │   │
│           │   │   ├── model/
│           │   │   │   ├── AuthResponse.kt
│           │   │   │   ├── LoginRequest.kt
│           │   │   │   ├── SignupRequest.kt
│           │   │   │   └── ...
│           │   │   │
│           │   │   ├── remote/
│           │   │   │   ├── ApiService.kt
│           │   │   │   ├── RetrofitClient.kt
│           │   │   │   └── SocketManager.kt
│           │   │   │
│           │   │   └── repository/
│           │   │       ├── AuthRepository.kt
│           │   │       ├── ChatRepository.kt
│           │   │       └── ...
│           │   │
│           │   ├── ui/
│           │   │   ├── auth/
│           │   │   │   ├── AuthViewModel.kt
│           │   │   │   ├── LoginScreen.kt
│           │   │   │   └── SignupScreen.kt
│           │   │   │
│           │   │   └── chat/
│           │   │       ├── ChatViewModel.kt
│           │   │       ├── HomeScreen.kt
│           │   │       ├── ConversationScreen.kt
│           │   │       └── ...
│           │   │
│           │   └── MainActivity.kt
│           │
│           └── AndroidManifest.xml
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

The tree illustrates the major architectural areas; exact Gradle files and package locations may vary with the current repository structure.

---

## Getting Started

### Prerequisites

Make sure the development environment includes:

- [Android Studio](https://developer.android.com/studio)
- Android SDK compatible with the project's Gradle configuration
- JDK compatible with the project's Gradle configuration
- Git
- Access to the Lumo backend

### 1. Clone the Repository

```bash
git clone https://github.com/Tanmaypatil-25/lumo-android.git
cd lumo-android
```

### 2. Open the Project

Open the cloned project in Android Studio and allow Gradle synchronization to finish.

### 3. Configure the Backend URL

The Android client obtains its backend base URL through the application's build configuration instead of hardcoding it inside the networking layer.

Configure the URL using the project's existing build configuration or environment setup.

For a local development environment, ensure the URL is reachable from the Android device or emulator.

For example, an Android emulator commonly accesses a development server running on the host machine through `10.0.2.2`, rather than `localhost`.

Use the actual backend URL and port configured for the development environment.

### 4. Configure Backend Access

Ensure the Lumo backend is running and accessible.

The backend requires its own configuration for MongoDB, JWT authentication, Cloudinary, and permitted client origins. Follow the setup instructions in the [Lumo Web repository](https://github.com/Tanmaypatil-25/Lumo) for the shared backend infrastructure.

### 5. Run the Application

- Connect an Android device with USB debugging enabled, or start an Android emulator.
- Select the desired device in Android Studio.
- Build and run the application using the Run action.

Once the application launches, sign in or create an account through the configured backend.

---

## Engineering Focus

Lumo Android is being developed with an emphasis on:

- **Native Android development:** Kotlin and Jetpack Compose
- **Separation of concerns:** UI, ViewModels, repositories, and remote data sources
- **Reactive state management:** Coroutines and StateFlow
- **Real-time reliability:** Authenticated Socket.IO communication and lifecycle management
- **Cross-platform consistency:** A shared backend for Web and Android
- **Session persistence:** Authentication state restored across launches
- **Media handling:** URI-based image selection, multipart uploads, and remote rendering
- **Maintainability:** Centralized networking and repository-driven operations

The objective is to build a native mobile client that fits into the broader Lumo ecosystem while keeping application logic understandable, modular, and extensible.

---

## Development Status

**Lumo Android is under active development.**

The application has progressed beyond the initial networking foundation and authentication flow into a functional real-time messaging client.

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
- [x] Sent / Seen synchronization
- [x] Unread message tracking
- [x] Image selection and local previews
- [x] Multipart uploads and Cloudinary-backed media
- [x] Remote image rendering
- [x] Message editing and deletion

### Future Development

Potential areas for continued development include:

- Extended profile management
- Additional conversation controls
- UI/UX refinement
- Improved connection recovery and edge-case handling
- Broader automated testing
- Further reliability and production hardening

---

## Related Project

### Lumo Web

The original full-stack messaging application built using the MERN stack and Socket.IO.

[**View Lumo Web on GitHub →**](https://github.com/Tanmaypatil-25/Lumo)

[**Try the Lumo Web application →**](https://lumo-chat-tanmay.vercel.app/)

The Web and Android clients share the same backend, enabling cross-platform messaging and synchronized conversation state.

---

## Author

<div align="center">

### Tanmay Patil

Full-Stack Web & Android Developer

Information Technology Undergraduate

[![GitHub](https://img.shields.io/badge/GitHub-Tanmaypatil--25-181717?style=for-the-badge&logo=github)](https://github.com/Tanmaypatil-25)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Tanmay_Patil-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/tanmayppatil25/)

</div>

---

<div align="center">

<img src="https://raw.githubusercontent.com/Tanmaypatil-25/Lumo/main/client/src/assets/branding/lumo-mark.svg" alt="Lumo" width="45" />

### Lumo Android

**Built with Kotlin, Jetpack Compose & the Lumo backend.**

<sub>Designed and developed by Tanmay Patil.</sub>

</div>
