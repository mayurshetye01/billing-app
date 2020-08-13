package com.netcracker.billing.client;

import com.netcracker.billing.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

import static com.netcracker.billing.client.Constants.INVALID_INPUT_MSG;

@Slf4j
@Component
public class LoginScreen {
    private final BufferedReader bufferedReader;
    private final AuthenticationService authenticationService;

    public LoginScreen(AuthenticationService authenticationService, BufferedReader bufferedReader) {
        this.authenticationService = authenticationService;
        this.bufferedReader = bufferedReader;
    }

    public void showLoginPrompt() {
        log.info("Please login");
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            try {
                log.info("Username");
                String username = bufferedReader.readLine();
                log.info("Password");
                String password = bufferedReader.readLine();
                loginSuccessful = authenticationService.login(username, password);
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
            }
        }
    }
}
