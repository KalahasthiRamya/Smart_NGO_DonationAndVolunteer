package com.smartngo.service;

import com.smartngo.entity.Donor;
import com.smartngo.entity.User;
import com.smartngo.repository.DonorRepository;
import com.smartngo.service.impl.DonorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonorServiceTest {

    @Mock
    private DonorRepository donorRepository;

    @InjectMocks
    private DonorServiceImpl donorService;

    private Donor testDonor;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).name("Rahul Patil").email("rahul@gmail.com").build();
        testDonor = Donor.builder().id(1L).user(user).totalDonations(new BigDecimal("15000.00")).status("ACTIVE").build();
    }

    @Test
    @DisplayName("Should return all active donors")
    void shouldReturnAllDonors() {
        when(donorRepository.findAll()).thenReturn(Arrays.asList(testDonor));

        List<Donor> result = donorService.findAllDonors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Rahul Patil", result.get(0).getUser().getName());
        verify(donorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should calculate total donation sum")
    void shouldCalculateTotalDonations() {
        when(donorRepository.sumTotalDonationsOfActiveDonors()).thenReturn(new BigDecimal("15000.00"));

        BigDecimal total = donorService.sumTotalDonations();

        assertEquals(new BigDecimal("15000.00"), total);
        verify(donorRepository, times(1)).sumTotalDonationsOfActiveDonors();
    }
}
