package com.smartngo.service.impl;

import com.smartngo.dto.DonationDto;
import com.smartngo.entity.Campaign;
import com.smartngo.entity.Donation;
import com.smartngo.entity.Donor;
import com.smartngo.enums.NotificationType;
import com.smartngo.enums.PaymentStatus;
import com.smartngo.exception.BadRequestException;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.CampaignRepository;
import com.smartngo.repository.DonationRepository;
import com.smartngo.repository.DonorRepository;
import com.smartngo.service.DonationService;
import com.smartngo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Donation createDonation(DonationDto dto) {
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Donation amount must be greater than zero.");
        }

        Donor donor = donorRepository.findById(dto.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + dto.getDonorId()));

        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + dto.getCampaignId()));

        String txnId = "TXN_" + System.currentTimeMillis();

        Donation donation = Donation.builder()
                .donor(donor)
                .campaign(campaign)
                .amount(dto.getAmount())
                .donationDate(LocalDateTime.now())
                .paymentMethod(dto.getPaymentMethod())
                .transactionId(txnId)
                .status(PaymentStatus.SUCCESS)
                .build();

        Donation savedDonation = donationRepository.save(donation);

        // Update Campaign collected amount
        BigDecimal currentCampaignCollected = campaign.getCollectedAmount() != null ? campaign.getCollectedAmount() : BigDecimal.ZERO;
        campaign.setCollectedAmount(currentCampaignCollected.add(dto.getAmount()));
        campaignRepository.save(campaign);

        // Update Donor total donations
        BigDecimal currentDonorTotal = donor.getTotalDonations() != null ? donor.getTotalDonations() : BigDecimal.ZERO;
        donor.setTotalDonations(currentDonorTotal.add(dto.getAmount()));
        donorRepository.save(donor);

        // Send notification to donor
        if (donor.getUser() != null) {
            notificationService.sendNotification(
                    donor.getUser(),
                    NotificationType.DONATION,
                    "Thank you for your generous donation of ₹" + dto.getAmount() + " to '" + campaign.getName() + "'."
            );
        }

        return savedDonation;
    }

    @Override
    public List<Donation> findAllDonations() {
        return donationRepository.findAll();
    }

    @Override
    public Optional<Donation> findById(Long id) {
        return donationRepository.findById(id);
    }

    @Override
    public List<Donation> findByDonor(Donor donor) {
        return donationRepository.findByDonor(donor);
    }

    @Override
    public BigDecimal sumTotalDonations() {
        BigDecimal sum = donationRepository.sumTotalSuccessfulDonations();
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public long countTotalDonations() {
        return donationRepository.countSuccessfulDonations();
    }

    @Override
    public List<Donation> findRecentDonations() {
        return donationRepository.findTop5ByStatusOrderByDonationDateDesc(PaymentStatus.SUCCESS);
    }

    @Override
    public Map<String, BigDecimal> getDonationsByCategory() {
        List<Object[]> results = donationRepository.sumDonationsByCategory();
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String category = row[0] != null ? row[0].toString() : "OTHER";
            BigDecimal total = row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO;
            map.put(category, total);
        }
        return map;
    }
}
