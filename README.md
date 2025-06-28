# 🚀 SkillLink - Plataforma de Colaboración para Desarrolladores

<div align="center">

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-Real--time-4B8BBE?style=for-the-badge&logo=websocket&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

[![Deploy Status](https://img.shields.io/badge/Deploy-Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://alumnithon-bad-batch-backend.onrender.com)
[![API Docs](https://img.shields.io/badge/API%20Docs-Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://alumnithon-bad-batch-backend.onrender.com/v3/api-docs)

### **🏆 Proyecto desarrollado para Alumnithon 2025 - Alura Latam**

*Una plataforma innovadora que conecta desarrolladores a través de mentorías personalizadas y desafíos técnicos colaborativos*

</div>

---

## 📖 **Descripción del Proyecto**

**SkillLink** es una plataforma web diseñada para impulsar el crecimiento profesional de desarrolladores mediante la conexión con mentores experimentados y la participación en desafíos técnicos. La aplicación facilita la creación de perfiles técnicos detallados, la búsqueda de mentorías especializadas y la colaboración en tiempo real a través de chats integrados.

### 🎯 **Objetivos Principales**

- **Democratizar el mentoring**: Hacer accesible la mentoría técnica para desarrolladores de todos los niveles
- **Fomentar la colaboración**: Crear un ecosistema donde los desarrolladores puedan aprender unos de otros
- **Impulsar el crecimiento profesional**: Proporcionar herramientas para el desarrollo de habilidades técnicas
- **Conectar talento**: Facilitar la conexión entre desarrolladores experimentados y aquellos en crecimiento

### 👥 **Público Objetivo**

- **Desarrolladores Junior**: Buscan mentoría y oportunidades de aprendizaje
- **Desarrolladores Senior**: Comparten conocimiento y crean contenido educativo
- **Estudiantes de Tecnología**: Practican habilidades en desafíos reales
- **Tech Leads y Mentores**: Ofrecen sesiones de mentoría especializada
- **Equipos de Desarrollo**: Colaboran en proyectos y desafíos grupales

---

## ✨ **Funcionalidades del MVP**

### 🔐 **Sistema de Autenticación**
- Registro e inicio de sesión seguro con JWT
- Roles diferenciados (Developer, Mentor, Admin)
- Autenticación basada en tokens con expiración automática

### 👤 **Gestión de Perfiles**
- Creación de perfiles técnicos detallados
- Configuración de tecnologías y áreas de interés
- Niveles de experiencia (Junior, Intermediate, Senior)
- URLs a GitHub, LinkedIn y portafolios personales
- Control de visibilidad (Público/Privado)

### 🎓 **Sistema de Mentorías**
- Creación de sesiones de mentoría one-on-one y grupales
- Programación de sesiones con fechas y duraciones específicas
- Filtrado por tecnologías y nivel de dificultad
- Sistema de participantes y gestión de cupos

### 🏆 **Desafíos Técnicos**
- Creación de challenges de programación
- Definición de criterios de aceptación y entregables
- Soporte para desafíos individuales y en equipo
- Sistema de envío de soluciones con URLs de repositorios

### 💬 **Chat en Tiempo Real**
- Mensajería directa entre usuarios
- Chats grupales para mentorías
- Salas de chat específicas para desafíos
- Historial completo de conversaciones
- Búsqueda avanzada en mensajes

### 🔍 **Búsqueda y Filtros**
- Búsqueda de contenido por tecnologías
- Filtros por dificultad y tipo de contenido
- Búsqueda de perfiles por habilidades
- Sistema de paginación optimizado

---

## 🛠️ **Arquitectura y Tecnologías**

### **Backend (Spring Boot)**
```
📁 src/main/java/com/bad/batch/
├── 🔐 config/          # Configuración (CORS, Seguridad, OpenAPI)
├── 🎮 controller/      # Controllers REST y documentación Swagger
├── 📊 dto/            # DTOs para requests y responses
├── ⚠️ exceptions/      # Manejo centralizado de excepciones
├── 🗃️ model/          # Entidades JPA y enums
├── 📦 repository/     # Repositorios JPA con consultas personalizadas
├── 🔧 service/        # Lógica de negocio e implementaciones
└── 🌐 websocket/      # WebSocket para chat en tiempo real
```

### **Tecnologías Principales**

| Categoría | Tecnología | Versión | Propósito |
|-----------|------------|---------|-----------|
| **Framework** | Spring Boot | 3.3.1 | Framework principal |
| **Lenguaje** | Java | 21 | Lenguaje de desarrollo |
| **Base de Datos** | PostgreSQL | Latest | Almacenamiento principal |
| **ORM** | Spring Data JPA | - | Mapeo objeto-relacional |
| **Seguridad** | Spring Security | - | Autenticación y autorización |
| **Tokens** | JSON Web Tokens | - | Autenticación stateless |
| **WebSockets** | Spring WebSocket | - | Comunicación en tiempo real |
| **Documentación** | SpringDoc OpenAPI | - | Documentación automática API |
| **Validación** | Bean Validation | - | Validación de datos |
| **Deploy** | Render | - | Plataforma de despliegue |

### **Dependencias Clave**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

---

## 🗃️ **Modelo de Base de Datos**

### **Entidades Principales**

#### 👤 **User** (users)
```sql
- id (PK)
- email (UNIQUE)
- password (encoded)
- firstName, lastName
- role (DEVELOPER/MENTOR/ADMIN)
- isActive
- createdAt, updatedAt
```

#### 👤 **Profile** (profiles)
```sql
- id (PK)
- user_id (FK → User)
- bio, location, githubUrl, linkedinUrl
- experienceLevel (JUNIOR/INTERMEDIATE/SENIOR)
- visibility (PUBLIC/PRIVATE)
- technologies (Collection)
- interests (Collection)
- objectives
```

#### 📚 **Content** (contents) - *Herencia de Tabla Única*
```sql
- id (PK)
- content_type (DISCRIMINATOR)
- title, description
- creator_id (FK → User)
- status, difficulty
- requiredTechnologies (Collection)
- maxParticipants
- startDate, endDate
```

#### 🎓 **Mentorship** (extends Content)
```sql
- durationMinutes
- mentorshipType (ONE_ON_ONE/GROUP)
- isLive
```

#### 🏆 **Challenge** (extends Content)
```sql
- problemStatement
- acceptanceCriteria
- allowsTeams
- challengeType (CODING/DESIGN/ARCHITECTURE)
```

#### 💬 **Message** (messages)
```sql
- id (PK)
- content, type
- sender_id (FK → User)
- recipient_id (FK → User) [para mensajes directos]
- challengeId, mentorshipId [para chats grupales]
- conversationType (DIRECT/CHALLENGE/MENTORSHIP)
- isRead, isDeleted
- createdAt
```

#### 🤝 **Participation** (participations)
```sql
- id (PK)
- user_id (FK → User)
- content_id (FK → Content)
- joinedAt
- status (ACTIVE/COMPLETED/CANCELLED)
```

### **Relaciones Principales**

- **User ↔ Profile**: 1:1 (Un usuario tiene un perfil)
- **User ↔ Content**: 1:N (Un usuario puede crear múltiples contenidos)
- **Content ↔ Participation**: 1:N (Un contenido tiene múltiples participantes)
- **User ↔ Participation**: 1:N (Un usuario puede participar en múltiples contenidos)
- **User ↔ Message**: 1:N (Un usuario puede enviar múltiples mensajes)

---

## 🔧 **Requisitos del Sistema**

### **Requisitos Funcionales**

| RF | Descripción | Prioridad |
|----|-------------|-----------|
| **RF01** | Registro y autenticación de usuarios | 🔴 Crítico |
| **RF02** | Creación y gestión de perfiles técnicos | 🔴 Crítico |
| **RF03** | Creación y gestión de mentorías | 🔴 Crítico |
| **RF04** | Creación y gestión de desafíos técnicos | 🔴 Crítico |
| **RF05** | Sistema de chat en tiempo real | 🟡 Importante |
| **RF06** | Búsqueda y filtrado de contenido | 🟡 Importante |
| **RF07** | Sistema de participación en contenidos | 🔴 Crítico |
| **RF08** | Gestión de entregas para desafíos | 🟡 Importante |
| **RF09** | Notificaciones y mensajería | 🟢 Opcional |
| **RF10** | Dashboard de estadísticas | 🟢 Opcional |

### **Requisitos No Funcionales**

| RNF | Categoría | Descripción | Métricas |
|-----|-----------|-------------|----------|
| **RNF01** | **Seguridad** | Autenticación JWT con expiración | Tokens válidos por 24h |
| **RNF02** | **Seguridad** | Encriptación de contraseñas | BCrypt con salt |
| **RNF03** | **Seguridad** | Protección CORS configurada | Origins específicos |
| **RNF04** | **Rendimiento** | Tiempo de respuesta de API | < 500ms para consultas simples |
| **RNF05** | **Rendimiento** | Consultas optimizadas con JOIN FETCH | Evitar N+1 queries |
| **RNF06** | **Escalabilidad** | Conexión pooling para BD | HikariCP configurado |
| **RNF07** | **Escalabilidad** | Paginación en listados | Máximo 20 items por página |
| **RNF08** | **Usabilidad** | Documentación API automática | Swagger UI integrado |
| **RNF09** | **Usabilidad** | Validación de datos en entrada | Bean Validation |
| **RNF10** | **Compatibilidad** | API REST estándar | HTTP Status codes correctos |
| **RNF11** | **Mantenibilidad** | Logging estructurado | SLF4J + Logback |
| **RNF12** | **Privacidad** | Control de visibilidad de perfiles | Público/Privado |

### **Restricciones Técnicas**

- **Lenguaje**: Java 21 (LTS)
- **Framework**: Spring Boot 3.x
- **Base de Datos**: PostgreSQL (compatible con Supabase)
- **Despliegue**: Render (contenedores Docker)
- **Arquitectura**: Monolito modular con separación clara de responsabilidades

## 🚀 **Instalación y Configuración**

### **Prerrequisitos**

- Java 21+
- Maven 3.8+
- PostgreSQL 12+ o acceso a Supabase
- Git

### **Configuración Local**

1. **Clonar el repositorio**
```bash
git clone https://github.com/R-Mutt22/alumnithon-bad-batch-backend.git
cd alumnithon-bad-batch-backend
```

2. **Configurar variables de entorno**
```bash
# Crear archivo .env en la raíz del proyecto
DB_URL=jdbc:postgresql://localhost:5432/skilllink
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key
```

3. **Configurar application.properties**
```properties
# Base de datos
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

4. **Ejecutar la aplicación**
```bash
cd batch
mvn clean install
mvn spring-boot:run
```

5. **Verificar instalación**
```bash
curl http://localhost:8080/api/keep-alive/ping
# Respuesta esperada: "Servidor activo - [timestamp]"
```

### **Acceso a Documentación**

- **API Docs**: http://localhost:8080/v3/api-docs
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/keep-alive/status

---

## 📚 **Endpoints Principales**

### **🔐 Autenticación**
```http
POST /api/auth/register    # Registro de usuario
POST /api/auth/login       # Inicio de sesión
GET  /api/auth            # Datos del usuario autenticado
```

### **👤 Perfiles**
```http
GET    /api/profiles/me              # Mi perfil
POST   /api/profiles                 # Crear perfil
PUT    /api/profiles                 # Actualizar perfil
GET    /api/profiles/search          # Buscar perfiles
GET    /api/profiles/technologies    # Tecnologías válidas
GET    /api/profiles/interests       # Intereses válidos
```

### **📚 Contenidos**
```http
GET    /api/contents                 # Listar contenidos
POST   /api/contents                 # Crear contenido
GET    /api/contents/{id}            # Obtener contenido
PUT    /api/contents/{id}            # Actualizar contenido
PUT    /api/contents/{id}/publish    # Publicar contenido
POST   /api/contents/{id}/join       # Unirse a contenido
GET    /api/contents/{id}/participants # Listar participantes
```

### **💬 Mensajes**
```http
GET    /api/messages/conversations         # Mis conversaciones
GET    /api/messages/direct/{userId}       # Mensajes directos
GET    /api/messages/challenge/{id}        # Chat de desafío
GET    /api/messages/mentorship/{id}       # Chat de mentoría
GET    /api/messages/search               # Buscar mensajes
POST   /api/messages/test/send            # Enviar mensaje (test)
```

### **🏠 Sistema**
```http
GET    /api/keep-alive/ping         # Ping de conectividad
GET    /api/keep-alive/status       # Estado detallado del servidor
GET    /api/users                   # Listar usuarios (autenticado)
```

---

## 🧪 **Testing**

### **Script de Testing Automatizado**

El proyecto incluye un script completo de testing que valida todos los endpoints:

```bash
# Ejecutar test completo
./test_final.sh

# El script valida:
# ✅ Conectividad del servidor
# ✅ Endpoints públicos
# ✅ Autenticación JWT
# ✅ CRUD de perfiles
# ✅ CRUD de contenidos
# ✅ Sistema de mensajería
# ✅ Búsquedas y filtros
```

### **Cobertura de Testing**

- **Endpoints públicos**: Keep-alive, documentación
- **Autenticación**: Registro, login, validación JWT
- **Perfiles**: Creación, actualización, búsqueda
- **Contenidos**: CRUD completo, publicación, participación
- **Mensajería**: Chats directos, grupales, búsqueda
- **Casos edge**: Manejo de errores, validaciones

---

## 🚀 **Despliegue en Producción**

### **Render Deployment**

El proyecto está desplegado en Render con las siguientes configuraciones:

**URL de Producción**: https://alumnithon-bad-batch-backend.onrender.com

**Variables de Entorno Configuradas**:
```bash
DB_URL=postgresql://supabase_connection_string
DB_USERNAME=postgres
DB_PASSWORD=your_supabase_password
JWT_SECRET=production_jwt_secret
```

**Dockerfile**:
```dockerfile
FROM openjdk:21-jdk-slim
COPY batch/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

**Build Script**:
```bash
#!/bin/bash
cd batch
mvn clean package -DskipTests
```

---

## 🔮 **Roadmap y Mejoras Futuras**

### **Versión 1.1 - Q2 2025**
- [ ] Sistema de notificaciones push
- [ ] Integración con GitHub para validación automática de challenges
- [ ] Dashboard de analytics para mentores
- [ ] Sistema de rating y reviews

### **Versión 1.2 - Q3 2025**
- [ ] Aplicación móvil (React Native)
- [ ] Integración con calendarios (Google Calendar, Outlook)
- [ ] Sistema de badges y gamificación
- [ ] API pública para integraciones

### **Versión 1.3 - Q4 2025**
- [ ] IA para matching automático mentor-estudiante
- [ ] Videoconferencia integrada
- [ ] Marketplace de cursos
- [ ] Certificaciones digitales

### **Mejoras Técnicas**
- [ ] Migración a microservicios
- [ ] Implementación de Redis para caché
- [ ] CI/CD con GitHub Actions
- [ ] Monitoreo con Prometheus + Grafana

---

## 👥 **Equipo de Desarrollo - Bad Batch**

<div align="center">

### 🏆 **Alumnithon 2025 - Alura Latam**

| Desarrollador | GitHub | Rol Principal |
|---------------|--------|---------------|
| **R-Mutt22** | [@R-Mutt22](https://github.com/R-Mutt22) | *Tech Lead & Backend Developer* |
| **Juan Valenzuela** | [@Juan-Valenzuela3](https://github.com/Juan-Valenzuela3) | *Backend Developer* |
| **EV3TH** | [@EV3THlm](https://github.com/EV3THlm) | *Backend Developer* |
| **Diógenes Quintero** | [@dio-quincarDev](https://github.com/dio-quincarDev) | *Backend Developer* |

</div>

### **Contribuciones del Equipo**

- **Backend Architecture**: Diseño de APIs REST y WebSocket
- **Database Design**: Modelado de entidades y relaciones
- **Security Implementation**: JWT, CORS, validaciones
- **Real-time Features**: Chat WebSocket y notificaciones
- **Testing & QA**: Scripts automatizados y testing manual
- **DevOps**: Configuración de despliegue en Render

---

## 📄 **Licencia**

```
MIT License

Copyright (c) 2025 Bad-Batch Team - Alumnithon 2025
```

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

### **¿Qué significa esto?**

- ✅ **Uso libre**: Puedes usar este código para cualquier propósito
- ✅ **Modificación**: Puedes modificar y adaptar el código
- ✅ **Distribución**: Puedes distribuir el código original o modificado
- ✅ **Uso comercial**: Puedes usar este código en proyectos comerciales
- ⚠️ **Atribución**: Debes incluir el aviso de copyright original
- ⚠️ **Sin garantías**: El software se proporciona "tal como está"

---

## 📞 **Contacto y Soporte**

- **🌐 API Endpoint**: https://alumnithon-bad-batch-backend.onrender.com
- **📚 Documentación**: https://alumnithon-bad-batch-backend.onrender.com/v3/api-docs
- **🐛 Issues**: [GitHub Issues](https://github.com/R-Mutt22/alumnithon-bad-batch-backend/issues)
- **🚀 Pull Requests**: [GitHub PRs](https://github.com/R-Mutt22/alumnithon-bad-batch-backend/pulls)

---

<div align="center">

### 🎯 **Construido con ❤️ para la comunidad de desarrolladores**

**SkillLink** - *Conectando talento, construyendo futuro*

[![Made for Alumnithon](https://img.shields.io/badge/Made%20for-Alumnithon%202025-FF6B6B?style=for-the-badge&logo=graduation-cap&logoColor=white)](https://www.alura.com.br/)

</div>