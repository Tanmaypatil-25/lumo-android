# Lumo Android

> Native Android client for **Lumo**, a real-time messaging platform, built with **Kotlin** and **Jetpack Compose**.

Lumo Android brings the Lumo messaging ecosystem to Android through a native client connected to the same backend infrastructure as the Lumo web application.

The application supports persistent authentication, conversation history, real-time messaging, presence tracking, typing indicators, read-state synchronization, unread message tracking, and image messaging across Android and Web clients.

The project follows a layered architecture that separates networking, persistence, real-time communication, application state, and UI concerns.

---

## Features

### Authentication & Session Management

- User login and signup
- JWT-based authentication
- Persistent authentication sessions
- Automatic session restoration on application launch
- Secure authenticated API requests
- Logout and local session cleanup
- Reactive authentication state using `StateFlow`
- Backend API error handling

### Messaging

- One-to-one conversations
- Message history retrieval
- Real-time incoming messages
- Android ↔ Web message synchronization
- Unread message counts
- Automatic unread-state reset when opening a conversation
- Sent / Seen message synchronization
- Paginated conversation state
- Real-time conversation updates

### Real-Time Communication

- Socket.IO connection authenticated using JWT
- Socket lifecycle tied to the authenticated user session
- Automatic socket reconnection handling
- Online/offline presence
- Cached presence state across UI listeners
- Typing indicators
- Debounced typing events
- `typing` / `stopTyping` event handling
- Real-time message delivery
- Seen-state synchronization

### Media Messaging

- Android Photo Picker integration
- Local image selection using URI
- Selected-image preview using Coil
- Image removal before sending
- Multipart image upload
- Backend image processing through Multer
- Cloudinary media storage
- Remote image rendering using Coil
- Real-time delivery of image messages

---

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM-style layered architecture |
| Networking | Retrofit |
| Serialization | Gson |
| Async / State | Kotlin Coroutines + StateFlow |
| Real-Time | Socket.IO |
| Image Loading | Coil |
| Local Session | Android persistence layer |
| Backend | Node.js + Express |
| Database | MongoDB |
| Authentication | JWT |
| Media Storage | Cloudinary |
| Media Upload | Multipart + Multer |

---

## Architecture

Lumo Android separates UI, application state, data access, networking, and real-time communication.

```text
┌─────────────────────────────────────┐
│          Jetpack Compose UI         │
│                                     │
│ Login · Signup · Home · Conversation│
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│              ViewModels             │
│                                     │
│       StateFlow · UI State          │
│       Application Logic             │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             Repositories            │
│                                     │
│ Authentication · Chat Operations    │
└──────────────┬──────────────┬───────┘
               │              │
               ▼              ▼
┌──────────────────────┐ ┌──────────────────────┐
│      ApiService      │ │    SocketManager     │
│                      │ │                      │
│ Retrofit REST APIs   │ │ Socket.IO Events     │
└──────────┬───────────┘ └──────────┬───────────┘
           │                        │
           └────────────┬───────────┘
                        │
                        ▼
          ┌──────────────────────────┐
          │       Lumo Backend       │
          │                          │
          │ Express · MongoDB · JWT  │
          │ Socket.IO · Cloudinary   │
          └──────────────────────────┘
```

REST APIs are responsible for operations such as authentication, loading users, retrieving message history, sending messages, and uploading media.

Socket.IO complements the REST layer by synchronizing events that need to propagate immediately between connected clients, including new messages, presence, typing state, and message read state.

---

## Project Structure

The Android client is organized around data, networking, persistence, repositories, ViewModels, and Compose UI.

```text
com.tanmay.lumo/
│
├── data/
│   │
│   ├── local/
│   │   └── SessionManager.kt
│   │
│   ├── model/
│   │   ├── AuthResponse.kt
│   │   ├── ErrorResponse.kt
│   │   ├── LoginRequest.kt
│   │   ├── SignupRequest.kt
│   │   ├── User.kt
│   │   └── ...
│   │
│   ├── remote/
│   │   ├── ApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── SocketManager.kt
│   │
│   └── repository/
│       ├── AuthRepository.kt
│       ├── ChatRepository.kt
│       └── ...
│
├── ui/
│   │
│   ├── auth/
│   │   ├── AuthViewModel.kt
│   │   ├── LoginScreen.kt
│   │   └── SignupScreen.kt
│   │
│   └── chat/
│       ├── ChatViewModel.kt
│       ├── HomeScreen.kt
│       ├── ConversationScreen.kt
│       └── ...
│
└── MainActivity.kt
```

This structure keeps Compose screens independent from direct networking and socket operations while allowing ViewModels to expose reactive application state to the UI.

---

## Authentication Flow

Authentication follows the application's standard repository-driven data flow.

```text
User Credentials
       │
       ▼
┌─────────────────┐
│   Login/Signup  │
│      Screen     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  AuthViewModel  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ AuthRepository  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   ApiService    │
│    Retrofit     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Lumo Backend   │
└────────┬────────┘
         │
         ▼
   JWT + User Data
         │
         ▼
┌─────────────────┐
│ SessionManager  │
└────────┬────────┘
         │
         ▼
   Authenticated UI
```

After successful authentication, the JWT is persisted locally.

On subsequent launches, the application restores the existing session and returns the user directly to the authenticated application flow without requiring another login.

Logging out clears the persisted session and disconnects authenticated application state.

---

## Messaging Architecture

Message operations use both REST and Socket.IO.

```text
                 ┌─────────────────┐
                 │ Conversation UI │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │  ChatViewModel  │
                 └────────┬────────┘
                          │
               ┌──────────┴──────────┐
               │                     │
               ▼                     ▼
      ┌─────────────────┐   ┌─────────────────┐
      │ ChatRepository  │   │  SocketManager  │
      └────────┬────────┘   └────────┬────────┘
               │                     │
               ▼                     │
      ┌─────────────────┐            │
      │    Retrofit     │            │
      │    REST API     │            │
      └────────┬────────┘            │
               │                     │
               └──────────┬──────────┘
                          ▼
                 ┌─────────────────┐
                 │  Lumo Backend   │
                 │                 │
                 │ REST + Socket.IO│
                 └─────────────────┘
```

REST provides persistent server operations while Socket.IO keeps active clients synchronized.

A message created from Android can therefore be persisted by the backend and propagated in real time to another connected Android or Web client.

---

## Real-Time Socket Layer

`SocketManager` encapsulates the Socket.IO connection used by the Android application.

The socket connection operates as part of the authenticated session and is responsible for receiving and propagating real-time state.

Current real-time capabilities include:

```text
Socket Connection
       │
       ├── New Messages
       │
       ├── Online Users
       │
       ├── Typing
       │
       ├── Stop Typing
       │
       └── Seen State
```

### Presence

The backend broadcasts the set of currently connected users.

The Android client maintains the latest presence state so newly registered UI listeners can immediately receive the most recent online-user information rather than waiting for another server event.

### Typing Indicators

Typing state is synchronized through Socket.IO.

```text
Android User Types
       │
       ▼
typing event
       │
       ▼
Lumo Backend
       │
       ▼
Recipient Client
       │
       ▼
"Typing..."
```

Typing events are debounced to prevent unnecessary socket traffic, and `stopTyping` is emitted when the user stops interacting with the message composer.

### Seen State

Opening and reading conversations updates message read state, allowing Sent / Seen information to remain synchronized between Android and Web clients.

---

## Image Messaging Pipeline

Lumo Android supports end-to-end image messaging using the Android Photo Picker, Retrofit multipart uploads, Cloudinary, Socket.IO, and Coil.

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
Multipart Request
        │
        ▼
┌─────────────────────┐
│     ApiService      │
│          +          │
│   ChatRepository    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    Express API      │
│                     │
│ upload.single(...)  │
└──────────┬──────────┘
           │
           ▼
      Cloudinary
           │
           ▼
    Message Record
           │
           ▼
    Socket.IO Event
           │
           ▼
 Recipient Client
           │
           ▼
 Remote Coil Rendering
```

The selected image can be previewed locally before transmission.

When sent, the Android URI is converted into a multipart request and passed through the existing Lumo backend media pipeline. The backend processes the upload through Multer, stores the media using Cloudinary, persists the corresponding message, and distributes the resulting message through the real-time messaging layer.

Remote images are rendered using Coil with network support.

---

## Cross-Platform Synchronization

Lumo Android and Lumo Web operate as clients of the same messaging system.

```text
                       ┌──────────────────────┐
                       │     Lumo Backend     │
                       │                      │
                       │ Express              │
                       │ MongoDB              │
                       │ JWT                  │
                       │ Socket.IO            │
                       │ Cloudinary           │
                       └──────────┬───────────┘
                                  │
                         REST + Socket.IO
                                  │
                ┌─────────────────┴─────────────────┐
                │                                   │
                ▼                                   ▼
       ┌──────────────────┐                ┌──────────────────┐
       │     Lumo Web     │                │   Lumo Android   │
       │                  │                │                  │
       │ React + Vite     │◄──────────────►│ Kotlin + Compose │
       └──────────────────┘                └──────────────────┘
```

Both clients share the same:

- User accounts
- Authentication system
- MongoDB database
- Conversations
- Message history
- Read state
- Presence information
- Real-time infrastructure
- Cloudinary media storage

This allows a user on Android to communicate directly with a user on the Web client while maintaining synchronized conversation state.

---

## Network Layer

REST communication is implemented using Retrofit.

The API layer is responsible for communication with the existing Lumo Express backend and supports authenticated requests for application operations including:

```text
Authentication
├── Login
├── Signup
└── Session validation

Users
└── Conversation/user discovery

Messages
├── Retrieve conversation history
├── Send text message
├── Send image message
└── Update message read state
```

JSON responses are deserialized using Gson.

Plain-text backend responses can be handled using `ScalarsConverterFactory`.

The backend base URL is supplied through the application's build configuration instead of being hardcoded inside the networking layer.

---

## State Management

The application uses Kotlin `StateFlow` to expose reactive state from ViewModels to Compose.

```text
Backend / Socket Event
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
    Compose Recomposition
```

This pattern is used for authentication and conversation state and keeps UI rendering driven by observable application state rather than direct network operations.

Conversation state maintains information such as:

- Selected user
- Message history
- Pagination state
- Unread counts
- Presence
- Typing state
- Real-time message updates

---

## Current Development Status

### Implemented

- [x] Native Android application with Kotlin
- [x] Jetpack Compose UI foundation
- [x] Layered MVVM-style architecture
- [x] Retrofit networking layer
- [x] Gson serialization
- [x] Environment/build-config based backend URL
- [x] Login flow
- [x] Signup flow
- [x] JWT authentication
- [x] Persistent session storage
- [x] Automatic session restoration
- [x] Logout flow
- [x] User/conversation loading
- [x] Conversation interface
- [x] Message history
- [x] Text message sending
- [x] Socket.IO authenticated connection
- [x] Real-time incoming messages
- [x] Android ↔ Web real-time messaging
- [x] Unread message counts
- [x] Unread-state reset
- [x] Sent / Seen synchronization
- [x] Online/offline presence
- [x] Presence-state caching
- [x] Typing indicators
- [x] Socket lifecycle handling
- [x] Image selection using Android Photo Picker
- [x] Local image preview
- [x] Multipart image uploads
- [x] Cloudinary-backed image messaging
- [x] Remote image rendering
- [x] Real-time image delivery

### In Progress / Planned

- [ ] Message editing
- [ ] Message deletion
- [ ] Extended profile management
- [ ] Additional conversation controls
- [ ] UI/UX refinement
- [ ] Broader connection recovery and edge-case handling
- [ ] Additional production hardening

---

## Development Progress

```text
Networking Foundation        ✅
        │
Authentication               ✅
        │
Session Persistence          ✅
        │
Conversation Flow            ✅
        │
Message History              ✅
        │
Text Messaging               ✅
        │
Socket.IO Integration        ✅
        │
Real-Time Messaging          ✅
        │
Presence & Typing            ✅
        │
Seen / Unread Sync           ✅
        │
Image Messaging              ✅
        │
Advanced Chat Operations     ◉
        │
UI / Production Polish       ○
```

`✅ Complete`    `◉ Current Focus`    `○ Planned`

---

## Engineering Goals

Lumo Android is being developed with an emphasis on:

- Clear separation of concerns
- Reactive UI state
- Shared backend infrastructure
- Cross-platform synchronization
- Reliable real-time communication
- Incremental feature development
- Maintainable networking and repository layers
- Native Android development practices

The Android client is intentionally built on top of the existing Lumo backend instead of maintaining a separate mobile backend, keeping authentication, conversations, media, and real-time behavior consistent across platforms.

---

## Project Status

**Lumo Android is under active development.**

The project has progressed beyond its initial networking and authentication foundation into a functional real-time messaging client.

Core messaging infrastructure—including persistent authentication, conversation history, Socket.IO communication, presence, typing state, Seen synchronization, unread tracking, and image messaging—is currently implemented.

Development is now moving toward advanced message operations, additional application features, UI refinement, and production hardening.

---

## Related Project

### Lumo Web

The original web client for the Lumo messaging platform, built using the MERN stack.

Lumo Web and Lumo Android communicate through the same backend and real-time infrastructure, enabling cross-platform conversations and synchronized messaging state.

---

## License

This project is currently maintained as part of the **Lumo application ecosystem**.
