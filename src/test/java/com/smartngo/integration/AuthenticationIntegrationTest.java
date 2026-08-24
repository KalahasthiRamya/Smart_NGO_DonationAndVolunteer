package com.smartngo.integration;

import com.smartngo.dto.UserRegistrationDto;
import com.smartngo.entity.User;
import com.smartngo.enums.Role;
import com.smartngo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthenticationIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Integration Test: Should register new donor user and create profile in database")
    void shouldRegisterUserSuccessfully() {
        UserRegistrationDto dto = UserRegistrationDto.builder()
                .name("Integration Donor")
                .email("int_donor@smartngo.com")
                .password("password123")
                .confirmPassword("password123")
                .role(Role.DONOR)
                .build();

        User registered = userService.registerUser(dto);

        assertNotNull(registered.getId());
        assertEquals("int_donor@smartngo.com", registered.getEmail());

        Optional<User> found = userService.findByEmail("int_donor@smartngo.com");
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("Integration Test: Should reject duplicate email registration")
    void shouldRejectDuplicateEmail() {
        UserRegistrationDto dto1 = UserRegistrationDto.builder()
                .name("Test User")
                .email("dup@smartngo.com")
                .password("pass123")
                .confirmPassword("pass123")
                .role(Role.DONOR)
                .build();

        userService.registerUser(dto1);

        UserRegistrationDto dto2 = UserRegistrationDto.builder()
                .name("Test User 2")
                .email("dup@smartngo.com")
                .password("pass123")
                .confirmPassword("pass123")
                .role(Role.DONOR)
                .build();

        assertThrows(RuntimeException.class, () -> userService.registerUser(dto2));
    }
}
