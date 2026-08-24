package com.smartngo.service;

import com.smartngo.dto.DashboardStatsDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {
    DashboardStatsDto getDashboardStats();
    Map<String, Object> getDonationsOverTimeChartData();
    Map<String, BigDecimal> getDonationsByCategoryChartData();
}
