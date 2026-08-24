package com.smartngo.service.impl;

import com.smartngo.entity.Notification;
import com.smartngo.entity.User;
import com.smartngo.enums.NotificationType;
import com.smartngo.repository.NotificationRepository;
import com.smartngo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Value("${notification.mode:mock}")
    private String notificationMode;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification sendNotification(User user, NotificationType type, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .message(message)
                .status("UNREAD")
                .build();

        Notification saved = notificationRepository.save(notification);

        if ("mock".equalsIgnoreCase(notificationMode)) {
            log.info("[MOCK NOTIFICATION SERVICE] Sent {} to user {}: {}", type, user.getEmail(), message);
        } else {
            log.info("[EMAIL/SMS API SERVICE] Dispatched {} alert to {}", type, user.getEmail());
        }

        return saved;
    }

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public long countUnreadNotifications(User user) {
        return notificationRepository.countByUserAndStatus(user, "UNREAD");
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setStatus("READ");
            notificationRepository.save(n);
        });
    }
}
