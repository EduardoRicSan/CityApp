# 🌍 City Weather App – Search, Favorites & Weather Info

This repository contains an Android app built using **Jetpack Compose**, **Kotlin**, **Ktor**, **Room**, and **Hilt**. The app allows users to:

- 🔍 Search cities.
- ❤️ Mark them as favorites.
- 🗺️ View them on a map.
- 🌦️ Get current weather for any selected city.

> 🔀 **Note:** The updated codebase is maintained on the [`develop`](https://github.com/EduardoRicSan/CityApp/tree/develop) branch.

---

## 🔍 Problem to Solve

Build an intuitive, efficient and scalable search experience with:

- Real-time search filter.
- Toggle between all cities and favorites.
- Handling large JSON datasets.
- One-time data sync from remote.
- Persisted local storage and smooth user experience.

---

## ✅ Solution Overview

### 🧠 Key Strategies

- **Reactive State**: Combined flows of city list, search query, and favorites into one reactive stream using Kotlin Flow.
- **Local Persistence**: Used Room for cities and DataStore for favorite IDs (as a JSON-encoded list).
- **Separation of Concerns**: Clear division between UI, business logic, and data layers using Clean Architecture.
- **Composable UI**: UI built entirely with Jetpack Compose, following a unidirectional data flow.
- **Modularization**: Code split across distinct Gradle modules for maintainability and scalability.

---

## 🏗️ Architecture Overview

This app follows **Clean Architecture**, structured in layers:

      ┌─────────────────────────────┐
      │        Presentation         │ ← Jetpack Compose UI + ViewModel
      └────────────┬────────────────┘
                   │
      ┌────────────▼──────────────┐
      │          Domain           │ ← UseCases + Entities
      └────────────┬──────────────┘
                   │
      ┌────────────▼──────────────┐
      │           Data            │ ← Repositories + APIs + Room + DataStore
      └───────────────────────────┘

### 📁 Modules

📦 app
└── MainActivity.kt, Navigation, ScaffoldHost

📦 core
└── Design system, reusable UI components

📦 domain
├── usecase/
└── model/ ← Pure business models (e.g., City, Weather)

📦 data
├── repository/
├── local/ ← Room + DataStore
├── remote/ ← Ktor APIs + DTOs
└── mapper/ ← DTO ↔ Entity ↔ Domain model


---

## 🧩 Features

- 🔍 Search cities with real-time filtering
- ❤️ Favorite/unfavorite cities
- 🗺️ View city on map using Maps Compose
- 🌦️ Fetch weather info using [WeatherAPI](https://weatherapi.com)
- 🧠 Smart city sync on first load only
- 🧾 Data persistence using Room & DataStore
- 💉 Dependency Injection using Hilt

---

## 📌 Architectural Decisions

- 🌐 **Two HTTP clients** via `@CityClient` and `@WeatherClient` to manage different base URLs.
- 🧠 Used **sealed classes** for screen navigation and dynamic route handling (e.g., `map/{cityId}`).
- 📦 Kept **Weather data and city data modular and separate** using qualifier-based injections.
- ✅ Introduced **use cases** like `SearchCitiesUseCase`, `ToggleFavoriteUseCase`, `SyncCitiesUseCase` to encapsulate domain logic.

---

## 🧪 Testing Strategy

- ✅ **Unit tests** for:
  - Repositories
  - Use Cases
  - API Service with `MockEngine`
- ✅ **UI Tests** for Compose screens using `Compose UI Test`
- ✅ **Flow tests** using [Turbine](https://github.com/cashapp/turbine)

---

## ⚙️ Tech Stack

| Layer        | Tech                          |
|--------------|-------------------------------|
| Language     | Kotlin                        |
| UI           | Jetpack Compose + Material3   |
| DI           | Dagger Hilt                   |
| Network      | Ktor                          |
| Persistence  | Room + DataStore              |
| Weather API  | [weatherapi.com](https://weatherapi.com) |
| Maps         | Maps Compose + Google Play Maps |
| Testing      | JUnit, Mockk, Turbine         |

---

## 🤔 Assumptions

- The city list is static and only needs to be fetched once.
- Weather info is fetched only on demand.
- Cities can be identified uniquely by their `_id`.
- The favorites list is small enough to store as a `List<Int>` JSON in DataStore.

---

## 🔮 Future Enhancements

- 📡 Periodic background sync via WorkManager
- 🌙 Better dark theme support
- 🧠 Fuzzy or typo-tolerant search
- 🧪 Add end-to-end tests
- 🌍 Multi-language support
- ⚡ Offline weather cache fallback
- 🧱 Build a library of reusable Compose components
- 🧩 Refactor SearchBar, TopBar, CityRow, and Scaffold into reusable composables
- ♿ Improve accessibility support
- 🧱 Introduce UiState pattern or MVI pattern for better state management across screens.

---

## 📂 Branching

| Branch      | Purpose                    |
|-------------|-----------------------------|
| `main`      | Production-ready stable code |
| `develop`   | Active development branch (latest) ✅ |

---

## 🧑‍💻 Author

Developed with ❤️ by **Eduardo Rico**  
📫 [LinkedIn](https://www.linkedin.com/in/eduardorsmx/) · ✉️ lalo19jers.azul@gmail.com

---

