package com.netcracker.billing.services.impl;

import com.netcracker.billing.config.ApplicationParameters;
import com.netcracker.billing.services.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ApplicationParameters applicationParameters;

    public AuthenticationServiceImpl(ApplicationParameters applicationParameters) {
        this.applicationParameters = applicationParameters;
    }

    @Override
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            return false;

        String systemUsername = applicationParameters.getSystemUsername();
        String systemPassword = applicationParameters.getSystemPassword();

        return username.equals(systemUsername) && password.equals(systemPassword);
    }
}
