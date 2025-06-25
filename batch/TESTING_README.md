# 🧪 Guía de Pruebas de Integración - SkillLink

Esta guía te ayudará a ejecutar pruebas completas de todos los endpoints de la aplicación SkillLink utilizando tanto una base de datos H2 en memoria como la base de datos de Supabase.

## 📋 Índice

- [Configuración Inicial](#configuración-inicial)
- [Estructura de Pruebas](#estructura-de-pruebas)
- [Configuración de Base de Datos](#configuración-de-base-de-datos)
- [Ejecución de Pruebas](#ejecución-de-pruebas)
- [Endpoints Probados](#endpoints-probados)
- [Resolución de Problemas](#resolución-de-problemas)

## 🚀 Configuración Inicial

### Prerrequisitos

- Java 21+
- Maven 3.8+
- Base de datos Supabase configurada (opcional)

### Instalación de Dependencias

```bash
cd batch
mvn clean install
```

## 🏗️ Estructura de Pruebas

Las pruebas están organizadas en las siguientes clases:

```
src/test/java/com/bad/batch/integration/
├── AuthIntegrationTest.java          # Pruebas de autenticación
├── UserIntegrationTest.java          # Pruebas de gestión de usuarios  
├── ProfileIntegrationTest.java       # Pruebas de perfiles
├── ContentIntegrationTest.java       # Pruebas de contenidos (mentorías/desafíos)
├── MessageIntegrationTest.java       # Pruebas de mensajería
└── AllIntegrationTestSuite.java      # Documentación de la suite
```

## 🗄️ Configuración de Base de Datos

### H2 (Base de datos en memoria)

Configuración automática para pruebas rápidas. No requiere configuración adicional.

### Supabase (Base de datos de producción)

1. Copia el archivo de configuración:
```bash
cp src/test/resources/application-supabase.properties.example src/test/resources/application-supabase.properties
```

2. Edita `application-supabase.properties` con tus credenciales:
```properties
spring.datasource.url=jdbc:postgresql://db.xxxxxxxxxxxxxxxx.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=tu-password-real
```

## 🏃‍♂️ Ejecución de Pruebas

### Opción 1: Script Automatizado (Recomendado)

```bash
# Pruebas con H2 (rápido)
./run-tests.sh test

# Pruebas con Supabase (requiere configuración)
./run-tests.sh supabase

# Ambas configuraciones
./run-tests.sh all

# Ayuda
./run-tests.sh help
```

### Opción 2: Maven Directo

```bash
# Pruebas con H2
./mvnw test -Dspring.profiles.active=test -Dtest="com.bad.batch.integration.*Test"

# Pruebas con Supabase  
./mvnw test -Dspring.profiles.active=supabase -Dtest="com.bad.batch.integration.*Test"

# Ejecutar una sola clase de prueba
./mvnw test -Dtest="AuthIntegrationTest"
```

### Opción 3: IDE

Ejecuta las clases de prueba directamente desde tu IDE favorito (IntelliJ IDEA, Eclipse, VS Code).

## 🌐 Endpoints Probados

### 🔐 Autenticación (`/api/auth`)

- **POST** `/api/auth/register` - Registro de usuario
- **POST** `/api/auth/login` - Inicio de sesión  
- **GET** `/api/auth` - Obtener información del usuario autenticado

**Casos de prueba:**
- ✅ Registro exitoso
- ✅ Registro con email duplicado
- ✅ Validación de datos inválidos
- ✅ Login exitoso
- ✅ Login con credenciales incorrectas
- ✅ Acceso con token válido/inválido

### 👥 Usuarios (`/api/users`)

- **GET** `/api/users` - Obtener todos los usuarios

**Casos de prueba:**
- ✅ Listado exitoso con autenticación
- ✅ Acceso denegado sin autenticación
- ✅ Listado vacío

### 👤 Perfiles (`/api/profiles`)

- **POST** `/api/profiles` - Crear perfil
- **GET** `/api/profiles/me` - Obtener mi perfil
- **GET** `/api/profiles/{userId}` - Obtener perfil de otro usuario
- **PUT** `/api/profiles/me` - Actualizar mi perfil
- **GET** `/api/profiles/search` - Buscar perfiles
- **GET** `/api/profiles/technologies` - Tecnologías válidas
- **GET** `/api/profiles/interests` - Intereses válidos

**Casos de prueba:**
- ✅ Creación de perfil exitosa
- ✅ Validación de datos
- ✅ Búsqueda con filtros
- ✅ Actualización de perfil
- ✅ Perfiles públicos/privados

### 📚 Contenidos (`/api/contents`)

- **POST** `/api/contents` - Crear contenido (mentoría/desafío)
- **GET** `/api/contents` - Listar contenidos
- **GET** `/api/contents/{id}` - Obtener contenido específico
- **PUT** `/api/contents/{id}` - Actualizar contenido
- **DELETE** `/api/contents/{id}` - Eliminar contenido
- **POST** `/api/contents/{id}/join` - Unirse a contenido
- **DELETE** `/api/contents/{id}/leave` - Abandonar contenido
- **PUT** `/api/contents/{id}/publish` - Publicar contenido
- **GET** `/api/contents/search` - Buscar contenidos

**Casos de prueba:**
- ✅ Creación de mentorías
- ✅ Creación de desafíos
- ✅ Gestión de participantes
- ✅ Publicación de contenidos
- ✅ Búsqueda avanzada

### 💬 Mensajes (`/api/messages`)

- **GET** `/api/messages/direct/{otherUserId}` - Mensajes directos
- **GET** `/api/messages/challenge/{challengeId}` - Mensajes de desafío
- **GET** `/api/messages/mentorship/{mentorshipId}` - Mensajes de mentoría
- **GET** `/api/messages/conversations` - Últimas conversaciones
- **GET** `/api/messages/unread/count` - Contador de no leídos
- **PUT** `/api/messages/{messageId}/read` - Marcar como leído
- **PUT** `/api/messages/conversation/{otherUserId}/read` - Marcar conversación como leída
- **GET** `/api/messages/search` - Buscar mensajes

**Casos de prueba:**
- ✅ Historial de mensajes
- ✅ Búsqueda en conversaciones
- ✅ Gestión de estados de lectura
- ✅ Paginación

## 📊 Resultados de Pruebas

### Ver Reportes

```bash
# Reportes de Surefire
open target/surefire-reports/index.html

# Logs detallados
tail -f target/surefire-reports/*.txt
```

### Métricas Importantes

- **Cobertura de Endpoints**: 100% de los endpoints REST
- **Códigos de Estado**: Todas las respuestas HTTP esperadas
- **Autenticación**: JWT válido/inválido en todos los endpoints protegidos
- **Validación**: Datos válidos e inválidos
- **Base de Datos**: Transacciones y rollbacks automáticos

## 🐛 Resolución de Problemas

### Error: "application-supabase.properties no encontrado"

```bash
cp src/test/resources/application-supabase.properties.example src/test/resources/application-supabase.properties
# Edita el archivo con tus credenciales reales
```

### Error: "Connection refused" con Supabase

1. Verifica tus credenciales en `application-supabase.properties`
2. Asegúrate de que tu IP esté en la whitelist de Supabase
3. Verifica que el proyecto de Supabase esté activo

### Error: "Tests failing with JWT"

1. Verifica que el secreto JWT esté configurado correctamente
2. Revisa que las fechas de los tokens no hayan expirado

### Lentitud en las Pruebas

1. Usa el perfil `test` (H2) para desarrollo rápido
2. Usa el perfil `supabase` solo para pruebas finales
3. Ejecuta pruebas individuales durante desarrollo

## 🎯 Casos de Uso de las Pruebas

### Durante Desarrollo
```bash
# Ejecuta pruebas rápidas mientras desarrollas
./run-tests.sh test
```

### Antes de Deploy
```bash
# Ejecuta pruebas completas contra Supabase
./run-tests.sh supabase
```

### CI/CD Pipeline
```bash
# Ambas configuraciones para máxima confianza
./run-tests.sh all
```

## 🔧 Personalización

### Agregar Nuevas Pruebas

1. Crea una nueva clase en `src/test/java/com/bad/batch/integration/`
2. Extiende la configuración base con `@SpringBootTest` y `@ActiveProfiles("test")`
3. Usa `MockMvc` para simular peticiones HTTP
4. Documenta los nuevos casos en este README

### Configurar Otros Entornos

1. Crea un nuevo perfil en `application-{entorno}.properties`
2. Actualiza el script `run-tests.sh` para incluir el nuevo entorno
3. Documenta la configuración

---

## 📞 Soporte

Si encuentras problemas o tienes preguntas:

1. Revisa los logs en `target/surefire-reports/`
2. Verifica la configuración de base de datos
3. Asegúrate de tener las dependencias correctas
4. Consulta la documentación de la API en `/swagger-ui`

¡Las pruebas están listas para garantizar la calidad de tu aplicación SkillLink! 🚀
