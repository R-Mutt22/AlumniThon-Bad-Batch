#!/bin/bash

# Script para probar todos los endpoints de SkillLink
BASE_URL="https://alumnithon-bad-batch-backend.onrender.com"
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1IiwidXNlcklkIjo1LCJyb2xlIjoiREVWRUxPUEVSIiwiaWF0IjoxNzUwODc0NTA5LCJleHAiOjE3NTE3Mzg1MDl9.x-yHJU90pOj54C1mTc6t3-76sb05AOphzn4lcxgweJg"

echo "🚀 Iniciando pruebas de endpoints de SkillLink"
echo "============================================="

# Función para hacer peticiones con mejor formato
make_request() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo ""
    echo "📋 $description"
    echo "🔗 $method $url"
    
    if [ -n "$data" ]; then
        curl -s -X $method "$BASE_URL$url" \
            -H "Authorization: Bearer $TOKEN" \
            -H "Content-Type: application/json" \
            -d "$data" | jq . 2>/dev/null || curl -s -X $method "$BASE_URL$url" \
            -H "Authorization: Bearer $TOKEN" \
            -H "Content-Type: application/json" \
            -d "$data"
    else
        curl -s -X $method "$BASE_URL$url" \
            -H "Authorization: Bearer $TOKEN" \
            -H "Content-Type: application/json" | jq . 2>/dev/null || curl -s -X $method "$BASE_URL$url" \
            -H "Authorization: Bearer $TOKEN" \
            -H "Content-Type: application/json"
    fi
    echo ""
    echo "----------------------------------------"
}

# 1. CONTENIDOS/PUBLICACIONES
echo "📚 PROBANDO CONTENIDOS/PUBLICACIONES"
echo "===================================="

make_request "GET" "/api/contents" "" "Listar todos los contenidos"

make_request "GET" "/api/contents/7" "" "Obtener contenido específico (ID: 7)"

make_request "GET" "/api/contents/search?type=CHALLENGE&page=0&size=10" "" "Buscar challenges"

make_request "POST" "/api/contents" '{
    "title": "Challenge: Calculadora React",
    "description": "Desarrollar una calculadora usando React y TypeScript",
    "difficulty": "INTERMEDIATE",
    "requiredTechnologies": ["JAVASCRIPT", "REACT", "TYPESCRIPT"],
    "maxParticipants": 15,
    "startDate": "2025-07-01T09:00:00",
    "endDate": "2025-07-07T23:59:59",
    "type": "CHALLENGE",
    "problemStatement": "Crear una calculadora que maneje operaciones básicas y avanzadas",
    "acceptanceCriteria": "La calculadora debe ser responsive, manejar errores y tener tests",
    "allowsTeams": true,
    "challengeType": "CODING"
}' "Crear nuevo challenge"

make_request "POST" "/api/contents/7/join?userId=5" "" "Unirse al challenge ID 7"

# 2. MENTORÍAS
echo "🎓 PROBANDO MENTORÍAS"
echo "===================="

make_request "POST" "/api/contents" '{
    "title": "Mentoría: React Hooks Avanzado",
    "description": "Aprende a usar React Hooks de manera efectiva",
    "difficulty": "INTERMEDIATE",
    "requiredTechnologies": ["JAVASCRIPT", "REACT"],
    "maxParticipants": 3,
    "startDate": "2025-07-01T10:00:00",
    "endDate": "2025-07-01T12:00:00",
    "type": "MENTORSHIP",
    "durationMinutes": 120,
    "mentorshipType": "GROUP",
    "isLive": true
}' "Crear nueva mentoría"

# 3. MENSAJES
echo "💬 PROBANDO MENSAJES"
echo "==================="

make_request "GET" "/api/messages/conversations" "" "Obtener conversaciones"

make_request "GET" "/api/messages/unread/count" "" "Contar mensajes no leídos"

make_request "GET" "/api/messages/challenge/7?page=0&size=20" "" "Mensajes del challenge 7"

make_request "POST" "/api/messages/test/send" '{
    "content": "¡Hola! Este es un mensaje de prueba para el challenge",
    "type": "TEXT",
    "challengeId": 7
}' "Enviar mensaje de prueba al challenge"

make_request "POST" "/api/messages/test/create-chat-room?contentId=7" "" "Crear sala de chat para el challenge 7"

echo ""
echo "✅ Pruebas completadas!"
echo "======================="
