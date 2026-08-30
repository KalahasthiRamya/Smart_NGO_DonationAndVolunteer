package com.smartngo.controller;

import com.smartngo.dto.DonationDto;
import com.smartngo.dto.TaskDto;
import com.smartngo.dto.UserRegistrationDto;
import com.smartngo.entity.Campaign;
import com.smartngo.entity.Donor;
import com.smartngo.entity.User;
import com.smartngo.entity.Volunteer;

import com.smartngo.enums.AttendanceStatus;
import com.smartngo.enums.CampaignCategory;
import com.smartngo.enums.CampaignStatus;
import com.smartngo.enums.NotificationType;
import com.smartngo.enums.PaymentMethod;
import com.smartngo.enums.Role;
import com.smartngo.enums.TaskPriority;
import com.smartngo.enums.TaskStatus;
import com.smartngo.exception.BadRequestException;
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
    private AttendanceService attendanceService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("recentDonations", donationService.findRecentDonations());
        model.addAttribute("upcomingTasks", taskService.findUpcomingTasks());
        model.addAttribute("activeCampaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "admin/dashboard";
    }

    @GetMapping("/impact")
    public String impactDashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("recentDonations", donationService.findRecentDonations());
        model.addAttribute("upcomingTasks", taskService.findUpcomingTasks());
        model.addAttribute("activeCampaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "admin/impact";
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

    @PostMapping("/donors/create")
    public String createDonor(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam(value = "phone", required = false) String phone,
                              RedirectAttributes ra) {
        try {
            UserRegistrationDto dto = UserRegistrationDto.builder()
                    .name(name)
                    .email(email)
                    .phone(phone != null && !phone.trim().isEmpty() ? phone : "9876543210")
                    .password("donor123")
                    .confirmPassword("donor123")
                    .role(Role.DONOR)
                    .build();

            User user = userService.registerUser(dto);
            donorService.createDonor(user);

            notificationService.sendNotification(user, NotificationType.REGISTRATION, "Welcome to Smart NGO! Your donor account is active.");
            notificationService.notifyAllAdmins(NotificationType.SYSTEM, "New donor registered: " + name + " (" + email + ")");

            ra.addFlashAttribute("successMessage", "New donor " + name + " created successfully!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Failed to create donor. Email may already be registered.");
        }
        return "redirect:/admin/donors";
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

    @PostMapping("/volunteers/create")
    public String createVolunteer(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "skills", required = false) String skills,
                                RedirectAttributes ra) {
        try {
            String finalPassword = (password != null && !password.trim().isEmpty()) ? password : "volunteer123";
            String finalSkills = (skills != null && !skills.trim().isEmpty()) ? skills : "General Support";

            UserRegistrationDto dto = UserRegistrationDto.builder()
                    .name(name)
                    .email(email)
                    .phone(phone != null && !phone.trim().isEmpty() ? phone : "9876501234")
                    .password(finalPassword)
                    .confirmPassword(finalPassword)
                    .role(Role.VOLUNTEER)
                    .skills(finalSkills)
                    .build();

            User user = userService.registerUser(dto);
            volunteerService.registerVolunteer(user, finalSkills);

            notificationService.sendNotification(user, NotificationType.REGISTRATION, "Welcome to Smart NGO! Your volunteer profile is active.");
            notificationService.notifyAllAdmins(NotificationType.SYSTEM, "New volunteer registered: " + name + " (" + email + ")");

            ra.addFlashAttribute("successMessage", "Volunteer created successfully.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Failed to create volunteer. Email may already be registered.");
        }
        return "redirect:/admin/volunteers";
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
    public String createDonation(@RequestParam(value = "donorId", required = false) Long donorId,
                                 @RequestParam(value = "newDonorName", required = false) String newDonorName,
                                 @RequestParam(value = "newDonorEmail", required = false) String newDonorEmail,
                                 @RequestParam(value = "newDonorPhone", required = false) String newDonorPhone,
                                 @RequestParam("campaignId") Long campaignId,
                                 @RequestParam("amount") BigDecimal amount,
                                 @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                 RedirectAttributes ra) {
        try {
            Long targetDonorId = donorId;

            if ((targetDonorId == null || targetDonorId == -1L) && newDonorName != null && !newDonorName.trim().isEmpty()) {
                UserRegistrationDto dto = UserRegistrationDto.builder()
                        .name(newDonorName)
                        .email(newDonorEmail)
                        .phone(newDonorPhone != null && !newDonorPhone.trim().isEmpty() ? newDonorPhone : "9876543210")
                        .password("donor123")
                        .confirmPassword("donor123")
                        .role(Role.DONOR)
                        .build();

                User user = userService.registerUser(dto);
                Donor newDonor = donorService.createDonor(user);
                targetDonorId = newDonor.getId();

                notificationService.sendNotification(user, NotificationType.REGISTRATION, "Welcome! Your donor profile has been created.");
                notificationService.notifyAllAdmins(NotificationType.SYSTEM, "New donor created during donation: " + newDonorName);
            }

            if (targetDonorId == null) {
                ra.addFlashAttribute("errorMessage", "Please select an existing donor or enter new donor details.");
                return "redirect:/admin/donations";
            }

            DonationDto dto = DonationDto.builder()
                    .donorId(targetDonorId)
                    .campaignId(campaignId)
                    .amount(amount)
                    .paymentMethod(paymentMethod)
                    .build();
            donationService.createDonation(dto);

            Donor donorObj = donorService.findById(targetDonorId).orElse(null);
            if (donorObj != null && donorObj.getUser() != null) {
                notificationService.sendNotification(donorObj.getUser(), NotificationType.DONATION, "Thank you! Received donation of ₹" + amount);
            }
            notificationService.notifyAllAdmins(NotificationType.SYSTEM, "Donation of ₹" + amount + " recorded successfully!");

            ra.addFlashAttribute("successMessage", "Donation recorded successfully!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Failed to record donation.");
        }
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

        if (volunteerId != null) {
            volunteerService.findById(volunteerId).ifPresent(v -> {
                if (v.getUser() != null) {
                    notificationService.sendNotification(v.getUser(), NotificationType.TASK, "New task assigned: " + title);
                }
            });
        }
        notificationService.notifyAllAdmins(NotificationType.SYSTEM, "New task created: " + title);

        ra.addFlashAttribute("successMessage", "Task created and assigned successfully!");
        return "redirect:/admin/tasks";
    }

    @PostMapping("/tasks/status")
    public String updateTaskStatus(@RequestParam("id") Long id, @RequestParam("status") TaskStatus status, RedirectAttributes ra) {
        taskService.updateTaskStatus(id, status);
        notificationService.notifyAllAdmins(NotificationType.SYSTEM, "Task status updated to: " + status);
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
