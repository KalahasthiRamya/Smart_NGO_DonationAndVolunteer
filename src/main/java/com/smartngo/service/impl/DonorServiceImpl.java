package com.smartngo.service.impl;

import com.smartngo.entity.Donor;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.DonorRepository;
import com.smartngo.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Override
    public List<Donor> findAllDonors() {
        return donorRepository.findAll();
    }

    @Override
    public Optional<Donor> findById(Long id) {
        return donorRepository.findById(id);
    }

    @Override
    public Optional<Donor> findByUserId(Long userId) {
        return donorRepository.findByUserId(userId);
    }

    @Override
    public Donor saveDonor(Donor donor) {
        return donorRepository.save(donor);
    }

    @Override
    public Donor updateDonorStatus(Long id, String status) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        donor.setStatus(status);
        if (donor.getUser() != null) {
            donor.getUser().setStatus(status);
        }
        return donorRepository.save(donor);
    }

    @Override
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }

    @Override
    public List<Donor> searchDonors(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return donorRepository.findAll();
        }
        return donorRepository.searchDonors(keyword.trim());
    }

    @Override
    public long countActiveDonors() {
        return donorRepository.countByStatus("ACTIVE");
    }

    @Override
    public BigDecimal sumTotalDonations() {
        BigDecimal sum = donorRepository.sumTotalDonationsOfActiveDonors();
        return sum != null ? sum : BigDecimal.ZERO;
    }
}
