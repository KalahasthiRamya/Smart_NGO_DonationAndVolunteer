package com.smartngo.dto;

import com.smartngo.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;
    private Role role;
    private String skills; // For volunteer registration
}
