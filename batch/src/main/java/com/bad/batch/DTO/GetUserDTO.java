package com.bad.batch.DTO;

import java.time.LocalDateTime;

import com.bad.batch.Enum.UserRole;

import lombok.Data;


@Data
public class GetUserDTO {
    
    private String email;    
    private String firstName;    
    private String lastName;    
    private UserRole role; 
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
