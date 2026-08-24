package com.smartngo.service;

import com.smartngo.entity.Notification;
import com.smartngo.entity.User;
import com.smartngo.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    Notification sendNotification(User user, NotificationType type, String message);
    List<Notification> getUserNotifications(User user);
    long countUnreadNotifications(User user);
    void markAsRead(Long notificationId);
}
