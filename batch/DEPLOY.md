# Instrucciones de Despliegue en Render

## Configuración en Render

### 1. Variables de Entorno Requeridas

Configura estas variables de entorno en tu panel de Render:

```bash
# Base de datos
DB_URL=tu_url_de_supabase
DB_USERNAME=tu_usuario_db
DB_PASSWORD=tu_password_db

# JWT
JWT_SECRET=tu_jwt_secret_seguro

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Puerto (Render lo configura automáticamente)
PORT=8080
```

### 2. Configuración del Servicio Web en Render

- **Environment**: Docker
- **Docker Build Context Directory**: `.` (raíz del repositorio)
- **Dockerfile Path**: `./Dockerfile`
- **Health Check Path**: `/api/keep-alive/ping`

### 3. Configuración Alternativa (Sin Docker)

Si prefieres no usar Docker, puedes usar estas configuraciones:

- **Environment**: Java
- **Build Command**: `cd batch && ./mvnw clean package -DskipTests`
- **Start Command**: `cd batch && java -Dspring.profiles.active=prod -Dserver.port=$PORT -jar target/*.jar`

### 4. Keep-Alive Automático

El sistema incluye un endpoint de keep-alive que se ejecuta automáticamente cada 5 minutos:
- Endpoint: `https://tu-app.onrender.com/api/keep-alive/ping`
- Status: `https://tu-app.onrender.com/api/keep-alive/status`

### 5. Comandos Locales para Probar Docker (si tienes Docker instalado)

```bash
# Construir la imagen desde la raíz del proyecto
docker build -t skilllink .

# Ejecutar localmente
docker run -p 8080:8080 \
  -e DB_URL=tu_url \
  -e DB_USERNAME=tu_usuario \
  -e DB_PASSWORD=tu_password \
  -e JWT_SECRET=tu_secret \
  -e SPRING_PROFILES_ACTIVE=prod \
  skilllink

# Probar health check
curl http://localhost:8080/api/keep-alive/ping
```

### 6. URLs de la Aplicación Desplegada

Una vez desplegado, tendrás acceso a:
- API: `https://tu-app.onrender.com/api/`
- Swagger UI: `https://tu-app.onrender.com/swagger-ui`
- Keep-Alive: `https://tu-app.onrender.com/api/keep-alive/ping`
- Health Check: `https://tu-app.onrender.com/actuator/health`

### 7. Monitoreo

El keep-alive se ejecutará automáticamente y puedes monitorear el estado en:
`https://tu-app.onrender.com/api/keep-alive/status`

### 8. Estructura de Archivos

```
tu-repositorio/
├── Dockerfile          # En la raíz
├── .dockerignore       # En la raíz
├── batch/              # Código de la aplicación
│   ├── src/
│   ├── pom.xml
│   └── ...
└── README.md
```
