package com.netcracker.billing.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//TODO -> Refactor classes in client package
@Slf4j
@Component
public class ConsoleClient {


    private final LoginScreen loginScreen;
    private final MenuScreen menuScreen;
    private final OptionsScreen optionsScreen;

    public ConsoleClient(LoginScreen loginScreen, MenuScreen menuScreen, OptionsScreen optionsScreen) {
        this.loginScreen = loginScreen;
        this.menuScreen = menuScreen;
        this.optionsScreen = optionsScreen;
    }

    public void init() {
        loginScreen.showLoginPrompt();
        menuScreen.showInitMenuScreen();
        optionsScreen.showOptions();
    }

}
