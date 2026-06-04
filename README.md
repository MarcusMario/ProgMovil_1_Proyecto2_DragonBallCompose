# Dragon Ball Z — Proyecto Jetpack Compose

Aplicación Android que consulta la Dragon Ball API con autenticación Firebase, construida con Jetpack Compose (UI declarativa en Kotlin puro, sin XML de layouts).

---

## Requisitos previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK mínimo: API 24 (Android 7.0)
- Cuenta en Firebase

---
<img width="421" height="919" alt="image" src="https://github.com/user-attachments/assets/d354837c-13f8-4dad-a0d2-0733a00c115f" />
<img width="421" height="919" alt="image" src="https://github.com/user-attachments/assets/015c7491-6c73-4af8-8eb6-da03b26f6329" />
<img width="421" height="919" alt="image" src="https://github.com/user-attachments/assets/dc845c0c-4bef-405a-a873-64340858a9e0" />
<img width="419" height="946" alt="image" src="https://github.com/user-attachments/assets/398c47cd-9d71-48fd-b94c-fc822a1b709c" />





## Configuración Firebase

### Paso 1 — Crear proyecto en Firebase
1. Ve a https://console.firebase.google.com
2. Clic en "Agregar proyecto" → nombra el proyecto
3. Acepta y crea el proyecto

### Paso 2 — Registrar la app
1. En Firebase Console, clic en el ícono Android
2. Package name: `com.example.dragonballcompose`
3. Descarga el archivo `google-services.json`
4. Colócalo en: `DragonBallCompose/app/google-services.json`

### Paso 3 — Habilitar Authentication
1. Firebase Console → Authentication → Sign-in method
2. Habilitar: Email/Password

### Paso 4 — Crear Firestore
1. Firebase Console → Firestore Database
2. Crear base de datos en modo de prueba

---

## Ejecutar el proyecto

1. Abrir la carpeta `DragonBallCompose` en Android Studio
2. Colocar `google-services.json` en `app/`
3. File → Sync Project with Gradle Files
4. Run → Run 'app'

---

## Estructura del proyecto

```
DragonBallCompose/
├── app/
│   ├── google-services.json             ← descargar de Firebase
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/dragonballcompose/
│           ├── MainActivity.kt           ← punto de entrada + Splash
│           ├── models/
│           │   └── Models.kt             ← Character, Planet, UserProfile
│           ├── network/
│           │   └── Network.kt            ← Retrofit + ApiService
│           ├── repository/
│           │   └── DragonBallRepository.kt ← lógica de datos
│           ├── viewmodel/
│           │   ├── SearchViewModel.kt    ← StateFlow de búsqueda
│           │   └── DetailViewModel.kt    ← StateFlow de detalle + Factory
│           └── ui/
│               ├── theme/
│               │   └── Theme.kt          ← colores dorado/negro
│               ├── navigation/
│               │   ├── Screen.kt         ← rutas de navegación
│               │   └── NavGraph.kt       ← mapa de navegación
│               └── screens/
│                   ├── LoginScreen.kt    ← login con Firebase
│                   ├── RegisterScreen.kt ← registro 6 campos
│                   ├── SearchScreen.kt   ← búsqueda + lista
│                   └── DetailScreen.kt   ← imagen + datos completos
├── build.gradle
├── settings.gradle
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

---

## Arquitectura (MVVM)

```
LoginScreen / RegisterScreen / SearchScreen / DetailScreen
        ↕ collectAsState(StateFlow)
SearchViewModel / DetailViewModel
        ↕ coroutines (viewModelScope)
DragonBallRepository
        ↕                    ↕
RetrofitClient          FirebaseAuth
(Dragon Ball API)       (Firestore)
```

---

## Pantallas

| Pantalla | Archivo | Descripción |
|---|---|---|
| Splash | MainActivity.kt | installSplashScreen() |
| Login | LoginScreen.kt | Email + Password con Firebase |
| Registro | RegisterScreen.kt | 6 campos guardados en Firestore |
| Búsqueda | SearchScreen.kt | TextField + LazyColumn |
| Detalle | DetailScreen.kt | Imagen + datos completos |

---

## Navegación

```
Login ──────────────────→ Search ──→ Detail
  ↑                          ↑          |
  └── Register ──────────────┘          |
                                        ↓
                               (botón regresar)
```

Rutas definidas en `Screen.kt`:
- `login`
- `register`
- `search`
- `detail/{characterId}`

---

## API Dragon Ball

**Base URL:** `https://dragonball-api.com/api/`

| Endpoint | Tipo de respuesta |
|---|---|
| `GET /characters?name=Goku` | Array directo `[...]` |
| `GET /characters/{id}` | Objeto `Character` |

**Nota importante:** El filtro por nombre devuelve un array directo, no un objeto paginado. El Repository maneja ambos casos automáticamente.

---

## Datos del personaje mostrados

- Imagen (Coil AsyncImage)
- Nombre
- Raza
- Género
- Ki / Ki Máximo
- Afiliación
- Planeta de origen
- Descripción
- Lista de transformaciones

---

## Dependencias principales

| Librería | Versión |
|---|---|
| Firebase BOM | 32.7.0 |
| Compose BOM | 2024.02.00 |
| Navigation Compose | 2.7.7 |
| Retrofit 2 | 2.9.0 |
| Coil Compose | 2.5.0 |
| Coroutines | 1.7.3 |
| ViewModel Compose | 2.7.0 |

---

## gradle.properties requerido

```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
android.enableJetifier=true
```

---

## Datos de usuario en Firestore

Colección: `/users/{uid}`

```json
{
  "uid": "string",
  "nombre": "string",
  "apellidoPaterno": "string",
  "apellidoMaterno": "string",
  "username": "string",
  "email": "string"
}
```

---

