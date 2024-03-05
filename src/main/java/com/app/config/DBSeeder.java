package com.app.config;

import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.util.CommonUtil;
import org.springframework.context.annotation.Configuration;
import java.sql.Timestamp;

@Configuration
public class DBSeeder {
    private final UserRepository userRepository;

    public DBSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
        initData();
    }

    /**
     * create one admin user when application starts
     * email : jabbi@gmail.com
     * password : 123456
     */
    private void initData() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setFirstName("Jabbi");
            user.setLastName("01");
            user.setGender("MALE");
            user.setEmail("jabbi@gmail.com");
            user.setStatus("Approved");
            user.setRole("ADMIN");
            user.setPassword(CommonUtil.getEncodedPassword("123456"));

            user.setAddress("United Kingdom");
            user.setCity("London");
            user.setPostCode("E16AN");
            user.setTelephone("454545455");

            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }

    }
}
