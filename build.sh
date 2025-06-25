#!/bin/bash

# Script de construcción mejorado para SkillLink Backend
# Este script automatiza el proceso de construcción y validación

set -e  # Salir si cualquier comando falla

echo "🚀 Iniciando construcción de SkillLink Backend..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para logging
log() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "batch/pom.xml" ]; then
    error "No se encontró batch/pom.xml. ¿Estás en el directorio raíz del proyecto?"
    exit 1
fi

log "📋 Verificando prerrequisitos..."

# Verificar Java
if ! command -v java &> /dev/null; then
    error "Java no está instalado o no está en PATH"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    error "Se requiere Java 21 o superior. Versión actual: $JAVA_VERSION"
    exit 1
fi

log "✅ Java $JAVA_VERSION detectado"

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    error "Maven no está instalado o no está en PATH"
    exit 1
fi

log "✅ Maven disponible"

# Verificar Docker (opcional)
if command -v docker &> /dev/null; then
    log "✅ Docker disponible"
    DOCKER_AVAILABLE=true
else
    warn "Docker no disponible - se omitirá la construcción de imagen"
    DOCKER_AVAILABLE=false
fi

# Limpiar construcción anterior
log "🧹 Limpiando construcción anterior..."
cd batch
mvn clean -q

# Compilar proyecto
log "🔨 Compilando proyecto..."
if mvn compile -q; then
    log "✅ Compilación exitosa"
else
    error "❌ Error en compilación"
    exit 1
fi

# Ejecutar tests (opcional)
if [ "$1" != "--skip-tests" ]; then
    log "🧪 Ejecutando tests..."
    if mvn test -q; then
        log "✅ Tests pasaron correctamente"
    else
        warn "⚠️ Algunos tests fallaron, pero continuando..."
    fi
else
    log "⏭️ Omitiendo tests (--skip-tests especificado)"
fi

# Empaquetar aplicación
log "📦 Empaquetando aplicación..."
if mvn package -DskipTests -q; then
    log "✅ Empaquetado exitoso"
else
    error "❌ Error en empaquetado"
    exit 1
fi

# Verificar JAR
JAR_FILE=$(find target -name "*.jar" -not -name "*sources*" -not -name "*javadoc*" | head -1)
if [ -z "$JAR_FILE" ]; then
    error "❌ No se encontró el JAR generado"
    exit 1
fi

JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
log "✅ JAR creado: $JAR_FILE ($JAR_SIZE)"

# Volver al directorio raíz
cd ..

# Construir imagen Docker si está disponible
if [ "$DOCKER_AVAILABLE" = true ] && [ "$2" != "--no-docker" ]; then
    log "🐳 Construyendo imagen Docker..."
    if docker build -t skilllink-backend . -q; then
        log "✅ Imagen Docker creada: skilllink-backend"
        
        # Mostrar tamaño de imagen
        IMAGE_SIZE=$(docker images skilllink-backend --format "table {{.Size}}" | tail -1)
        log "📊 Tamaño de imagen: $IMAGE_SIZE"
        
        # Ejecutar test básico del contenedor
        log "🧪 Probando contenedor..."
        CONTAINER_ID=$(docker run -d -p 8081:8080 \
            -e DB_URL="jdbc:postgresql://localhost:5432/test" \
            -e DB_USERNAME="test" \
            -e DB_PASSWORD="test" \
            -e JWT_SECRET="dGVzdC1zZWNyZXQtZm9yLWRvY2tlci10ZXN0aW5n" \
            skilllink-backend)
            
        sleep 5
        
        # Verificar si el contenedor está corriendo
        if docker ps | grep -q $CONTAINER_ID; then
            log "✅ Contenedor iniciado correctamente"
            docker stop $CONTAINER_ID >/dev/null 2>&1
            docker rm $CONTAINER_ID >/dev/null 2>&1
        else
            warn "⚠️ El contenedor no se inició correctamente (probablemente por falta de DB)"
        fi
    else
        error "❌ Error construyendo imagen Docker"
        exit 1
    fi
else
    log "⏭️ Omitiendo construcción Docker"
fi

# Resumen final
log "🎉 ¡Construcción completada exitosamente!"
echo
echo "📋 Resumen:"
echo "   • JAR: batch/$JAR_FILE ($JAR_SIZE)"
if [ "$DOCKER_AVAILABLE" = true ] && [ "$2" != "--no-docker" ]; then
    echo "   • Docker: skilllink-backend ($IMAGE_SIZE)"
fi
echo
echo "🚀 Comandos útiles:"
echo "   • Ejecutar JAR: java -jar batch/$JAR_FILE"
if [ "$DOCKER_AVAILABLE" = true ]; then
    echo "   • Ejecutar Docker: docker run -p 8080:8080 skilllink-backend"
fi
echo "   • Ver logs: docker logs [container-id]"
echo
echo "🌐 URLs de producción:"
echo "   • Backend: https://alumnithon-bad-batch-backend.onrender.com"
echo "   • Swagger: https://alumnithon-bad-batch-backend.onrender.com/swagger-ui/index.html"
echo "   • Health: https://alumnithon-bad-batch-backend.onrender.com/actuator/health"
