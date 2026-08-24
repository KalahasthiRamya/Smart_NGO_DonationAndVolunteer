package com.smartngo.controller;

import com.smartngo.dto.*;
import com.smartngo.entity.*;
import com.smartngo.enums.TaskStatus;
import com.smartngo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private DonorService donorService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PaymentService paymentService;

    // --- AUTH REST ENDPOINTS ---
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationDto dto) {
        User user = userService.registerUser(dto);
        return ResponseEntity.ok(user);
    }

    // --- DONORS REST ENDPOINTS ---
    @GetMapping("/donors")
    public ResponseEntity<List<Donor>> getAllDonors(@RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(donorService.searchDonors(search));
    }

    @GetMapping("/donors/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable Long id) {
        return donorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/donors/{id}")
    public ResponseEntity<Donor> updateDonorStatus(@PathVariable Long id, @RequestParam("status") String status) {
        return ResponseEntity.ok(donorService.updateDonorStatus(id, status));
    }

    @DeleteMapping("/donors/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.noContent().build();
    }

    // --- DONATIONS REST ENDPOINTS ---
    @GetMapping("/donations")
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationService.findAllDonations());
    }

    @GetMapping("/donations/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return donationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/donations")
    public ResponseEntity<Donation> createDonation(@RequestBody DonationDto dto) {
        return ResponseEntity.ok(donationService.createDonation(dto));
    }

    // --- VOLUNTEERS REST ENDPOINTS ---
    @GetMapping("/volunteers")
    public ResponseEntity<List<Volunteer>> getAllVolunteers(@RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(volunteerService.searchVolunteers(search));
    }

    @PutMapping("/volunteers/{id}")
    public ResponseEntity<Volunteer> updateVolunteer(@PathVariable Long id,
                                                    @RequestParam(value = "skills", required = false) String skills,
                                                    @RequestParam(value = "status", required = false) String status) {
        return ResponseEntity.ok(volunteerService.updateVolunteerSkillsAndStatus(id, skills, status));
    }

    @DeleteMapping("/volunteers/{id}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.noContent().build();
    }

    // --- TASKS REST ENDPOINTS ---
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestParam("status") TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    // --- CAMPAIGNS REST ENDPOINTS ---
    @GetMapping("/campaigns")
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.findAllCampaigns());
    }

    @PostMapping("/campaigns")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.saveCampaign(campaign));
    }

    @PutMapping("/campaigns/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, campaignDetails));
    }

    // --- DASHBOARD REST ENDPOINTS ---
    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardStatsDto> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/dashboard/donations")
    public ResponseEntity<Map<String, Object>> getDonationsOverTime() {
        return ResponseEntity.ok(dashboardService.getDonationsOverTimeChartData());
    }

    @GetMapping("/dashboard/categories")
    public ResponseEntity<Map<String, BigDecimal>> getDonationsByCategory() {
        return ResponseEntity.ok(dashboardService.getDonationsByCategoryChartData());
    }

    @GetMapping("/dashboard/volunteer-activity")
    public ResponseEntity<Map<String, Object>> getVolunteerActivityStats() {
        Map<String, Object> activity = new HashMap<>();
        activity.put("totalVolunteers", volunteerService.countActiveVolunteers());
        activity.put("completedTasks", taskService.countCompletedTasks());
        return ResponseEntity.ok(activity);
    }

    // --- REPORTS REST ENDPOINTS ---
    @GetMapping("/reports/donations")
    public ResponseEntity<Map<String, Object>> getDonationReport() {
        return ResponseEntity.ok(reportService.generateDonationReport());
    }

    @GetMapping("/reports/volunteers")
    public ResponseEntity<Map<String, Object>> getVolunteerReport() {
        return ResponseEntity.ok(reportService.generateVolunteerReport());
    }

    @GetMapping("/reports/impact")
    public ResponseEntity<Map<String, Object>> getImpactReport() {
        return ResponseEntity.ok(reportService.generateImpactReport());
    }

    // --- PAYMENTS REST ENDPOINTS ---
    @PostMapping("/payments/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @PostMapping("/payments/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestParam("transactionId") String transactionId) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "VERIFIED");
        result.put("transactionId", transactionId);
        return ResponseEntity.ok(result);
    }
}
