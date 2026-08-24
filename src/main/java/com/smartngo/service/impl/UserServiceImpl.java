package com.smartngo.service.impl;

import com.smartngo.dto.UserRegistrationDto;
import com.smartngo.entity.Donor;
import com.smartngo.entity.User;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.Role;
import com.smartngo.exception.BadRequestException;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.DonorRepository;
import com.smartngo.repository.UserRepository;
import com.smartngo.repository.VolunteerRepository;
import com.smartngo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email address is required.");
        }
        if (userRepository.existsByEmail(dto.getEmail().trim())) {
            throw new BadRequestException("An account with this email already exists.");
        }
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match.");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail().trim())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .role(dto.getRole() != null ? dto.getRole() : Role.DONOR)
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.DONOR) {
            Donor donor = Donor.builder()
                    .user(savedUser)
                    .totalDonations(BigDecimal.ZERO)
                    .status("ACTIVE")
                    .build();
            donorRepository.save(donor);
        } else if (savedUser.getRole() == Role.VOLUNTEER) {
            Volunteer volunteer = Volunteer.builder()
                    .user(savedUser)
                    .skills(dto.getSkills() != null ? dto.getSkills() : "General Assistance")
                    .status("ACTIVE")
                    .joinedDate(LocalDate.now())
                    .build();
            volunteerRepository.save(volunteer);
        }

        return savedUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existing.setName(userDetails.getName());
        existing.setPhone(userDetails.getPhone());
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
