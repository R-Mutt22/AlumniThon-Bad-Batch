package com.bad.batch.Controller;

import com.bad.batch.Model.Message;
import com.bad.batch.Enum.MessageType;
import com.bad.batch.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> message = messageRepository.findById(id);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        if (message.getType() == null) {
            message.setType(MessageType.DIRECT);
        }
        return messageRepository.save(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody Message details) {
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Message message = optional.get();
        message.setContent(details.getContent());
        message.setType(details.getType());
        message.setRelatedContent(details.getRelatedContent());
        message.setIsRead(details.getIsRead());
        message.setReadAt(details.getReadAt());
        message.setIsEdited(details.getIsEdited());
        message.setIsDeleted(details.getIsDeleted());
        message.setEditedAt(details.getEditedAt());
        return ResponseEntity.ok(messageRepository.save(message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        messageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

