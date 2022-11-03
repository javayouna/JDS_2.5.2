package com.eoe.jds;

import com.eoe.jds.entity.SiteUser;
import com.eoe.jds.persistent.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class JdsApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	//테스트
	@Test
	void test_insert(){
		SiteUser su = SiteUser.builder()
				.username("test12232")
				.password("test")
				.email("test2122@test.com")
				.createDate(LocalDateTime.now())
				.build();
		this.userRepository.save(su);
	}

}
