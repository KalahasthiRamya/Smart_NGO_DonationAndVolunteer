package com.smartngo.config;

import com.smartngo.entity.*;
import com.smartngo.enums.*;
import com.smartngo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${ADMIN_EMAIL:admin@smartngo.com}")
    private String envAdminEmail;

    @Value("${ADMIN_PASSWORD:admin123}")
    private String envAdminPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Running DataInitializer for active profile: {}", activeProfile);

        // 1. Always Ensure Administrator Account Exists with Encoded Password
        User admin = userRepository.findByEmail(envAdminEmail).orElse(null);
        if (admin == null) {
            admin = User.builder()
                    .name("System Admin")
                    .email(envAdminEmail)
                    .password(passwordEncoder.encode(envAdminPassword))
                    .phone("9876543210")
                    .role(Role.ADMIN)
                    .status("ACTIVE")
                    .build();
            userRepository.save(admin);
            log.info("Created production administrator account: {}", envAdminEmail);
        } else {
            admin.setPassword(passwordEncoder.encode(envAdminPassword));
            userRepository.save(admin);
            log.info("Synced credentials for administrator account: {}", envAdminEmail);
        }

        // In production profile, stop after bootstrapping admin account to keep production DB clean
        if ("prod".equalsIgnoreCase(activeProfile)) {
            log.info("Production profile active. Skipping demo seed data population.");
            return;
        }

        // 2. Dev / Test Demo Seed Data
        User donorUser = userRepository.findByEmail("rahul@gmail.com").orElse(null);
        if (donorUser == null) {
            donorUser = User.builder()
                    .name("Rahul Patil")
                    .email("rahul@gmail.com")
                    .password(passwordEncoder.encode("donor123"))
                    .phone("9876543210")
                    .role(Role.DONOR)
                    .status("ACTIVE")
                    .build();
            userRepository.save(donorUser);
            donorRepository.save(Donor.builder().user(donorUser).totalDonations(new BigDecimal("15000.00")).status("ACTIVE").build());
        } else {
            donorUser.setPassword(passwordEncoder.encode("donor123"));
            userRepository.save(donorUser);
        }

        User volUser = userRepository.findByEmail("sneha@gmail.com").orElse(null);
        if (volUser == null) {
            volUser = User.builder()
                    .name("Sneha Desai")
                    .email("sneha@gmail.com")
                    .password(passwordEncoder.encode("volunteer123"))
                    .phone("9876501234")
                    .role(Role.VOLUNTEER)
                    .status("ACTIVE")
                    .build();
            userRepository.save(volUser);
            volunteerRepository.save(Volunteer.builder().user(volUser).skills("Teaching").status("ACTIVE").joinedDate(LocalDate.of(2024, 1, 10)).build());
        } else {
            volUser.setPassword(passwordEncoder.encode("volunteer123"));
            userRepository.save(volUser);
        }

        if (campaignRepository.count() == 0) {
            Campaign c1 = campaignRepository.save(Campaign.builder().name("Education for All").description("Provide free school supplies and scholarships").category(CampaignCategory.EDUCATION).targetAmount(new BigDecimal("60000.00")).collectedAmount(new BigDecimal("40000.00")).startDate(LocalDate.of(2024, 1, 1)).endDate(LocalDate.of(2024, 12, 31)).status(CampaignStatus.ACTIVE).build());
            campaignRepository.save(Campaign.builder().name("Health & Wellness Drive").description("Free health checkup camps in rural areas").category(CampaignCategory.HEALTH).targetAmount(new BigDecimal("50000.00")).collectedAmount(new BigDecimal("30000.00")).startDate(LocalDate.of(2024, 2, 1)).endDate(LocalDate.of(2024, 11, 30)).status(CampaignStatus.ACTIVE).build());
            campaignRepository.save(Campaign.builder().name("Green Earth Initiative").description("Tree plantation and awareness programs").category(CampaignCategory.ENVIRONMENT).targetAmount(new BigDecimal("40000.00")).collectedAmount(new BigDecimal("20000.00")).startDate(LocalDate.of(2024, 3, 1)).endDate(LocalDate.of(2024, 10, 31)).status(CampaignStatus.ACTIVE).build());
        }

        log.info("DataInitializer completed successfully.");
    }
}
