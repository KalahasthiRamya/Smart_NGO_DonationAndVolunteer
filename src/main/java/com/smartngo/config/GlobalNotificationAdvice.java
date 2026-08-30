package com.smartngo.config;

import com.smartngo.entity.Notification;
import com.smartngo.entity.User;
import com.smartngo.security.CustomUserDetails;
import com.smartngo.service.NotificationService;
import com.smartngo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalNotificationAdvice {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @ModelAttribute("unreadNotificationCount")
    public long populateUnreadCount(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            return notificationService.countUnreadNotifications(user);
        }
        return 0;
    }

    @ModelAttribute("recentNotifications")
    public List<Notification> populateRecentNotifications(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            return notificationService.getUserNotifications(user);
        }
        return Collections.emptyList();
    }
}
