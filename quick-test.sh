#!/bin/bash

# Script simplificado para probar endpoints de SkillLink
BASE_URL="https://alumnithon-bad-batch-backend.onrender.com"
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcklkIjoxLCJyb2xlIjoiREVWRUxPUEVSIiwiaWF0IjoxNzUwODk1MTQwLCJleHAiOjE3NTE3NTkxNDB9._muXiRGjtjSk6aSJYyHpB-UK4XxeXD2FRtDfmWZkPFM"

echo "🚀 Pruebas rápidas de endpoints SkillLink"
echo "========================================"

# Función para hacer peticiones
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo ""
    echo "🔸 $description"
    echo "   $method $endpoint"
    
    if [ -n "$data" ]; then
        result=$(curl -s -X $method "$BASE_URL$endpoint" \
            -H "Authorization: Bearer $TOKEN" \
            -H "Content-Type: application/json" \
            -d "$data" 2>/dev/null)
    else
        result=$(curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL$endpoint" 2>/dev/null)
    fi
    
    # Verificar si la respuesta es válida
    if echo "$result" | grep -q "error\|Error"; then
        echo "   ❌ ERROR: $(echo "$result" | grep -o '"message":"[^"]*"' | cut -d'"' -f4)"
    elif [ -z "$result" ]; then
        echo "   ⚠️  Sin respuesta"
    else
        echo "   ✅ OK"
        echo "$result" | head -c 100
        if [ ${#result} -gt 100 ]; then
            echo "..."
        fi
    fi
}

# Pruebas básicas
test_endpoint "GET" "/" "" "Endpoint raíz"
test_endpoint "GET" "/actuator/health" "" "Health check (sin token)"
test_endpoint "GET" "/api/users" "" "Obtener usuarios"

# Pruebas de perfiles
test_endpoint "GET" "/api/profiles" "" "Obtener perfiles"
test_endpoint "GET" "/api/profiles/1" "" "Obtener perfil ID 1"

# Pruebas de contenido
test_endpoint "GET" "/api/contents" "" "Obtener contenidos"

# Prueba de creación de contenido
test_endpoint "POST" "/api/contents" '{
    "title": "Test Challenge",
    "description": "Challenge de prueba",
    "difficulty": "BEGINNER",
    "requiredTechnologies": ["JAVA", "SPRING_BOOT"],
    "maxParticipants": 10,
    "startDate": "2025-07-01T09:00:00Z",
    "endDate": "2025-07-07T23:59:59Z",
    "type": "CHALLENGE",
    "problemStatement": "Resolver problema de prueba",
    "acceptanceCriteria": "Funciona correctamente",
    "allowsTeams": true,
    "challengeType": "CODING",
    "creatorId": 1,
    "status": "DRAFT"
}' "Crear contenido"

# Pruebas de mensajes
test_endpoint "GET" "/api/messages" "" "Obtener mensajes"

echo ""
echo "🏁 Pruebas completadas"
