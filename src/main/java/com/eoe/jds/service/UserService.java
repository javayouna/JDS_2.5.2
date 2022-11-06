package com.eoe.jds.service;

import com.eoe.jds.DataNotFoundException;
import com.eoe.jds.entity.SiteUser;
import com.eoe.jds.persistent.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(String username, String email, String password) {
        SiteUser siteUser = SiteUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .createDate(LocalDateTime.now())
                .build();
        this.userRepository.save(siteUser);

    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            try {
                throw new DataNotFoundException("siteuser not found");
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
