package com.smartngo.service;

import com.smartngo.dto.UserRegistrationDto;
import com.smartngo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDto dto);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAllUsers();
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
}
