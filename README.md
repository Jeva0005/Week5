# Week 6 - WeatherApp Room database

## Demo video

[Link to Demo video](https://unioulu-my.sharepoint.com/:v:/g/personal/t3vaje00_students_oamk_fi/IQBX65OxKkF6TIPrS60ANdTlATqNu1jqHWMN0MR8OzLOwK0)

- Room is the local database layer that lets the app save and read weather data as Kotlin objects. The Entity defines the table, the DAO defines queries, the Database provides access, the Repository wraps database calls, the ViewModel exposes state, and the UI simply reacts to that state.
- The project is split into a few layers: `data/model` for Room entities, `data/remote` for Retrofit API, `data/local` for Room DAO/database, `data/repository` for a single access point, `viewmodel` for app logic and state, and `ui` for Compose screens.
- When a city is being searched, the ViewModel decides whether to use either the cached data or call the API via the repository, and any new result is saved into Room. The UI checks ViewModel state (Flow/StateFlow), so when Room emits updated data, Compose recomposes automatically.
- The app checks the latest saved weather’s timestamp, and if it’s less than 30 minutes old and matches the same city, it uses the Room data instead of making a new network call. If it’s older than 30 minutes, it fetches fresh data from the API and updates Room, which then updates the UI.
