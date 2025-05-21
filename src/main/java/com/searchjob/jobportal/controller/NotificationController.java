package com.searchjob.jobportal.controller;

import com.searchjob.jobportal.entity.Notification;
import com.searchjob.jobportal.entity.Users;
import com.searchjob.jobportal.service.NotificationService;
import com.searchjob.jobportal.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsersService usersService;

    @GetMapping("/notifications/fetch")
    @ResponseBody
    public List<Map<String, Object>> fetchNotifications() {
        Users recruiter = usersService.getCurrentUser();
        List<Notification> notifications = notificationService.getUnreadNotifications(recruiter);

        return notifications.stream().map(n -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", n.getId());
            map.put("message", n.getMessage());
            map.put("jobSeekerId", n.getJobSeeker().getUserAccountId());
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/notifications/mark-read/{id}")
    @ResponseBody
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteNotif(@PathVariable Integer id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/mark-all-read")
    @ResponseBody
    public ResponseEntity<String> markAllAsRead() {
        Users recruiter = usersService.getCurrentUser();
        notificationService.markAllAsRead(recruiter);
        return ResponseEntity.ok("Marked all as read");
    }

}
