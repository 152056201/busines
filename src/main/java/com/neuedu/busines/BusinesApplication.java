package com.neuedu.busines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BusinesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinesApplication.class, args);
    }

}
