# Week 5 - WeatherApp

## Demo video

[Link to Demo video](https://unioulu-my.sharepoint.com/:v:/g/personal/t3vaje00_students_oamk_fi/IQAb-9bIdfhVR547FcubpUc3AZyaDl58TmDTYA_wSG_0ILM?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=fL3t2N)

- Retrofit is a networking library that helps make HTTP requests in a clean and structured way. Endpointsa are defined as Kotlin functions, and Retrofit handles the request/response.

- The app calls the `WeatherApi` interface function, and Retrofit sends the request to OpenWeather in the background. The response is returned as a Kotlin object when the call finishes.

- OpenWeather returns JSON, and `WeatherResponse` data classes match that JSON structure. Retrofit uses the Gson converter to map JSON fields into Kotlin data class properties automatically.

- The API call runs inside `viewModelScope.launch`, so it doesn’t block the UI thread. When the result arrives, state updates and the UI refreshes instantly.

- `WeatherViewModel` owns a `WeatherUiState` inside a `StateFlow`, which represents loading, data, and error states. Compose collects that state and automatically recomposes whenever it changes.

- The API key is saved in `local.properties`, then injected into `BuildConfig` using `buildConfigField`. The ViewModel reads `BuildConfig.OPENWEATHER_API_KEY` and passes it to Retrofit.
