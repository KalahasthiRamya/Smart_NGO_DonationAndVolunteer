package com.smartngo.controller;

import com.smartngo.dto.DonationDto;
import com.smartngo.dto.TaskDto;
import com.smartngo.entity.Campaign;

import com.smartngo.enums.AttendanceStatus;
import com.smartngo.enums.CampaignCategory;
import com.smartngo.enums.CampaignStatus;
import com.smartngo.enums.PaymentMethod;
import com.smartngo.enums.TaskPriority;
import com.smartngo.enums.TaskStatus;
import com.smartngo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

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
    private AttendanceService attendanceService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("recentDonations", donationService.findRecentDonations());
        model.addAttribute("upcomingTasks", taskService.findUpcomingTasks());
        model.addAttribute("activeCampaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "admin/dashboard";
    }

    @GetMapping("/donors")
    public String donorManagement(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("donors", donorService.searchDonors(search));
        model.addAttribute("totalDonors", donorService.countActiveDonors());
        model.addAttribute("totalDonationAmount", donorService.sumTotalDonations());
        model.addAttribute("activeDonorsCount", donorService.countActiveDonors());
        model.addAttribute("searchKeyword", search);
        return "admin/donors";
    }

    @PostMapping("/donors/status")
    public String updateDonorStatus(@RequestParam("id") Long id, @RequestParam("status") String status, RedirectAttributes ra) {
        donorService.updateDonorStatus(id, status);
        ra.addFlashAttribute("successMessage", "Donor status updated successfully.");
        return "redirect:/admin/donors";
    }

    @GetMapping("/volunteers")
    public String volunteerManagement(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("volunteers", volunteerService.searchVolunteers(search));
        model.addAttribute("totalVolunteers", volunteerService.countActiveVolunteers());
        model.addAttribute("activeVolunteers", volunteerService.countActiveVolunteers());
        model.addAttribute("searchKeyword", search);
        return "admin/volunteers";
    }

    @PostMapping("/volunteers/status")
    public String updateVolunteerStatus(@RequestParam("id") Long id,
                                        @RequestParam(value = "skills", required = false) String skills,
                                        @RequestParam("status") String status,
                                        RedirectAttributes ra) {
        volunteerService.updateVolunteerSkillsAndStatus(id, skills, status);
        ra.addFlashAttribute("successMessage", "Volunteer updated successfully.");
        return "redirect:/admin/volunteers";
    }

    @GetMapping("/donations")
    public String donationManagement(Model model) {
        model.addAttribute("donations", donationService.findAllDonations());
        model.addAttribute("donors", donorService.findAllDonors());
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        model.addAttribute("totalAmount", donationService.sumTotalDonations());
        model.addAttribute("totalCount", donationService.countTotalDonations());
        return "admin/donations";
    }

    @PostMapping("/donations/create")
    public String createDonation(@RequestParam("donorId") Long donorId,
                                 @RequestParam("campaignId") Long campaignId,
                                 @RequestParam("amount") BigDecimal amount,
                                 @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                 RedirectAttributes ra) {
        DonationDto dto = DonationDto.builder()
                .donorId(donorId)
                .campaignId(campaignId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .build();
        donationService.createDonation(dto);
        ra.addFlashAttribute("successMessage", "Donation recorded successfully!");
        return "redirect:/admin/donations";
    }

    @GetMapping("/campaigns")
    public String campaignManagement(Model model) {
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        model.addAttribute("categories", CampaignCategory.values());
        model.addAttribute("statuses", CampaignStatus.values());
        return "admin/campaigns";
    }

    @PostMapping("/campaigns/create")
    public String createCampaign(@RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("category") CampaignCategory category,
                                 @RequestParam("targetAmount") BigDecimal targetAmount,
                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                 RedirectAttributes ra) {
        Campaign campaign = Campaign.builder()
                .name(name)
                .description(description)
                .category(category)
                .targetAmount(targetAmount)
                .collectedAmount(BigDecimal.ZERO)
                .startDate(startDate)
                .endDate(endDate)
                .status(CampaignStatus.ACTIVE)
                .build();
        campaignService.saveCampaign(campaign);
        ra.addFlashAttribute("successMessage", "Campaign created successfully!");
        return "redirect:/admin/campaigns";
    }

    @GetMapping("/tasks")
    public String taskManagement(Model model) {
        model.addAttribute("tasks", taskService.findAllTasks());
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        model.addAttribute("volunteers", volunteerService.findAllVolunteers());
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());
        return "admin/tasks";
    }

    @PostMapping("/tasks/create")
    public String createTask(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam(value = "campaignId", required = false) Long campaignId,
                             @RequestParam(value = "volunteerId", required = false) Long volunteerId,
                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam("dueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                             @RequestParam("priority") TaskPriority priority,
                             RedirectAttributes ra) {
        TaskDto dto = TaskDto.builder()
                .title(title)
                .description(description)
                .campaignId(campaignId)
                .volunteerId(volunteerId)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(priority)
                .status(TaskStatus.ASSIGNED)
                .build();
        taskService.createTask(dto);
        ra.addFlashAttribute("successMessage", "Task created and assigned successfully!");
        return "redirect:/admin/tasks";
    }

    @PostMapping("/tasks/status")
    public String updateTaskStatus(@RequestParam("id") Long id, @RequestParam("status") TaskStatus status, RedirectAttributes ra) {
        taskService.updateTaskStatus(id, status);
        ra.addFlashAttribute("successMessage", "Task status updated.");
        return "redirect:/admin/tasks";
    }

    @GetMapping("/attendance")
    public String attendanceManagement(Model model) {
        model.addAttribute("attendanceList", attendanceService.findAllAttendance());
        model.addAttribute("volunteers", volunteerService.findAllVolunteers());
        model.addAttribute("tasks", taskService.findAllTasks());
        return "admin/attendance";
    }

    @PostMapping("/attendance/record")
    public String recordAttendance(@RequestParam("volunteerId") Long volunteerId,
                                   @RequestParam(value = "taskId", required = false) Long taskId,
                                   @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam("status") AttendanceStatus status,
                                   RedirectAttributes ra) {
        attendanceService.recordAttendance(volunteerId, taskId, date, status);
        ra.addFlashAttribute("successMessage", "Attendance recorded successfully!");
        return "redirect:/admin/attendance";
    }

    @GetMapping("/reports")
    public String reportsPage(Model model) {
        model.addAttribute("donationReport", reportService.generateDonationReport());
        model.addAttribute("volunteerReport", reportService.generateVolunteerReport());
        model.addAttribute("impactReport", reportService.generateImpactReport());
        return "admin/reports";
    }

    @GetMapping("/notifications")
    public String notificationsPage(Model model) {
        return "admin/notifications";
    }

    @GetMapping("/settings")
    public String settingsPage(Model model) {
        return "admin/settings";
    }
}
