package com.smartngo.service;

import com.smartngo.entity.Donor;
import com.smartngo.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DonorService {
    List<Donor> findAllDonors();
    Optional<Donor> findById(Long id);
    Optional<Donor> findByUserId(Long userId);
    Donor saveDonor(Donor donor);
    Donor createDonor(User user);
    Donor updateDonorStatus(Long id, String status);
    void deleteDonor(Long id);
    List<Donor> searchDonors(String keyword);
    long countActiveDonors();
    BigDecimal sumTotalDonations();
}
