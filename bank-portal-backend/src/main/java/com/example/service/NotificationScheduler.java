package com.example.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Run every 30 seconds (for testing; adjust in production)
    @Scheduled(fixedRate = 30000)
    public void sendPendingNotifications() {
        notificationService.processPendingNotifications();
    }
}
