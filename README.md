# PostViewer

**Student:** Leo Marques Sabino de Freitas  
**Student ID (RA):** 3045366  
**Course:** Mobile Device Programming (PRDM)  
**Institution:** IFSP — São Carlos Campus  

---

## Description

PostViewer is an Android application that consumes the public [JSONPlaceholder](https://jsonplaceholder.typicode.com) API to display posts and their comments, and lets the user add their own comments, which are saved locally on the device.

### Implemented requirements

• List of posts fetched from the API (home screen)
• Post detail screen showing comments from the API
• Adding local comments per screen, persisted on the device
• Local and API comments displayed together on the detail screen
• Navigation between screens with a back button in the top bar

---

## Running locally

### Prerequisites

• Android Studio Meerkat or later
• JDK 11
• A physical device or emulator running Android 8.0+ (API 26+)
• Internet connection (to fetch posts and comments from the API)

### Steps

• Clone or copy the project into a local folder
• Open Android Studio and select Open → the project folder
• Wait for Gradle to sync the dependencies
• Click Run → Run 'app' or press `Shift + F10`

No API key or environment variable needs to be configured.

---

## Technologies and libraries

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.3.21 | Main language |
| Android Gradle Plugin | 9.0.1 | Project build |
| Jetpack Compose | BOM 2026.05.00 | Declarative UI |
| Navigation Compose | 2.9.8 | Navigation between screens |
| Room | 2.7.1 | Local database |
| Retrofit | 2.11.0 | HTTP requests |
| Gson Converter | 2.11.0 | Deserialization of API JSON |
| Coroutines | 1.10.2 | Asynchronous operations |
| Lifecycle ViewModel Compose | 2.10.0 | ViewModel integrated with Compose |
| KSP | 2.3.9 | Code generation for Room |

---

## Design decisions

### Jetpack Compose instead of XML
Compose lets the UI be described directly in Kotlin, without switching between layout files and code. Its built in reactivity (via `StateFlow` + `collectAsState`) simplifies updating the UI whenever state changes.

### ViewModel + StateFlow
Each screen has its own ViewModel that exposes a `StateFlow<UiState>`. The screen only reads the state, it never accesses the API or the database directly. This keeps business logic separate from the UI and makes bugs easier to locate.

### AndroidViewModel for database access
`PostDetailViewModel` extends `AndroidViewModel` (instead of `ViewModel`) to receive the `Application` and open the Room database without depending on a repository or extra dependency injection, keeping the implementation simple given the project's scope.

### Room for local comments
Comments added by the user are stored with Room. The query returns a `Flow<List<LocalComment>>`, which is combined with the API comments via `combine()`. When the user inserts a new comment, the screen updates automatically without any manual refresh call.

### Centralized Navigation Compose
Routes are defined in a `sealed class Screen`, and the navigation graph lives in `AppNavGraph`. This avoids route strings scattered across the codebase and centralizes all navigation logic in a single place.

### Scaffold with TopAppBar in MainActivity
The `Scaffold` with the `TopAppBar` lives in `MainActivity`, shared across all screens. The back button appears automatically whenever there is a previous screen in the navigation stack, with no need to configure it individually on each screen.
