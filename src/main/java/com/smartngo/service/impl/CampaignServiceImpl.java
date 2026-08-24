package com.smartngo.service.impl;

import com.smartngo.entity.Campaign;
import com.smartngo.enums.CampaignCategory;
import com.smartngo.enums.CampaignStatus;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.CampaignRepository;
import com.smartngo.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public List<Campaign> findAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        return campaignRepository.findById(id);
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public Campaign updateCampaign(Long id, Campaign campaignDetails) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + id));
        campaign.setName(campaignDetails.getName());
        campaign.setDescription(campaignDetails.getDescription());
        campaign.setCategory(campaignDetails.getCategory());
        campaign.setTargetAmount(campaignDetails.getTargetAmount());
        campaign.setStartDate(campaignDetails.getStartDate());
        campaign.setEndDate(campaignDetails.getEndDate());
        campaign.setStatus(campaignDetails.getStatus());
        return campaignRepository.save(campaign);
    }

    @Override
    public void deleteCampaign(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campaign not found with id: " + id);
        }
        campaignRepository.deleteById(id);
    }

    @Override
    public List<Campaign> findByStatus(CampaignStatus status) {
        return campaignRepository.findByStatus(status);
    }

    @Override
    public List<Campaign> findByCategory(CampaignCategory category) {
        return campaignRepository.findByCategory(category);
    }

    @Override
    public long countActiveCampaigns() {
        return campaignRepository.countByStatus(CampaignStatus.ACTIVE);
    }
}
