package com.smartngo.service;

import com.smartngo.dto.DonationDto;
import com.smartngo.entity.Donation;
import com.smartngo.entity.Donor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DonationService {
    Donation createDonation(DonationDto dto);
    List<Donation> findAllDonations();
    Optional<Donation> findById(Long id);
    List<Donation> findByDonor(Donor donor);
    BigDecimal sumTotalDonations();
    long countTotalDonations();
    List<Donation> findRecentDonations();
    Map<String, BigDecimal> getDonationsByCategory();
}
