/**
 * This is the main class of the application.
 * It is used to start the application.
 * It is also used to log the start of the application.
 *
 * @author Group 2 of XJCO2913
 * @version 1.0
 * @since 2022-2023
 */

package com.gym.gymmaster;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class GymMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymMasterApplication.class, args);
        // log the start of the system
        log.info("system successfully started!");

    }

}