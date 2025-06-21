package com.bad.batch.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bad.batch.DTO.GetUserDTO;
import com.bad.batch.Service.GetUserService;

@RestController
@RequestMapping("/api")
public class GetUserController {

    private final GetUserService getUserService;

    @Autowired
    public GetUserController(GetUserService getUserService) {
        this.getUserService = getUserService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUserDTO>> getAllUsers() {
        List<GetUserDTO> users = getUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}