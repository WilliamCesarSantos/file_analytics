package com.fileanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author william.santos
 * @since 0.1.0 - 2020-10-24
 */
@EnableScheduling
@SpringBootApplication
public class Main {

    /**
     * Init app
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
