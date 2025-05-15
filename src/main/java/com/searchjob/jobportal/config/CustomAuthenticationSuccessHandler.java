package com.searchjob.jobportal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.searchjob.jobportal.entity.Users;
import com.searchjob.jobportal.repository.UsersRepository;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsersRepository usersRepository;

    @Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                     Authentication authentication) throws IOException, ServletException {
    Object principal = authentication.getPrincipal();
    String username;

    if (principal instanceof UserDetails) {
        username = ((UserDetails) principal).getUsername();
        //System.out.println("PRINCIPAL***** "+username);
    } else if (principal instanceof OAuth2User) {
        //System.out.println("PRINCIPAL***** ");
        username = ((OAuth2User) principal).getAttribute("email");
    } else {
        throw new IllegalStateException("Unknown principal type: " + principal.getClass());
    }

    Users user = usersRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    boolean hasJobSeekerRole = authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals("Job Seeker"));
    boolean hasRecruiterRole = authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals("Recruiter"));

    if (hasRecruiterRole || hasJobSeekerRole) {
        response.sendRedirect("/dashboard/");
    } else {
        response.sendRedirect("/");
    }
}

}
