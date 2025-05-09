package edu.ut.its;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ItsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsApplication.class, args);
    }

}
