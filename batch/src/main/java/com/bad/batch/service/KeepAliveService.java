package com.bad.batch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class KeepAliveService {

    private static final Logger logger = LoggerFactory.getLogger(KeepAliveService.class);
    
    private final RestTemplate restTemplate;
    private final AtomicLong pingCount = new AtomicLong(0);
    private volatile LocalDateTime lastPingTime = LocalDateTime.now();
    private volatile String lastPingStatus = "Iniciado";
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    public KeepAliveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Ejecuta un ping automático cada 5 minutos para mantener activo el servidor
     */
    @Scheduled(fixedRate = 300000) // 5 minutos = 300,000 milisegundos
    public void keepServerAlive() {
        try {
            logger.info("Ejecutando keep-alive ping automático #{}", pingCount.incrementAndGet());
            
            // Realiza una llamada HTTP al propio endpoint de ping
            String pingUrl = baseUrl + "/api/keep-alive/ping";
            String response = restTemplate.getForObject(pingUrl, String.class);
            
            lastPingTime = LocalDateTime.now();
            lastPingStatus = "Exitoso";
            
            logger.info("Keep-alive ping exitoso: {}", response);
            
        } catch (Exception e) {
            lastPingStatus = "Error: " + e.getMessage();
            logger.error("Error en keep-alive ping: {}", e.getMessage(), e);
        }
    }

    /**
     * Obtiene el estado básico del servidor
     */
    public String getServerStatus() {
        return String.format("Servidor activo - %s - Ping #%d", 
                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                           pingCount.get());
    }

    /**
     * Obtiene información detallada sobre el estado del servidor
     */
    public Map<String, Object> getDetailedStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        status.put("status", "ACTIVE");
        status.put("totalPings", pingCount.get());
        status.put("lastPingTime", lastPingTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        status.put("lastPingStatus", lastPingStatus);
        status.put("uptime", "Servidor en funcionamiento");
        status.put("baseUrl", baseUrl);
        status.put("keepAliveInterval", "5 minutos");
        
        return status;
    }

    /**
     * Reinicia las estadísticas del keep-alive
     */
    public void resetStats() {
        pingCount.set(0);
        lastPingTime = LocalDateTime.now();
        lastPingStatus = "Reiniciado";
        logger.info("Estadísticas de keep-alive reiniciadas");
    }
}
