package com.smartngo.controller;

import com.smartngo.entity.User;
import com.smartngo.security.CustomUserDetails;
import com.smartngo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notifications/mark-read/{id}")
    public String markRead(@PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes ra) {
        notificationService.markAsRead(id);
        ra.addFlashAttribute("successMessage", "Notification marked as read.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/notifications/mark-all-read")
    public String markAllRead(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request, RedirectAttributes ra) {
        if (userDetails != null) {
            notificationService.markAllAsRead(userDetails.getUser());
            ra.addFlashAttribute("successMessage", "All notifications marked as read.");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/donor/notifications")
    public String donorNotifications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            model.addAttribute("notifications", notificationService.getUserNotifications(user));
        }
        return "donor/notifications";
    }

    @GetMapping("/volunteer/notifications")
    public String volunteerNotifications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            model.addAttribute("notifications", notificationService.getUserNotifications(user));
        }
        return "volunteer/notifications";
    }
}
