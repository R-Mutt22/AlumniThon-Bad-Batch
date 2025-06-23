package com.bad.batch.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) throws Exception {
        // Simulate processing delay
        Thread.sleep(1000);
        return new Message("Hello," + HtmlUtils.htmlEscape(message.getName()) + "!"); // Echo the received message
    }
    
}
