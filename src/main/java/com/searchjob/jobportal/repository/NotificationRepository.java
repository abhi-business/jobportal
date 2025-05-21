package com.searchjob.jobportal.repository;

import com.searchjob.jobportal.entity.Notification;
import com.searchjob.jobportal.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAndIsReadFalseOrderByTimestampDesc(Users user);
}
