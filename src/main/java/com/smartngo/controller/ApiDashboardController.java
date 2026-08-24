package com.smartngo.controller;

import com.smartngo.dto.DashboardStatsDto;
import com.smartngo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class ApiDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/donations-chart")
    public ResponseEntity<Map<String, Object>> getDonationsOverTimeChart() {
        return ResponseEntity.ok(dashboardService.getDonationsOverTimeChartData());
    }

    @GetMapping("/categories-chart")
    public ResponseEntity<Map<String, BigDecimal>> getDonationsByCategoryChart() {
        return ResponseEntity.ok(dashboardService.getDonationsByCategoryChartData());
    }
}
