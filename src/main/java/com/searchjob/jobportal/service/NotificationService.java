package com.searchjob.jobportal.service;

import com.searchjob.jobportal.entity.Notification;
import com.searchjob.jobportal.entity.Users;
import com.searchjob.jobportal.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Users user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Users user) {
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    public List<Notification> getUnreadNotifications(Users user) {
        return notificationRepository.findByUserAndIsReadFalse(user);
    }

    public void markAllAsRead(Users user) {
        List<Notification> unread = notificationRepository.findByUserAndIsReadFalse(user);
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }
}