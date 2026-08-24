package com.smartngo.controller;

import com.smartngo.entity.Volunteer;
import com.smartngo.enums.CampaignStatus;
import com.smartngo.enums.TaskStatus;
import com.smartngo.security.CustomUserDetails;
import com.smartngo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AttendanceService attendanceService;

    private Volunteer getLoggedInVolunteer(CustomUserDetails userDetails) {
        return volunteerService.findByUserId(userDetails.getUser().getId()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String volunteerDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Volunteer volunteer = getLoggedInVolunteer(userDetails);
        if (volunteer != null) {
            model.addAttribute("volunteer", volunteer);
            model.addAttribute("myTasks", taskService.findByVolunteer(volunteer));
            model.addAttribute("attendancePercentage", attendanceService.calculateAttendancePercentage(volunteer));
        }
        model.addAttribute("activeCampaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "volunteer/dashboard";
    }

    @GetMapping("/profile")
    public String volunteerProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("volunteer", getLoggedInVolunteer(userDetails));
        return "volunteer/profile";
    }

    @PostMapping("/profile/update")
    public String updateSkills(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam("skills") String skills,
                               RedirectAttributes ra) {
        Volunteer volunteer = getLoggedInVolunteer(userDetails);
        if (volunteer != null) {
            volunteerService.updateVolunteerSkillsAndStatus(volunteer.getId(), skills, volunteer.getStatus());
            ra.addFlashAttribute("successMessage", "Skills updated successfully!");
        }
        return "redirect:/volunteer/profile";
    }

    @GetMapping("/opportunities")
    public String opportunities(Model model) {
        model.addAttribute("campaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "volunteer/opportunities";
    }

    @GetMapping("/tasks")
    public String myTasks(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Volunteer volunteer = getLoggedInVolunteer(userDetails);
        if (volunteer != null) {
            model.addAttribute("tasks", taskService.findByVolunteer(volunteer));
        } else {
            model.addAttribute("tasks", Collections.emptyList());
        }
        model.addAttribute("taskStatuses", TaskStatus.values());
        return "volunteer/tasks";
    }

    @PostMapping("/tasks/status")
    public String updateMyTaskStatus(@RequestParam("taskId") Long taskId,
                                     @RequestParam("status") TaskStatus status,
                                     RedirectAttributes ra) {
        taskService.updateTaskStatus(taskId, status);
        ra.addFlashAttribute("successMessage", "Task status updated!");
        return "redirect:/volunteer/tasks";
    }

    @GetMapping("/attendance")
    public String volunteerAttendance(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Volunteer volunteer = getLoggedInVolunteer(userDetails);
        if (volunteer != null) {
            model.addAttribute("attendanceList", attendanceService.findByVolunteer(volunteer));
            model.addAttribute("attendancePercentage", attendanceService.calculateAttendancePercentage(volunteer));
        }
        return "volunteer/attendance";
    }

    @GetMapping("/impact")
    public String volunteerImpact(Model model) {
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        return "volunteer/impact";
    }
}
