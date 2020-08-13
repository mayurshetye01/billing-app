package com.netcracker.billing.client;

import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.services.BillingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.netcracker.billing.client.Constants.DASHED_SEPARATOR;

@Slf4j
@Component
public class BillingHistoryScreen {

    private final BillingService billingService;
    private final BufferedReader bufferedReader;

    public BillingHistoryScreen(BillingService billingService, BufferedReader bufferedReader) {
        this.billingService = billingService;
        this.bufferedReader = bufferedReader;
    }

    public void showBillHistoryScreen() throws IOException {
        log.info("1 - View Bills generated today");
        log.info("2 - View Bills generated for current month");
        log.info("3 - View all Bills");

        int choice = Integer.parseInt(bufferedReader.readLine());
        navigate(choice);
    }

    public void navigate(int screenId) {
        switch (screenId) {
            case 1:
                showBillsForToday();
                break;
            case 2:
                showBillsForCurrentMonth();
                break;
            case 3:
                showAllBills();
                break;
            default:
                log.info("Invalid screen id");
        }
    }

    private void showAllBills() {
        List<Bill> allBills = billingService.fetchAllBills();
        show(allBills);
    }

    private void showBillsForCurrentMonth() {
        LocalDate current = LocalDate.now();
        LocalDate from = current.withDayOfMonth(1);
        LocalDate to = current.withDayOfMonth(current.lengthOfMonth());
        List<Bill> currentMonthBills = billingService.fetchBills(from, to);
        show(currentMonthBills);
    }

    private void showBillsForToday() {
        LocalDate current = LocalDate.now();
        List<Bill> currentMonthBills = billingService.fetchBills(current, current);
        show(currentMonthBills);
    }

    private void show(List<Bill> bills) {
        log.info(DASHED_SEPARATOR);
        log.info("Date\t\t| Amount Collected");
        log.info(DASHED_SEPARATOR);
        bills.forEach(bill -> {
            log.info("{}\t  {}", bill.getGeneratedDate(), bill.getCharges().getAmountPayable());
        });
    }
}
