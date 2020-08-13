package com.netcracker.billing.client;

import com.netcracker.billing.entity.MenuItem;
import com.netcracker.billing.services.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static com.netcracker.billing.client.Constants.DASHED_SEPARATOR;
import static com.netcracker.billing.client.ScreenUtils.isYes;

@Slf4j
@Component
public class ItemsScreen {
    private final MenuService menuService;
    private final BufferedReader bufferedReader;

    public ItemsScreen(MenuService menuService, BufferedReader bufferedReader) {
        this.menuService = menuService;
        this.bufferedReader = bufferedReader;
    }

    public void showDeleteItemScreen() throws IOException {
        log.info("Input ID of Item to be deleted from Menu");
        int itemCode = Integer.parseInt(bufferedReader.readLine());

        try {
            menuService.deleteByItemCode(itemCode);
        } catch (NoSuchElementException e) {
            log.info("Item with ID {} is not on Menu", itemCode);
        }
    }

    public void showModifyItemScreen() throws IOException {
        log.info("Input ID of Item to modify");
        int itemId = Integer.parseInt(bufferedReader.readLine());

        try {
            MenuItem menuItem = menuService.get(itemId);
            log.info("ID\t|Name\t|Price\t|Available");
            log.info(DASHED_SEPARATOR);
            log.info("{}\t{}\t{}\t{}", menuItem.getItemCode(), menuItem.getName(), menuItem.getPricePerUnit(), menuItem.isAvailable());
            log.info("Update values");
            log.info("Name");
            menuItem.setName(bufferedReader.readLine());
            log.info("Price");
            menuItem.setPricePerUnit(new BigDecimal(Integer.parseInt(bufferedReader.readLine())));
            log.info("Is Available? y/N");
            String isAvailableStr = bufferedReader.readLine();
            if (isYes(isAvailableStr))
                menuItem.setAvailable(true);
            else
                menuItem.setAvailable(false);

            menuService.update(menuItem);
        } catch (NoSuchElementException e) {
            log.info("Item with ID {} is not on Menu", itemId);
        }

    }

    public void showAddItemScreen() throws IOException {
        MenuItem menuItem = new MenuItem();

        log.info("Name");
        menuItem.setName(bufferedReader.readLine());
        log.info("Price");
        menuItem.setPricePerUnit(new BigDecimal(bufferedReader.readLine()));
        log.info("Is Available? y/N");
        String isAvailableStr = bufferedReader.readLine();
        if (isYes(isAvailableStr))
            menuItem.setAvailable(true);
        else
            menuItem.setAvailable(false);

        menuService.add(menuItem);
    }
}
