# Am I Taking Enough PTO?

A Kotlin Multiplatform app to track your PTO (Paid Time Off) days and ensure you're taking adequate vacation time.

## Features

- 📊 **PTO Summary**: View total PTO days taken with visual status indicator (red/yellow/green)
- 📅 **Year Mode Toggle**: Switch between calendar year and rolling 365-day tracking
- ➕ **Quick Add**: Add PTO days with one tap (today, next X days)
- 📋 **View History**: See all your PTO days in a list with delete functionality
- ⚙️ **Settings**: Configure your target PTO days (default: 15 days)
- 📱 **Multiplatform**: Runs on Android, iOS, and Web (wasmJs)

## Architecture

### Technology Stack

- **Kotlin Multiplatform 2.0.0**: Shared business logic across all platforms
- **Compose Multiplatform 1.7.0**: Declarative UI framework
- **Circuit 0.22.2**: Presentation framework with unidirectional data flow
- **kotlinx.datetime**: Multiplatform date handling
- **kotlinx.serialization**: Data serialization
- **multiplatform-settings**: Platform-agnostic key-value storage

### Project Structure

```
composeApp/
├── src/
│   ├── commonMain/kotlin/com/ptotracker/
│   │   ├── domain/
│   │   │   ├── models/           # Domain models (PTODay, PTOSettings, etc.)
│   │   │   └── repository/       # Repository interfaces
│   │   ├── data/
│   │   │   ├── repository/       # Repository implementations
│   │   │   └── storage/          # Platform-specific storage
│   │   ├── ui/
│   │   │   ├── screens/          # Circuit screens (Home, Settings, AddPTO, ViewPTO)
│   │   │   ├── navigation/       # Screen definitions
│   │   │   └── PTOCircuit.kt     # Circuit factory
│   │   └── App.kt                # Main app entry point
│   ├── androidMain/
│   │   └── kotlin/com/ptotracker/
│   │       ├── MainActivity.kt
│   │       └── data/storage/StorageFactory.android.kt
│   ├── iosMain/
│   │   └── kotlin/com/ptotracker/
│   │       ├── main.kt
│   │       └── data/storage/StorageFactory.ios.kt
│   └── wasmJsMain/
│       ├── kotlin/com/ptotracker/
│       │   ├── main.kt
│       │   └── data/storage/StorageFactory.wasmJs.kt
│       └── resources/index.html
```

### Key Design Patterns

#### 1. Repository Pattern
Repositories abstract data access with interface/implementation separation:
- `PTORepository`: Manages PTO days
- `SettingsRepository`: Manages app settings
- Platform-specific storage via expect/actual mechanism

#### 2. Circuit Architecture
Using Slack's Circuit framework for UI:
- **Presenter**: Thin layer translating repository data to UI state
- **UI State**: Sealed interfaces with type-safe states
- **UI**: Stateless composables rendering Circuit state

#### 3. Sealed Interface States (No Event Sink)
Following strict sealed interface guidelines:
```kotlin
sealed interface HomeUiState : CircuitUiState {
    data object Loading : HomeUiState

    data class Loaded(
        val daysTaken: Int,
        val targetDays: Int,
        val status: PTOStatus,
        val onToggleYearMode: () -> Unit,
        val onAddPTO: () -> Unit,
        // Lambdas scoped to Loaded state only
    ) : HomeUiState
}
```

**Benefits**:
- No mutually exclusive fields in states
- Lambdas only exist where they're valid
- Better compile-time safety vs. event sink pattern
- Type-safe state prevents invalid UI configurations

#### 4. Flow-Based Reactive State
Repositories expose `Flow<T>` for reactive state updates:
```kotlin
interface PTORepository {
    fun getAllPTODays(): Flow<List<PTODay>>
    suspend fun addPTODay(date: LocalDate)
}
```

## Platform-Specific Storage

### Android
Uses `SharedPreferences` via `multiplatform-settings`

### iOS
Uses `NSUserDefaults` via `multiplatform-settings`

### Web (wasmJs)
Uses browser `localStorage` via `multiplatform-settings`

## Building and Running

### Prerequisites
- JDK 11 or higher
- Android Studio (for Android)
- Xcode (for iOS)
- Modern web browser (for Web)

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
1. Open `iosApp` in Xcode
2. Select target device/simulator
3. Build and run

### Web (wasmJs)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Status Calculation

PTO status is calculated based on progress toward target:
- 🟢 **Good**: ≥75% of target days taken
- 🟡 **Warning**: 40-74% of target days taken
- 🔴 **Critical**: <40% of target days taken

## Future Enhancements

- Calendar date picker for adding specific dates
- Calendar grid view for viewing PTO days
- Export PTO history (CSV, PDF)
- Reminder notifications
- Cloud sync across devices
- PTO categories (vacation, sick, personal)

## License

MIT License
