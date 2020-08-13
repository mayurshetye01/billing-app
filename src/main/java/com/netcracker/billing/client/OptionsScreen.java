package com.netcracker.billing.client;

import com.netcracker.billing.services.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

import static com.netcracker.billing.client.Constants.INVALID_INPUT_MSG;

@Slf4j
@Component
public class OptionsScreen {
    private final BufferedReader bufferedReader;
    private final MenuService menuService;
    private final ItemsScreen itemsScreen;
    private final MenuScreen menuScreen;
    private final BillingHistoryScreen billingHistoryScreen;
    private final BillGenerationScreen billGenerationScreen;

    public OptionsScreen(BufferedReader bufferedReader, MenuService menuService, ItemsScreen itemsScreen, MenuScreen menuScreen, BillingHistoryScreen billingHistoryScreen, BillGenerationScreen billGenerationScreen) {
        this.bufferedReader = bufferedReader;
        this.menuService = menuService;
        this.itemsScreen = itemsScreen;
        this.menuScreen = menuScreen;
        this.billingHistoryScreen = billingHistoryScreen;
        this.billGenerationScreen = billGenerationScreen;
    }

    public void showOptions() {
        boolean quit = false;
        while (!quit) {
            log.info("\n");
            log.info("***** Options ******");
            log.info("1 - View Menu");
            log.info("2 - Add item");
            log.info("3 - Modify item");
            log.info("4 - Delete item");
            log.info("5 - Generate bill");
            log.info("6 - View previous bills");
            log.info("7 - Clear Menu");
            log.info("8 - Quit");

            try {
                int input = Integer.parseInt(bufferedReader.readLine());
                if (input == 8)
                    quit = true;
                else
                    navigate(input);
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
                showOptions();
            }
        }
    }

    private void navigate(int input) throws IOException {
        switch (input) {
            case 1:
                menuScreen.displayMenu();
                break;
            case 2:
                itemsScreen.showAddItemScreen();
                break;
            case 3:
                itemsScreen.showModifyItemScreen();
                break;
            case 4:
                itemsScreen.showDeleteItemScreen();
                break;
            case 5:
                billGenerationScreen.showGenerateBillScreen();
                break;
            case 6:
                billingHistoryScreen.showBillHistoryScreen();
                break;
            case 7:
                menuScreen.showClearMenuScreen();
                break;
            default:
                showOptions();
        }
    }
}
