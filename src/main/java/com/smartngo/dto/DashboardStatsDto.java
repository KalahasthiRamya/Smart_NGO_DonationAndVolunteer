package com.smartngo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDto {
    private BigDecimal totalDonations;
    private long totalDonors;
    private long totalVolunteers;
    private long totalActivities;
    private long activeCampaigns;
}
