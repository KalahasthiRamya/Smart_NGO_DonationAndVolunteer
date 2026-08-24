package com.smartngo.repository;

import com.smartngo.entity.Donor;
import com.smartngo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findByUser(User user);
    Optional<Donor> findByUserId(Long userId);
    List<Donor> findByStatus(String status);
    long countByStatus(String status);

    @Query("SELECT SUM(d.totalDonations) FROM Donor d WHERE d.status = 'ACTIVE'")
    BigDecimal sumTotalDonationsOfActiveDonors();

    @Query("SELECT d FROM Donor d WHERE LOWER(d.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Donor> searchDonors(@Param("keyword") String keyword);
}
