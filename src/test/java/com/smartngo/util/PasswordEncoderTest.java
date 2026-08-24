package com.smartngo.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void printBCryptHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("BCrypt admin123: " + encoder.encode("admin123"));
        System.out.println("BCrypt donor123: " + encoder.encode("donor123"));
        System.out.println("BCrypt volunteer123: " + encoder.encode("volunteer123"));
    }
}
