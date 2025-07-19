# Assignment - Country List Android App


This project is an Android application built with Kotlin and Java. It fetches a list of countries from a remote JSON endpoint and displays them in the app. The project demonstrates networking with OkHttp, JSON parsing with Gson, and proper threading for UI updates.


## Features

- Fetches country data from a remote API.
- Parses JSON into Kotlin data classes.
- Handles network requests off the main thread.
- Updates UI on the main thread.
- Error handling for network and parsing failures.

## Technologies Used

- Kotlin
- Java
- OkHttp
- Gson
- Gradle


## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or device.


## Test Cases

- **Successful fetch:** Check if the app displays the full list of countries.
- **Network failure:** Checks if the app shows an error if the device is offline or the URL is invalid.
- **Malformed JSON:** Checks if the app handles and reports parsing errors.
- **Empty response:** Checks if the app shows an empty state or error if the response is empty.

## Edge Cases

- **Device Rotation:** Display rotates whenever the device/emulator is rotated without calling the API everytime
- **API returns partial data:** The app handles missing or null fields gracefully.
- **Main thread blocking:** All network and parsing operations are performed off the main thread to prevent UI freezes.
