package com.eoe.jds.service;

import com.eoe.jds.entity.SiteUser;
import com.eoe.jds.persistent.UserRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Log4j2
public class UserService_backup {

    private final UserRepository userRepository;
    //회원가입


    public void create(String username,String email,String password) {
        SiteUser siteUser = SiteUser.builder()
                .username(username)
                .email(email)
                .password(password)
                .createDate(LocalDateTime.now())
                .build();
        this.userRepository.save(siteUser);

    }
    //작성자
    public SiteUser getUser(String username) throws DataNotFoundException {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

}
