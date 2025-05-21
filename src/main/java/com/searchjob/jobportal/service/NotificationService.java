package com.searchjob.jobportal.service;

import com.searchjob.jobportal.entity.JobSeekerProfile;
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

    public void sendNotification(Users recruiter, JobSeekerProfile candidate, String message) {
        Notification notification = new Notification();
        notification.setUser(recruiter);
        notification.setJobSeeker(candidate);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(Users user) {
        return notificationRepository.findByUserAndIsReadFalseOrderByTimestampDesc(user);
    }

    public void markAsRead(Integer id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(Users user) {
        List<Notification> unread = getUnreadNotifications(user);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    public void deleteNotificationById(Integer id) {
        notificationRepository.deleteById(id);
    }
}
