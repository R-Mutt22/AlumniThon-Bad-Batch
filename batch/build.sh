#!/bin/bash

# Script de build para Render
# Este script se ejecuta durante el despliegue

echo "🚀 Iniciando build de SkillLink..."

# Limpiar y compilar
echo "📦 Limpiando y compilando..."
./mvnw clean package -DskipTests

echo "✅ Build completado exitosamente!"

# Mostrar información del JAR generado
echo "📄 Información del JAR:"
ls -la target/*.jar

echo "🎉 Listo para despliegue!"
