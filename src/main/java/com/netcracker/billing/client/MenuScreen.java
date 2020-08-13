package com.netcracker.billing.client;

import com.netcracker.billing.entity.MenuItem;
import com.netcracker.billing.services.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static com.netcracker.billing.client.Constants.DASHED_SEPARATOR;
import static com.netcracker.billing.client.Constants.INVALID_INPUT_MSG;
import static com.netcracker.billing.client.ScreenUtils.isYes;

@Slf4j
@Component
public class MenuScreen {
    private final MenuService menuService;
    private final BufferedReader bufferedReader;
    private final ItemsScreen itemsScreen;

    public MenuScreen(MenuService menuService, BufferedReader bufferedReader, ItemsScreen itemsScreen) {
        this.menuService = menuService;
        this.bufferedReader = bufferedReader;
        this.itemsScreen = itemsScreen;
    }

    public void displayMenu() {
        List<MenuItem> menuItems = menuService.getMenuItems();
        log.info("MENU");
        log.info(DASHED_SEPARATOR);
        log.info("ID\t| Name\t\t\t| Price");
        log.info(DASHED_SEPARATOR);
        menuItems.forEach(menuItem -> {
            log.info("{}\t\t{}\t\t\t{}", menuItem.getItemCode(), menuItem.getName(), menuItem.getPricePerUnit());
        });
        log.info(DASHED_SEPARATOR);
    }

    public void showInitMenuScreen() {
        List<MenuItem> menu = menuService.getMenuItems();
        if (menu.isEmpty())
            initMenuPrompt();
    }

    private void initMenuPrompt() {
        log.info("Menu is empty. Please add some items");
        boolean repeat = true;
        while (repeat) {
            try {
                itemsScreen.showAddItemScreen();
                log.info("Add another item? y/N");
                String choice = bufferedReader.readLine();
                if (!isYes(choice))
                    repeat = false;
            } catch (IOException e) {
                log.error(INVALID_INPUT_MSG);
                repeat = false;
            }
        }
    }

    public void showClearMenuScreen() {
        log.info("Do you want to clear ALL items? y/N");
        try {
            String input = bufferedReader.readLine();
            if (isYes(input))
                menuService.clearMenu();
        } catch (IOException e) {
            log.error(INVALID_INPUT_MSG);
        }
    }
}
