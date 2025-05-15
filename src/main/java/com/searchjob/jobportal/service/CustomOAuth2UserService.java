package com.searchjob.jobportal.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.searchjob.jobportal.entity.JobSeekerProfile;
import com.searchjob.jobportal.entity.Users;
import com.searchjob.jobportal.entity.UsersType;
import com.searchjob.jobportal.repository.JobSeekerProfileRepository;
import com.searchjob.jobportal.repository.UsersRepository;
import com.searchjob.jobportal.repository.UsersTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersTypeRepository usersTypeRepository;

    // @PersistenceContext
    // private EntityManager entityManager;

    public CustomOAuth2UserService(UsersRepository usersRepository,
            JobSeekerProfileRepository jobSeekerProfileRepository,
            UsersTypeRepository usersTypeRepository) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersTypeRepository = usersTypeRepository;
    }

    @Transactional
@Override
public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
    String email = oAuth2User.getAttribute("email");

    Optional<Users> optionalUser = usersRepository.findByEmail(email);
    Users user;

    if (optionalUser.isEmpty()) {
        user = new Users();
        user.setEmail(email);
        user.setActive(true);
        user.setRegistrationDate(new Date());
        user.setPassword("google");

        // Set userType
        UsersType jobSeekerType = usersTypeRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("UserType not found"));
        user.setUserTypeId(jobSeekerType);

        Users savedUser = usersRepository.save(user);

        // Avoid detached entity issue by re-fetching
        Users managedUser = usersRepository.findById(savedUser.getUserId()).orElseThrow();

        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setUserId(managedUser);
        jobSeekerProfileRepository.save(profile);
    } else {
        user = optionalUser.get();
    }

    List<GrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(user.getUserTypeId().getUserTypeName())
    );

    return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
}


}
