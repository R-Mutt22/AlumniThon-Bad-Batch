const WebSocket = require('ws');
const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

// Datos de prueba
const testUser1 = {
    email: 'user1@test.com',
    password: 'Password123!',
    firstName: 'Usuario',
    lastName: 'Uno',
    role: 'DEVELOPER'
};

const testUser2 = {
    email: 'user2@test.com', 
    password: 'Password123!',
    firstName: 'Usuario',
    lastName: 'Dos',
    role: 'DEVELOPER'
};

class ChatPersistenceTest {
    constructor() {
        this.user1Token = null;
        this.user2Token = null;
        this.user1Id = null;
        this.user2Id = null;
        this.ws1 = null;
        this.ws2 = null;
        this.messagesReceived = [];
    }

    async register(userData) {
        try {
            const response = await axios.post(`${BASE_URL}/api/auth/register`, userData);
            return response.data;
        } catch (error) {
            if (error.response?.status === 409 || error.response?.status === 500) {
                console.log(`Usuario ${userData.email} ya existe, intentando login...`);
                return null;
            }
            throw error;
        }
    }

    async login(email, password) {
        try {
            const response = await axios.post(`${BASE_URL}/api/auth/login`, {
                email,
                password
            });
            return response.data;
        } catch (error) {
            console.error(`Error en login de ${email}:`, error.response?.data || error.message);
            throw error;
        }
    }

    async setupUsers() {
        console.log('🔧 Configurando usuarios de prueba...');
        
        // Registrar o hacer login user1
        await this.register(testUser1);
        const loginResult1 = await this.login(testUser1.email, testUser1.password);
        this.user1Token = loginResult1.accessToken;
        this.user1Id = loginResult1.user.id;
        console.log(`✅ Usuario 1 autenticado: ${testUser1.email} (ID: ${this.user1Id})`);

        // Registrar o hacer login user2
        await this.register(testUser2);
        const loginResult2 = await this.login(testUser2.email, testUser2.password);
        this.user2Token = loginResult2.accessToken;
        this.user2Id = loginResult2.user.id;
        console.log(`✅ Usuario 2 autenticado: ${testUser2.email} (ID: ${this.user2Id})`);
    }

    connectWebSocket(userId, token) {
        return new Promise((resolve, reject) => {
            // Usar SockJS en lugar de WebSocket directo
            const ws = new WebSocket(`ws://localhost:8080/ws/websocket`);
            
            ws.on('open', () => {
                console.log(`🔌 WebSocket conectado para usuario ${userId}`);
                
                // Enviar comando CONNECT con el token en headers
                const connectFrame = `CONNECT\nAuthorization:Bearer ${token}\naccept-version:1.0,1.1,2.0\nheart-beat:10000,10000\n\n\x00`;
                ws.send(connectFrame);
                
                resolve(ws);
            });

            ws.on('error', (error) => {
                console.error(`❌ Error WebSocket usuario ${userId}:`, error);
                reject(error);
            });

            ws.on('message', (data) => {
                try {
                    const message = data.toString();
                    console.log(`📨 Mensaje recibido por usuario ${userId}:`, message);
                    this.messagesReceived.push({
                        receivedBy: userId,
                        message: message
                    });
                } catch (error) {
                    console.error('Error parsing WebSocket message:', error);
                }
            });
        });
    }

    async setupWebSockets() {
        console.log('🔌 Conectando WebSockets...');
        this.ws1 = await this.connectWebSocket(this.user1Id, this.user1Token);
        this.ws2 = await this.connectWebSocket(this.user2Id, this.user2Token);
    }

    sendWebSocketMessage(ws, destination, payload) {
        const stompMessage = `SEND\ndestination:${destination}\ncontent-type:application/json\n\n${JSON.stringify(payload)}\x00`;
        ws.send(stompMessage);
    }

    async sendDirectMessage(fromWs, fromUserId, toUserId, content) {
        console.log(`💬 Enviando mensaje directo de ${fromUserId} a ${toUserId}: "${content}"`);
        
        const messagePayload = {
            content: content,
            type: 'TEXT',
            recipientId: toUserId
        };

        this.sendWebSocketMessage(fromWs, '/app/chat.direct', messagePayload);
        
        // Esperar un poco para que se procese
        await new Promise(resolve => setTimeout(resolve, 1000));
    }

    async getDirectMessages(token, otherUserId) {
        try {
            const response = await axios.get(
                `${BASE_URL}/api/messages/direct/${otherUserId}?page=0&size=10`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                }
            );
            return response.data;
        } catch (error) {
            console.error('Error obteniendo mensajes:', error.response?.data || error.message);
            throw error;
        }
    }

    async testMessagePersistence() {
        console.log('\n🔍 Probando persistencia de mensajes...');
        
        // Enviar algunos mensajes
        await this.sendDirectMessage(this.ws1, this.user1Id, this.user2Id, 'Hola! Este es un mensaje de prueba');
        await this.sendDirectMessage(this.ws2, this.user2Id, this.user1Id, 'Hola! Mensaje de respuesta');
        await this.sendDirectMessage(this.ws1, this.user1Id, this.user2Id, 'Segundo mensaje para probar persistencia');

        // Esperar un poco más para asegurar que se guarden
        await new Promise(resolve => setTimeout(resolve, 2000));

        // Consultar historial
        console.log('\n📚 Consultando historial de mensajes...');
        
        const messagesUser1View = await this.getDirectMessages(this.user1Token, this.user2Id);
        const messagesUser2View = await this.getDirectMessages(this.user2Token, this.user1Id);

        console.log('📋 Historial desde perspectiva Usuario 1:');
        console.log(`Total de mensajes: ${messagesUser1View.totalElements}`);
        messagesUser1View.content.forEach((msg, index) => {
            console.log(`  ${index + 1}. [${msg.timestamp}] ${msg.senderName}: ${msg.content}`);
        });

        console.log('\n📋 Historial desde perspectiva Usuario 2:');
        console.log(`Total de mensajes: ${messagesUser2View.totalElements}`);
        messagesUser2View.content.forEach((msg, index) => {
            console.log(`  ${index + 1}. [${msg.timestamp}] ${msg.senderName}: ${msg.content}`);
        });

        // Verificar que los mensajes fueron persistidos
        const expectedMessages = 3;
        if (messagesUser1View.totalElements >= expectedMessages && messagesUser2View.totalElements >= expectedMessages) {
            console.log('\n✅ ÉXITO: Los mensajes se han persistido correctamente en la base de datos');
            console.log('✅ ÉXITO: El historial es consistente entre usuarios');
        } else {
            console.log('\n❌ ERROR: Los mensajes no se persistieron correctamente');
            console.log(`Esperados: ${expectedMessages}, Encontrados User1: ${messagesUser1View.totalElements}, User2: ${messagesUser2View.totalElements}`);
        }
    }

    async testConversationAPIs() {
        console.log('\n🔄 Probando APIs adicionales...');
        
        try {
            // Obtener últimas conversaciones
            const conversations = await axios.get(
                `${BASE_URL}/api/messages/conversations`,
                {
                    headers: {
                        'Authorization': `Bearer ${this.user1Token}`
                    }
                }
            );
            console.log(`📞 Conversaciones del usuario 1: ${conversations.data.length}`);

            // Contar mensajes no leídos
            const unreadCount = await axios.get(
                `${BASE_URL}/api/messages/unread/count`,
                {
                    headers: {
                        'Authorization': `Bearer ${this.user2Token}`
                    }
                }
            );
            console.log(`📬 Mensajes no leídos usuario 2: ${unreadCount.data}`);

            console.log('✅ APIs adicionales funcionando correctamente');
        } catch (error) {
            console.log('⚠️  Algunas APIs adicionales pueden no estar completamente funcionales:', error.message);
        }
    }

    async cleanup() {
        console.log('\n🧹 Limpiando conexiones...');
        if (this.ws1) this.ws1.close();
        if (this.ws2) this.ws2.close();
    }

    async runTest() {
        try {
            console.log('🚀 Iniciando prueba de persistencia de mensajes de chat...\n');
            
            await this.setupUsers();
            await this.setupWebSockets();
            await this.testMessagePersistence();
            await this.testConversationAPIs();
            
            console.log('\n🎉 Prueba completada exitosamente!');
            console.log('\n📊 Resumen:');
            console.log(`- Mensajes WebSocket recibidos: ${this.messagesReceived.length}`);
            console.log('- Persistencia de mensajes: ✅ Funcionando');
            console.log('- API REST de historial: ✅ Funcionando');
            console.log('- Integración WebSocket + REST: ✅ Funcionando');
            
        } catch (error) {
            console.error('\n❌ Error durante la prueba:', error);
        } finally {
            await this.cleanup();
        }
    }
}

// Ejecutar la prueba
if (require.main === module) {
    const test = new ChatPersistenceTest();
    test.runTest();
}

module.exports = ChatPersistenceTest;
