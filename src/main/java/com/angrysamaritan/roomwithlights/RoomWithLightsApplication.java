package com.angrysamaritan.roomwithlights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class RoomWithLightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomWithLightsApplication.class, args);
    }

}
