package com.bad.batch.Service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bad.batch.DTO.GetUserDTO;
import com.bad.batch.Model.User;
import com.bad.batch.Repository.UserRepository;
import com.bad.batch.Service.GetUserService;

@Service
public class GetUserServiceImpl implements GetUserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<GetUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private GetUserDTO convertToDTO(User user) {
        GetUserDTO dto = new GetUserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
