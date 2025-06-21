package com.bad.batch.service;

import com.bad.batch.dto.request.UserDTO;

import java.util.List;

public interface  UserService {
    List<UserDTO> getAllUsers();
}
