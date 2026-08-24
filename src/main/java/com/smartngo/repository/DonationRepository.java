package com.smartngo.repository;

import com.smartngo.entity.Campaign;
import com.smartngo.entity.Donation;
import com.smartngo.entity.Donor;
import com.smartngo.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonor(Donor donor);
    List<Donation> findByCampaign(Campaign campaign);
    List<Donation> findByStatus(PaymentStatus status);
    Optional<Donation> findByTransactionId(String transactionId);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.status = 'SUCCESS'")
    BigDecimal sumTotalSuccessfulDonations();

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.status = 'SUCCESS'")
    long countSuccessfulDonations();

    @Query("SELECT d.campaign.category, SUM(d.amount) FROM Donation d WHERE d.status = 'SUCCESS' GROUP BY d.campaign.category")
    List<Object[]> sumDonationsByCategory();

    List<Donation> findTop5ByStatusOrderByDonationDateDesc(PaymentStatus status);
}
