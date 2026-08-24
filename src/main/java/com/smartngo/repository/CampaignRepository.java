package com.smartngo.repository;

import com.smartngo.entity.Campaign;
import com.smartngo.enums.CampaignCategory;
import com.smartngo.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(CampaignStatus status);
    List<Campaign> findByCategory(CampaignCategory category);
    long countByStatus(CampaignStatus status);
}
