package com.netcracker.billing;

import com.netcracker.billing.client.ConsoleClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class BillingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ConsoleClient consoleClient = ctx.getBean(ConsoleClient.class);
            consoleClient.init();
        };
    }

    @Bean
    public BufferedReader bufferedReader(){
        return new BufferedReader(new InputStreamReader(System.in));
    }
}
