package com.smartngo.service;

import com.smartngo.dto.DonationDto;
import com.smartngo.entity.Campaign;
import com.smartngo.entity.Donation;
import com.smartngo.entity.Donor;
import com.smartngo.entity.User;
import com.smartngo.enums.PaymentMethod;
import com.smartngo.enums.PaymentStatus;
import com.smartngo.repository.CampaignRepository;
import com.smartngo.repository.DonationRepository;
import com.smartngo.repository.DonorRepository;
import com.smartngo.service.impl.DonationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private DonorRepository donorRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DonationServiceImpl donationService;

    private Donor donor;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).name("Rahul Patil").email("rahul@gmail.com").build();
        donor = Donor.builder().id(1L).user(user).totalDonations(BigDecimal.ZERO).status("ACTIVE").build();
        campaign = Campaign.builder().id(1L).name("Education for All").collectedAmount(BigDecimal.ZERO).targetAmount(new BigDecimal("60000")).build();
    }

    @Test
    @DisplayName("Should create donation and update campaign target amount")
    void shouldCreateDonation() {
        DonationDto dto = DonationDto.builder()
                .donorId(1L)
                .campaignId(1L)
                .amount(new BigDecimal("5000.00"))
                .paymentMethod(PaymentMethod.UPI)
                .build();

        Donation savedDonation = Donation.builder()
                .id(1L)
                .donor(donor)
                .campaign(campaign)
                .amount(new BigDecimal("5000.00"))
                .transactionId("TXN_12345")
                .status(PaymentStatus.SUCCESS)
                .build();

        when(donorRepository.findById(1L)).thenReturn(Optional.of(donor));
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(donationRepository.save(any(Donation.class))).thenReturn(savedDonation);

        Donation result = donationService.createDonation(dto);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(new BigDecimal("5000.00"), result.getAmount());
        verify(donationRepository, times(1)).save(any(Donation.class));
    }
}
