package com.bad.batch.Controller;

import com.bad.batch.Model.ChatRoom;
import com.bad.batch.Repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat-rooms")
public class ChatRoomController {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoom> getChatRoomById(@PathVariable Long id) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(id);
        return chatRoom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatRoom> updateChatRoom(@PathVariable Long id, @RequestBody ChatRoom details) {
        Optional<ChatRoom> optional = chatRoomRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ChatRoom chatRoom = optional.get();
        chatRoom.setContent(details.getContent());
        chatRoom.setRoomId(details.getRoomId());
        chatRoom.setIsActive(details.getIsActive());
        return ResponseEntity.ok(chatRoomRepository.save(chatRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long id) {
        if (!chatRoomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chatRoomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

