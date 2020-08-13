package com.netcracker.billing.client;

import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.entity.MenuItem;
import com.netcracker.billing.entity.OrderItem;
import com.netcracker.billing.services.BillingService;
import com.netcracker.billing.services.MenuService;
import com.netcracker.billing.services.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.netcracker.billing.client.Constants.INVALID_INPUT_MSG;
import static com.netcracker.billing.client.ScreenUtils.isYes;

@Slf4j
@Component
public class BillGenerationScreen {
    private final BufferedReader bufferedReader;
    private final MenuService menuService;
    private final BillingService billingService;
    private final PrintService printService;

    public BillGenerationScreen(BufferedReader bufferedReader, MenuService menuService, BillingService billingService, PrintService printService) {
        this.bufferedReader = bufferedReader;
        this.menuService = menuService;
        this.billingService = billingService;
        this.printService = printService;
    }

    public void showGenerateBillScreen() {
        log.info("Add items ordered");
        Set<OrderItem> orderedItems = new HashSet<>();
        boolean submit = false;
        while (!submit) {
            log.info("ID");
            int itemId;
            try {
                itemId = Integer.parseInt(bufferedReader.readLine());
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
                continue;
            }

            try {
                MenuItem menuItem = menuService.get(itemId);
                OrderItem orderItem = new OrderItem();
                orderItem.setItemCode(menuItem.getItemCode());
                orderItem.setName(menuItem.getName());
                orderItem.setPricePerUnit(menuItem.getPricePerUnit());
                log.info("Quantity");
                orderItem.setQuantity(Integer.parseInt(bufferedReader.readLine()));

                orderedItems.add(orderItem);
            } catch (NoSuchElementException e) {
                log.info("Item with ID {} is not on Menu", itemId);
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
            }

            log.info("Add more items? y/N");
            String choice;
            try {
                choice = bufferedReader.readLine();
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
                choice = "Y";
            }
            if (!isYes(choice))
                submit = true;
        }

        final Bill bill;
        if (orderedItems.isEmpty()) {
            log.info("Cannot generate bill for empty order");
        } else {
            boolean isParcelOrder = false;
            log.info("Add parcel charges? y/N");
            String input;
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                log.info(INVALID_INPUT_MSG);
                input = "N";
            }
            if (isYes(input))
                isParcelOrder = true;
            bill = billingService.generateBill(orderedItems, isParcelOrder);
            printService.print(bill);
        }
    }
}
