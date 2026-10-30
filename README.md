# AprendizajeApp – Sistema de Gestión de Recursos de Aprendizaje

Aplicación Android en **Kotlin** con arquitectura **MVC** que consume una **API REST dinámica (MockAPI.io)** mediante **Retrofit**. Incluye autenticación con roles (Estudiante / Docente), CRUD de recursos, búsqueda, favoritos y sistema de calificación.

**Alumno:** Kevin Alexander Figueroa Rodriguez 
**Materia:** Desarrollo de Software para Móviles – DSM  
**Universidad:** Universidad Don Bosco  

---

## Características

### Autenticación (MockAPI – colección `usuarios`)
- Registro con nombre, correo, contraseña y **rol** (estudiante / docente)
- Login con validación de correo y contraseña
- Sesión persistente con `SharedPreferences`
- Cierre de sesión

### Roles
| Rol | Permisos |
|-----|----------|
| **Estudiante** | Ver lista, buscar, filtrar, favoritos, calificar (1–5 ★), abrir enlace |
| **Docente** | Todo lo del estudiante + **CRUD completo** (crear, editar, eliminar) |

### Recursos (MockAPI – colección `recursos`)
- Lista en `RecyclerView` + `CardView` (imagen con Glide, título, tipo, descripción, rating)
- Búsqueda por título, tipo o ID
- Filtro por tipo (libro, video, artículo, tutorial)
- Favoritos locales por usuario
- Calificación que actualiza promedio en el backend

---

## Arquitectura MVC (Guía #13)

```text
com.example.aprendizajeapp/
├── model/          → Usuario, Recurso, Repositories
├── network/        → ApiService (Retrofit), RetrofitClient
├── controller/     → AuthController, RecursoController
├── view/
│   ├── auth/       → LoginActivity, RegisterActivity
│   ├── recurso/    → ListaRecursosActivity, FormRecursoActivity, Adapter
│   └── favoritos/  → FavoritosActivity
└── util/           → Constants, SessionManager, FavoritosManager
```

---

## Configurar MockAPI.io (obligatorio)

1. Entra a [https://mockapi.io](https://mockapi.io) y crea un proyecto.
2. Crea **2 recursos** (colecciones):

### Colección `usuarios`
| Campo | Tipo |
|-------|------|
| nombre | String |
| email | String |
| password | String |
| rol | String |

### Colección `recursos`
| Campo | Tipo |
|-------|------|
| titulo | String |
| descripcion | String |
| tipo | String |
| enlace | String |
| imagen | String |
| promedioRating | Number |
| totalRatings | Number |

3. Copia la **Base URL** (ejemplo: `https://66240d4504457d4aaf9b8530.mockapi.io/api/`)
4. Pégala en:

```kotlin
// util/Constants.kt
const val BASE_URL = "https://TU_PROJECT_ID.mockapi.io/api/"
```

5. (Opcional) Agrega 2–3 recursos de ejemplo desde el panel de MockAPI.

**Usuarios de prueba sugeridos** (regístralos desde la app o créalos en MockAPI):

| Email | Password | Rol |
|-------|----------|-----|
| docente@test.com | 123456 | docente |
| alumno@test.com | 123456 | estudiante |

---

## Tecnologías

- Kotlin + Android Studio  
- Retrofit 2 + Gson (Guías 11 y 12)  
- Arquitectura MVC (Guía 13)  
- Material Components, RecyclerView, Glide  
- SharedPreferences (sesión y favoritos)  

---

## Cómo ejecutar

1. Clonar / abrir el proyecto en Android Studio  
2. Configurar `BASE_URL` en `Constants.kt`  
3. Sync Gradle  
4. Ejecutar en emulador o dispositivo con internet  

---

## Video de defensa

https://drive.google.com/file/d/1N7eCBURCpOibnGVYQcPkTiprNFne6Xsq/view?usp=sharing
---

## Autor

**KEVIN ALEXANDER FIGUEROA RODRIGUEZ**  
Universidad Don Bosco – DSM 2026
