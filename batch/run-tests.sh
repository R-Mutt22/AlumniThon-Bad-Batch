#!/bin/bash

# Script para ejecutar pruebas de integración de SkillLink
# Uso: ./run-tests.sh [test|supabase|all]

set -e

echo "🚀 Ejecutando pruebas de integración de SkillLink..."

# Función para ejecutar pruebas con perfil específico
run_tests_with_profile() {
    local profile=$1
    local description=$2
    
    echo "📋 Ejecutando pruebas con perfil: $profile - $description"
    
    if [ "$profile" == "supabase" ]; then
        if [ ! -f "src/test/resources/application-supabase.properties" ]; then
            echo "❌ Error: archivo application-supabase.properties no encontrado"
            echo "   Copia application-supabase.properties.example y configura tus credenciales"
            exit 1
        fi
    fi
    
    ./mvnw clean test \
        -Dspring.profiles.active=$profile \
        -Dtest="com.bad.batch.integration.*Test" \
        -Dmaven.test.failure.ignore=false
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [opción]"
    echo ""
    echo "Opciones:"
    echo "  test      - Ejecuta pruebas con base de datos H2 en memoria (por defecto)"
    echo "  supabase  - Ejecuta pruebas contra base de datos Supabase"
    echo "  all       - Ejecuta ambos tipos de pruebas"
    echo "  help      - Muestra esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 test"
    echo "  $0 supabase"
    echo "  $0 all"
}

# Leer parámetro de entrada
case "${1:-test}" in
    "test")
        run_tests_with_profile "test" "Base de datos H2 en memoria"
        ;;
    "supabase")
        run_tests_with_profile "supabase" "Base de datos Supabase"
        ;;
    "all")
        echo "🔄 Ejecutando todas las configuraciones de pruebas..."
        run_tests_with_profile "test" "Base de datos H2 en memoria"
        echo ""
        run_tests_with_profile "supabase" "Base de datos Supabase"
        ;;
    "help"|"-h"|"--help")
        show_help
        exit 0
        ;;
    *)
        echo "❌ Opción no válida: $1"
        echo ""
        show_help
        exit 1
        ;;
esac

echo ""
echo "✅ Pruebas completadas exitosamente!"
echo ""
echo "📊 Para ver reportes detallados, revisa:"
echo "   - target/surefire-reports/"
echo "   - target/site/jacoco/index.html (si tienes jacoco configurado)"
