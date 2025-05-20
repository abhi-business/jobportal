package com.searchjob.jobportal.controller;

import com.searchjob.jobportal.entity.Users;
import com.searchjob.jobportal.repository.RecruiterProfileRepository;
import com.searchjob.jobportal.service.NotificationService;
import com.searchjob.jobportal.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    public record NotificationDto(String message) {
    }

    @GetMapping("/notifications/fetch")
    @ResponseBody
    public List<NotificationDto> fetchNotifications() {
        Users recruiter = usersService.getCurrentUser();
        return notificationService.getUnreadNotifications(recruiter).stream()
                .map(n -> new NotificationDto(n.getMessage()))
                .collect(Collectors.toList());
    }

}
