package com.smartngo.service;

import com.smartngo.entity.Campaign;
import com.smartngo.enums.CampaignCategory;
import com.smartngo.enums.CampaignStatus;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    List<Campaign> findAllCampaigns();
    Optional<Campaign> findById(Long id);
    Campaign saveCampaign(Campaign campaign);
    Campaign updateCampaign(Long id, Campaign campaignDetails);
    void deleteCampaign(Long id);
    List<Campaign> findByStatus(CampaignStatus status);
    List<Campaign> findByCategory(CampaignCategory category);
    long countActiveCampaigns();
}
