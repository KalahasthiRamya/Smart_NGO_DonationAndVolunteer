package com.smartngo.service;

import com.smartngo.entity.User;
import com.smartngo.entity.Volunteer;
import com.smartngo.repository.VolunteerRepository;
import com.smartngo.service.impl.VolunteerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerServiceImpl volunteerService;

    private Volunteer volunteer;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).name("Sneha Desai").email("sneha@gmail.com").build();
        volunteer = Volunteer.builder().id(1L).user(user).skills("Teaching").status("ACTIVE").joinedDate(LocalDate.now()).build();
    }

    @Test
    @DisplayName("Should update volunteer skills and status")
    void shouldUpdateVolunteerSkills() {
        when(volunteerRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        Volunteer updated = volunteerService.updateVolunteerSkillsAndStatus(1L, "Logistics & Teaching", "ACTIVE");

        assertNotNull(updated);
        assertEquals("Logistics & Teaching", updated.getSkills());
        verify(volunteerRepository, times(1)).save(volunteer);
    }
}
