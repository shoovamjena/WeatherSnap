# WeatherSnap

WeatherSnap is a small Android application that allows users to search for live weather data, capture custom photographic evidence, compress the images, and save detailed weather reports locally.

## Setup and Run Instructions

1. Clone or download this repository.
2. Open the project in Android Studio (Iguana or newer recommended).
3. Wait for Gradle to sync dependencies.
4. Run the app on a physical device or emulator. 

*Note: The app uses the Open-Meteo API for geocoding and forecasting, which does not require an API key to run.*

## Tech Stack

This project was built using modern Android development tools and principles:
* **UI:** Kotlin, Jetpack Compose, Material 3.
* **Architecture:** MVVM, ViewModel, StateFlow.
* **Concurrency:** Kotlin Coroutines (with explicit IO thread usage for database operations).
* **Dependency Injection:** Dagger Hilt.
* **Navigation:** Navigation Compose.
* **Networking:** Retrofit, Gson converter, OkHttp logging interceptor.
* **Local Storage:** Room Database.
* **Hardware:** CameraX.

## Developer Judgment Challenge: Lifecycle and State Management

To ensure a robust user experience, the report creation flow was designed to survive configuration changes (like device rotation) and process death without losing in-progress data or duplicating reports. 

### Approach

1. **State Preservation:** User inputs, such as the field notes and the captured image file path, are managed using `rememberSaveable`. This ensures the data is bundled and restored automatically by the OS if the user rotates the device or sends the app to the background before saving.
2. **Frozen Weather Snapshot:** When the user clicks "Create Report", the exact weather metrics (temperature, humidity, pressure, etc.) are passed as literal string arguments through Navigation Compose. This acts as a frozen snapshot. Even if the app is killed and recreated, the UI relies on this frozen state rather than fetching fresh data, ensuring the final report reflects the exact moment the user initiated it.
3. **Temporary File Management:** The CameraX implementation writes both the original capture and the compressed output directly to the application's `cacheDir`. 

### Tradeoffs

* **Navigation Arguments vs. Shared ViewModel:** Passing the weather snapshot via navigation routes requires encoding and decoding strings, which introduces slight boilerplate. However, the tradeoff is highly beneficial: it guarantees the frozen state survives process death natively, whereas a shared ViewModel might drop state if not carefully backed by a `SavedStateHandle`.
* **Cache Directory Storage:** Saving temporary images to the cache directory means we rely on the Android OS to eventually sweep and clean up files from abandoned reports. While manual deletion could be implemented when the user hits "Back", offloading this to the OS cache manager ensures that files won't leak indefinitely even if the app crashes unexpectedly before a manual cleanup block can execute.
