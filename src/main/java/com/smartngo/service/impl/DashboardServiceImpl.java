package com.smartngo.service.impl;

import com.smartngo.dto.DashboardStatsDto;
import com.smartngo.entity.Donation;
import com.smartngo.enums.PaymentStatus;
import com.smartngo.repository.*;
import com.smartngo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public DashboardStatsDto getDashboardStats() {
        BigDecimal totalDonations = donationRepository.sumTotalSuccessfulDonations();
        if (totalDonations == null) {
            totalDonations = new BigDecimal("125000.00"); // Fallback initial demo baseline
        }

        long totalDonors = donorRepository.countByStatus("ACTIVE");
        if (totalDonors == 0) totalDonors = 245;

        long totalVolunteers = volunteerRepository.countByStatus("ACTIVE");
        if (totalVolunteers == 0) totalVolunteers = 132;

        long totalActivities = taskRepository.count();
        if (totalActivities == 0) totalActivities = 89;

        long activeCampaigns = campaignRepository.count();
        if (activeCampaigns == 0) activeCampaigns = 8;

        return DashboardStatsDto.builder()
                .totalDonations(totalDonations)
                .totalDonors(totalDonors)
                .totalVolunteers(totalVolunteers)
                .totalActivities(totalActivities)
                .activeCampaigns(activeCampaigns)
                .build();
    }

    @Override
    public Map<String, Object> getDonationsOverTimeChartData() {
        List<Donation> donations = donationRepository.findByStatus(PaymentStatus.SUCCESS);
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        if (donations.isEmpty()) {
            labels = Arrays.asList("1 May", "8 May", "15 May", "22 May", "29 May");
            data = Arrays.asList(new BigDecimal("15000"), new BigDecimal("35000"), new BigDecimal("25000"), new BigDecimal("45000"), new BigDecimal("60000"));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
            for (Donation d : donations) {
                labels.add(d.getDonationDate().format(formatter));
                data.add(d.getAmount());
            }
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("data", data);
        return chartData;
    }

    @Override
    public Map<String, BigDecimal> getDonationsByCategoryChartData() {
        List<Object[]> results = donationRepository.sumDonationsByCategory();
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        if (results.isEmpty()) {
            map.put("Education", new BigDecimal("50000"));
            map.put("Health", new BigDecimal("37500"));
            map.put("Environment", new BigDecimal("25000"));
            map.put("Others", new BigDecimal("12500"));
        } else {
            for (Object[] row : results) {
                String cat = row[0] != null ? row[0].toString() : "OTHER";
                BigDecimal amt = row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO;
                map.put(cat, amt);
            }
        }
        return map;
    }
}
