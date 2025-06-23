const WebSocket = require('ws');

const JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMSIsInVzZXJJZCI6MTEsInJvbGUiOiJERVZFTE9QRVIiLCJpYXQiOjE3NTA3MDY0MzMsImV4cCI6MTc1MTU3MDQzM30.V3jIP2pzhHPmnvA1NYn0k21G2ZBB-TibHAgxS_m7AlI";

console.log('🚀 Iniciando pruebas de WebSocket...');
console.log('🔐 Token JWT:', JWT_TOKEN.substring(0, 50) + '...');

// Intentar conexión SockJS WebSocket  
const ws = new WebSocket('ws://localhost:8080/ws/websocket', {
    headers: {
        'Authorization': `Bearer ${JWT_TOKEN}`,
        'Upgrade': 'websocket',
        'Connection': 'Upgrade'
    }
});

ws.on('open', function open() {
    console.log('✅ Conectado al WebSocket con éxito!');
    console.log('� Estado de la conexión: OPEN');
    
    // Enviar mensaje de prueba simple
    setTimeout(() => {
        console.log('📤 Enviando mensaje de prueba...');
        try {
            const testMessage = JSON.stringify({
                content: "Mensaje de prueba desde Node.js",
                type: "TEXT",
                challengeId: 123
            });
            
            const stompFrame = `SEND\ndestination:/app/chat.challenge\ncontent-type:application/json\n\n${testMessage}`;
            console.log('� Enviando frame STOMP:', stompFrame.substring(0, 100) + '...');
            ws.send(stompFrame);
        } catch (error) {
            console.error('❌ Error enviando mensaje:', error.message);
        }
    }, 2000);
    
    // Cerrar después de 8 segundos
    setTimeout(() => {
        console.log('👋 Cerrando conexión...');
        ws.close();
    }, 8000);
});

ws.on('message', function message(data) {
    console.log('📨 Mensaje recibido:', data.toString());
});

ws.on('error', function error(err) {
    console.error('❌ Error de WebSocket:', err.message);
    console.error('❌ Código de error:', err.code);
});

ws.on('close', function close(code, reason) {
    console.log('🔌 Conexión WebSocket cerrada');
    console.log('📋 Código:', code, 'Razón:', reason.toString());
});
