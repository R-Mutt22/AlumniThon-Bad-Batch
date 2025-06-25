# AlumniThon SkillLink Backend

Backend de la aplicación SkillLink desarrollada para el AlumniThon. Una plataforma de networking que conecta estudiantes y profesionales.

## 🚀 Despliegue en Producción

**Backend URL:** https://alumnithon-bad-batch-backend.onrender.com

### Endpoints Principales

- **API Base:** `https://alumnithon-bad-batch-backend.onrender.com/api`
- **Health Check:** `https://alumnithon-bad-batch-backend.onrender.com/actuator/health`
- **Keep Alive:** `https://alumnithon-bad-batch-backend.onrender.com/api/keep-alive/ping`
- **Swagger UI:** `https://alumnithon-bad-batch-backend.onrender.com/swagger-ui/index.html`
- **OpenAPI Docs:** `https://alumnithon-bad-batch-backend.onrender.com/v3/api-docs`

## 🛠️ Tecnologías

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.3.1** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Acceso a datos
- **PostgreSQL** - Base de datos
- **JWT** - Tokens de autenticación
- **Swagger/OpenAPI** - Documentación de API
- **Docker** - Containerización
- **Maven** - Gestión de dependencias

## 📁 Estructura del Proyecto

```
batch/
├── src/main/java/com/bad/batch/
│   ├── SkillLinkApplication.java      # Clase principal
│   ├── config/                        # Configuraciones
│   ├── controller/                    # Controladores REST
│   ├── dto/                          # DTOs y mappers
│   ├── model/                        # Entidades JPA
│   ├── repository/                   # Repositorios
│   ├── service/                      # Lógica de negocio
│   └── websocket/                    # Configuración WebSocket
├── src/main/resources/
│   └── application.properties        # Configuración única
└── pom.xml                          # Dependencias Maven
```

## 🔧 Desarrollo Local

### Prerrequisitos

- Java 21 o superior
- Maven 3.6+
- PostgreSQL 12+
- Docker (opcional)

### Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone [repository-url]
   cd alumnithon-bad-batch-backend
   ```

2. **Configurar variables de entorno:**
   ```bash
   # Crear archivo .env en la carpeta batch/
   DB_URL=jdbc:postgresql://localhost:5432/skilllink
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_password
   JWT_SECRET=tu_jwt_secret_base64
   ```
   
   📋 Ver [`ENVIRONMENT_VARIABLES.md`](ENVIRONMENT_VARIABLES.md) para la lista completa de variables disponibles.

3. **Ejecutar la aplicación:**
   ```bash
   cd batch
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación:**
   - API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - Health Check: http://localhost:8080/actuator/health

## 🐳 Docker

### Construir y ejecutar:
```bash
# Construir imagen
docker build -t skilllink-backend .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://host:port/database" \
  -e DB_USERNAME="username" \
  -e DB_PASSWORD="password" \
  -e JWT_SECRET="your_secret" \
  skilllink-backend
```

### Script de construcción automatizado:
```bash
# Construcción completa con tests y Docker
./build.sh

# Solo construcción, sin tests ni Docker
./build.sh --skip-tests --no-docker
```

## 📚 Documentación

- 🔧 [`ENVIRONMENT_VARIABLES.md`](ENVIRONMENT_VARIABLES.md) - Variables de entorno y configuración
- 🚀 [`batch/DEPLOY.md`](batch/DEPLOY.md) - Guía de despliegue detallada  
- 💚 [`KEEP_ALIVE_CONFIG.md`](KEEP_ALIVE_CONFIG.md) - Configuración del servicio keep-alive
- 🛠️ [`DEPLOYMENT_TROUBLESHOOTING.md`](DEPLOYMENT_TROUBLESHOOTING.md) - Solución de problemas
- 📋 [`CHANGES_SUMMARY.md`](CHANGES_SUMMARY.md) - Resumen de cambios recientes

## 🔄 Keep-Alive Service

El backend incluye un servicio de keep-alive que:
- Evita que Render suspenda el servicio por inactividad
- Hace ping cada 14 minutos al endpoint público
- Proporciona endpoints de salud para monitoreo
- Configuración automática con variables de entorno

## 🛡️ Seguridad

- Autenticación basada en JWT
- Passwords hasheados con BCrypt  
- Configuración CORS para desarrollo y producción
- Validación de entrada en todos los endpoints
- Variables de entorno para credenciales sensibles

## 📝 API Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario

### Usuarios  
- `GET /api/users/profile` - Obtener perfil
- `PUT /api/users/profile` - Actualizar perfil

### Keep-Alive & Health
- `GET /api/keep-alive/ping` - Verificar estado del servicio
- `GET /api/keep-alive/status` - Estado detallado
- `GET /actuator/health` - Health check de Spring Boot

📖 **Documentación completa:** https://alumnithon-bad-batch-backend.onrender.com/swagger-ui/index.html

## 🔍 Configuración Simplificada

Este proyecto usa **una sola configuración** (`application.properties`) que se adapta automáticamente al entorno usando variables de entorno:

- **Desarrollo:** Usa valores por defecto y archivo `.env` local
- **Producción:** Usa variables de entorno de Render
- **Docker:** Usa variables pasadas al contenedor

No necesitas perfiles separados ni archivos de configuración múltiples.

## 🚨 Solución de Problemas

Si experimentas problemas:

1. **Error de Hibernate:** Ver [`DEPLOYMENT_TROUBLESHOOTING.md`](DEPLOYMENT_TROUBLESHOOTING.md)
2. **Variables de entorno:** Ver [`ENVIRONMENT_VARIABLES.md`](ENVIRONMENT_VARIABLES.md)  
3. **Keep-alive:** Ver [`KEEP_ALIVE_CONFIG.md`](KEEP_ALIVE_CONFIG.md)
4. **Cambios recientes:** Ver [`CHANGES_SUMMARY.md`](CHANGES_SUMMARY.md)

## 👥 Equipo

Desarrollado para el AlumniThon por el equipo **Bad Batch**.

