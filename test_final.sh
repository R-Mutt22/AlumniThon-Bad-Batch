#!/bin/bash

# =============================================================================
# SCRIPT DE TEST COMPLETO Y OPTIMIZADO PARA SKILLLINK API
# =============================================================================
# Versión final que maneja todos los endpoints y casos edge
# =============================================================================
# =============================================================================

BASE_URL="https://alumnithon-bad-batch-backend.onrender.com"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m'

# Contadores globales
TOTAL_TESTS=0
PASSED_TESTS=0
JWT_TOKEN=""
USER_ID=""
CONTENT_ID=""

print_header() {
    echo -e "\n${BOLD}${BLUE}════════════════════════════════════════${NC}"
    echo -e "${BOLD}${BLUE}$1${NC}"
    echo -e "${BOLD}${BLUE}════════════════════════════════════════${NC}"
}

print_section() {
    echo -e "\n${CYAN}── $1 ──${NC}"
}

print_test() {
    echo -e "\n${YELLOW}[TEST $((TOTAL_TESTS + 1))]${NC} $1"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
}

print_success() {
    echo -e "${GREEN}✅ PASS:${NC} $1"
    PASSED_TESTS=$((PASSED_TESTS + 1))
}

print_error() {
    echo -e "${RED}❌ FAIL:${NC} $1"
}

print_info() {
    echo -e "${CYAN}ℹ️  INFO:${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠️  WARN:${NC} $1"
}

# Función principal de test que acepta múltiples códigos de estado esperados
test_endpoint() {
    local method=$1
    local endpoint=$2
    local expected_status=$3
    local description=$4
    local data=$5
    local extra_headers=$6
    
    print_test "$description"
    
    # Construir headers
    local headers="-H 'Content-Type: application/json'"
    if [ -n "$JWT_TOKEN" ]; then
        headers="$headers -H 'Authorization: Bearer $JWT_TOKEN'"
    fi
    if [ -n "$extra_headers" ]; then
        headers="$headers $extra_headers"
    fi
    
    # Construir comando curl
    local curl_cmd="curl -s -w '%{http_code}' --max-time 10"
    curl_cmd="$curl_cmd $headers"
    
    if [ -n "$data" ]; then
        curl_cmd="$curl_cmd -d '$data'"
    fi
    
    curl_cmd="$curl_cmd -X $method '$BASE_URL$endpoint'"
    
    # Ejecutar request
    local response=$(eval $curl_cmd 2>/dev/null)
    if [ $? -ne 0 ]; then
        print_error "$method $endpoint - Timeout o error de conexión"
        return 1
    fi
    
    local status_code="${response: -3}"
    local body="${response%???}"
    
    # Verificar resultado (puede aceptar múltiples códigos separados por |)
    if [[ "$expected_status" == *"|"* ]]; then
        # Múltiples códigos esperados
        if [[ "|$expected_status|" == *"|$status_code|"* ]]; then
            print_success "$method $endpoint → $status_code"
            if [ ${#body} -gt 0 ] && [ ${#body} -lt 500 ]; then
                echo "   📄 Response: $(echo "$body" | head -c 120)..."
            fi
        else
            print_error "$method $endpoint → Expected: $expected_status, Got: $status_code"
            if [ ${#body} -gt 0 ]; then
                echo "   📄 Error: $(echo "$body" | head -c 150)..."
            fi
        fi
    else
        # Un solo código esperado
        if [ "$status_code" == "$expected_status" ]; then
            print_success "$method $endpoint → $status_code"
            if [ ${#body} -gt 0 ] && [ ${#body} -lt 500 ]; then
                echo "   📄 Response: $(echo "$body" | head -c 120)..."
            fi
        else
            print_error "$method $endpoint → Expected: $expected_status, Got: $status_code"
            if [ ${#body} -gt 0 ]; then
                echo "   📄 Error: $(echo "$body" | head -c 150)..."
            fi
        fi
    fi
    
    # Extraer datos útiles
    extract_data "$endpoint" "$status_code" "$body" "$method"
    
    sleep 0.2
}

# Función para extraer datos de las respuestas
extract_data() {
    local endpoint=$1
    local status_code=$2
    local body=$3
    local method=$4
    
    case "$endpoint" in
        "/api/auth/register"|"/api/auth/login")
            if [ "$status_code" == "200" ] || [ "$status_code" == "201" ]; then
                JWT_TOKEN=$(echo "$body" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)
                if [ -n "$JWT_TOKEN" ]; then
                    print_info "JWT Token obtenido: ${JWT_TOKEN:0:30}..."
                fi
            fi
            ;;
        "/api/auth")
            if [ "$status_code" == "200" ]; then
                USER_ID=$(echo "$body" | grep -o '"id":[0-9]*' | cut -d':' -f2)
                if [ -n "$USER_ID" ]; then
                    print_info "User ID: $USER_ID"
                fi
            fi
            ;;
        "/api/contents")
            if [ "$status_code" == "200" ] && [ "$method" == "POST" ]; then
                CONTENT_ID=$(echo "$body" | grep -o '"id":[0-9]*' | cut -d':' -f2)
                if [ -n "$CONTENT_ID" ]; then
                    print_info "Content ID creado: $CONTENT_ID"
                fi
            fi
            ;;
    esac
}

# =============================================================================
# DATOS DE TEST
# =============================================================================

setup_test_data() {
    # Usuario de test
    TEST_USER='{
        "firstName": "TestUser",
        "lastName": "Automated",
        "email": "automated.test@skilllink.com",
        "password": "AutoTest123!",
        "role": "DEVELOPER"
    }'

    TEST_LOGIN='{
        "email": "automated.test@skilllink.com",
        "password": "AutoTest123!"
    }'

    # Perfil simplificado - corregir campo isPublic -> visibility
    TEST_PROFILE='{
        "firstName": "TestUser",
        "lastName": "Automated",
        "bio": "Usuario creado por test automatizado",
        "experienceLevel": "INTERMEDIATE",
        "location": "Test City, Test Country",
        "technologies": ["JAVA", "SPRING_BOOT"],
        "interests": ["BACKEND", "TESTING"],
        "githubUrl": "https://github.com/testuser",
        "visibility": "PUBLIC"
    }'

    # Contenido completo con todos los campos requeridos
    TEST_CONTENT='{
        "title": "Automated Test Challenge",
        "description": "Challenge creado automáticamente para testing",
        "type": "CHALLENGE",
        "difficulty": "INTERMEDIATE",
        "maxParticipants": 5,
        "requiredTechnologies": ["JAVA", "SPRING_BOOT"],
        "startDate": "2025-06-27T10:00:00",
        "endDate": "2025-07-04T23:59:59",
        "problemStatement": "Create a simple REST API using Spring Boot that manages a list of tasks. The API should support CRUD operations.",
        "acceptanceCriteria": "1. API should have endpoints for GET, POST, PUT, DELETE\n2. Should use proper HTTP status codes\n3. Should include basic validation",
        "allowsTeams": false,
        "challengeType": "CODING"
    }'

    # Mensaje de test
    TEST_MESSAGE='{
        "content": "Hello from automated test system",
        "type": "TEXT"
    }'
}

# =============================================================================
# FUNCIONES DE TEST ESPECÍFICAS
# =============================================================================

test_public_endpoints() {
    print_section "Endpoints Públicos"
    
    test_endpoint "GET" "/api/keep-alive/ping" "200" "Ping del servidor"
    test_endpoint "GET" "/api/keep-alive/status" "200" "Estado detallado del servidor"
    test_endpoint "GET" "/v3/api-docs" "200" "Documentación OpenAPI"
}

test_authentication() {
    print_section "Autenticación"
    
    # Intentar registro (puede ser 201 si es nuevo o 409 si ya existe)
    test_endpoint "POST" "/api/auth/register" "201|409" "Registro de usuario (nuevo o existente)" "$TEST_USER"
    
    # Si no tenemos token, intentar login
    if [ -z "$JWT_TOKEN" ]; then
        print_warning "Registro falló, intentando login con usuario existente..."
        test_endpoint "POST" "/api/auth/login" "200" "Login de usuario" "$TEST_LOGIN"
    fi
    
    if [ -n "$JWT_TOKEN" ]; then
        test_endpoint "GET" "/api/auth" "200" "Obtener datos del usuario autenticado"
    else
        print_error "No se pudo obtener token JWT. Tests autenticados se saltarán."
    fi
}

test_authenticated_endpoints() {
    if [ -z "$JWT_TOKEN" ]; then
        print_warning "Sin JWT token - Saltando tests autenticados"
        return
    fi
    
    print_section "Endpoints con Autenticación"
    
    # Users
    test_endpoint "GET" "/api/users" "200" "Obtener lista de usuarios"
    
    # Profile endpoints
    test_endpoint "GET" "/api/profiles/technologies" "200" "Obtener tecnologías válidas"
    test_endpoint "GET" "/api/profiles/interests" "200" "Obtener intereses válidos"
    
    # Mi perfil (puede existir o no)
    test_endpoint "GET" "/api/profiles/me" "200|404" "Obtener mi perfil (existente o no)"
    
    # Contents
    test_endpoint "GET" "/api/contents" "200" "Listar contenidos"
    test_endpoint "GET" "/api/contents/search" "200" "Buscar contenidos"
    
    # Messages
    test_endpoint "GET" "/api/messages/conversations" "200" "Obtener conversaciones"
    test_endpoint "GET" "/api/messages/unread/count" "200" "Contar mensajes no leídos"
}

test_data_creation() {
    if [ -z "$JWT_TOKEN" ]; then
        print_warning "Sin JWT token - Saltando creación de datos"
        return
    fi
    
    print_section "Creación de Datos"
    
    # Crear perfil (201 si es nuevo, 409 si ya existe)
    test_endpoint "POST" "/api/profiles" "201|409" "Crear perfil de usuario (nuevo o existente)" "$TEST_PROFILE"
    
    # Intentar crear perfil duplicado para verificar el manejo de conflictos
    test_endpoint "POST" "/api/profiles" "409" "Intentar crear perfil duplicado (esperando 409)" "$TEST_PROFILE"
    
    # Crear contenido
    test_endpoint "POST" "/api/contents" "200" "Crear contenido de test" "$TEST_CONTENT"
    
    # Si se creó contenido, probar operaciones adicionales
    if [ -n "$CONTENT_ID" ]; then
        test_endpoint "GET" "/api/contents/$CONTENT_ID" "200" "Obtener contenido creado"
        test_endpoint "PUT" "/api/contents/$CONTENT_ID/publish?userId=${USER_ID:-1}" "200" "Publicar contenido"
    fi
}

test_advanced_features() {
    if [ -z "$JWT_TOKEN" ]; then
        return
    fi
    
    print_section "Funcionalidades Avanzadas"
    
    # Profile search
    test_endpoint "GET" "/api/profiles/search?query=test&page=0&size=5" "200" "Búsqueda de perfiles"
    
    # Message endpoints
    test_endpoint "GET" "/api/messages/direct/1?page=0&size=10" "200" "Mensajes directos"
    test_endpoint "POST" "/api/messages/test/send" "200" "Enviar mensaje de test" "$TEST_MESSAGE"
    
    # Content endpoints con ID fijo (para probar endpoints existentes)
    test_endpoint "GET" "/api/contents/1/participants" "200" "Obtener participantes (ID fijo)"
    test_endpoint "POST" "/api/contents/1/join?userId=${USER_ID:-1}" "200" "Unirse a contenido (ID fijo)"
}

# =============================================================================
# FUNCIÓN PRINCIPAL
# =============================================================================

main() {
    print_header "🧪 SKILLLINK API - TEST SUITE COMPLETO"
    echo -e "${BLUE}Base URL:${NC} $BASE_URL"
    echo -e "${BLUE}Fecha:${NC} $(date)"
    echo -e "${BLUE}Timeout por request:${NC} 10 segundos"
    
    # Setup
    setup_test_data
    
    # Ejecutar tests
    test_public_endpoints
    test_authentication
    test_authenticated_endpoints
    test_data_creation
    test_advanced_features
    
    # Resumen final
    print_header "📊 RESUMEN DE RESULTADOS"
    
    local failed_tests=$((TOTAL_TESTS - PASSED_TESTS))
    local success_percentage=$((PASSED_TESTS * 100 / TOTAL_TESTS))
    
    echo -e "${BLUE}Total de tests ejecutados:${NC} $TOTAL_TESTS"
    echo -e "${GREEN}Tests exitosos:${NC} $PASSED_TESTS"
    echo -e "${RED}Tests fallidos:${NC} $failed_tests"
    echo -e "${CYAN}Porcentaje de éxito:${NC} $success_percentage%"
    
    if [ $success_percentage -ge 80 ]; then
        echo -e "\n${GREEN}🎉 ¡EXCELENTE! La API está funcionando correctamente${NC}"
    elif [ $success_percentage -ge 60 ]; then
        echo -e "\n${YELLOW}⚠️  ACEPTABLE - Algunos endpoints tienen problemas menores${NC}"
    else
        echo -e "\n${RED}❌ CRÍTICO - Muchos endpoints están fallando${NC}"
    fi
    
    if [ -n "$JWT_TOKEN" ]; then
        echo -e "\n${GREEN}✅ Sistema de autenticación: FUNCIONANDO${NC}"
    else
        echo -e "\n${RED}❌ Sistema de autenticación: CON PROBLEMAS${NC}"
    fi
    
    echo -e "\n${BLUE}📋 Endpoints probados por categoría:${NC}"
    echo "   • Keep Alive: 2 endpoints"
    echo "   • Autenticación: 3 endpoints"
    echo "   • Usuarios: 1 endpoint"
    echo "   • Perfiles: 4+ endpoints"
    echo "   • Contenidos: 5+ endpoints"
    echo "   • Mensajes: 3+ endpoints"
    echo "   • Documentación: 1 endpoint"
    
    echo -e "\n${CYAN}📝 Nota:${NC} Algunos fallos son esperados (ej. 404 cuando no existen datos)"
    
    exit $([ $success_percentage -lt 60 ] && echo 1 || echo 0)
}

# Ejecutar main
main "$@"
