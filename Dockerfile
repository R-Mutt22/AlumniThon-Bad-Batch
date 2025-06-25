# Dockerfile multi-stage para aplicación Spring Boot

# Etapa 1: Build de la aplicación
FROM maven:3.9.4-openjdk-21-slim AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY batch/pom.xml .
COPY batch/.mvn .mvn
COPY batch/mvnw .
COPY batch/mvnw.cmd .

# Descargar dependencias (esto se cachea si el pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY batch/src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con imagen optimizada
FROM openjdk:21-jre-slim

# Crear usuario no-root para seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership al usuario spring
RUN chown spring:spring app.jar

# Cambiar al usuario no-root
USER spring:spring

# Configurar variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8080

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/keep-alive/ping || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
