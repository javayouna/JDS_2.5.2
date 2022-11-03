package com.eoe.jds.persistent;

import com.eoe.jds.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    //사용자조회

    Optional<SiteUser> findByusername(String username);
}
