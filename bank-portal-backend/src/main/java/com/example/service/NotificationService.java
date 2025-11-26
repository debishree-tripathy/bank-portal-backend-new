package com.example.service;

import com.example.model.Notification;
import com.example.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Save a new notification
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // Get all notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Get a notification by ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Update notification
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // ✅ Process pending notifications (used by scheduler)
    public void processPendingNotifications() {
        List<Notification> pendingList = notificationRepository.findByStatus("PENDING");

        for (Notification n : pendingList) {
            System.out.println("📨 Sending " + n.getType() + " to User " + n.getUserId());

            // Update status and sent time
            n.setStatus("SENT");
            n.setSentOn(LocalDateTime.now());

            notificationRepository.save(n);
        }
    }
}
