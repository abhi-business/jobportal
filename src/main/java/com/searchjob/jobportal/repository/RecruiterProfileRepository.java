package com.searchjob.jobportal.repository;

import com.searchjob.jobportal.entity.RecruiterProfile;
import com.searchjob.jobportal.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {
    RecruiterProfile findByUserId(Users user);
}
