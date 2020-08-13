package com.netcracker.billing.services.impl;

import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.entity.Charges;
import com.netcracker.billing.services.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrintServiceImpl implements PrintService {
    private static final String DASHED_LINE_SEPARATOR = "--------------------------------------------";

    public void print(Bill bill) {
        Charges charges = bill.getCharges();
        if (charges == null)
            throw new IllegalArgumentException("Charges are empty");


        log.info("Bill ID: {}", bill.getId());
        log.info("Date: {}", bill.getGeneratedDate());
        log.info(DASHED_LINE_SEPARATOR);
        log.info("Name\t| Item Price\t| Quantity\t| Price ");
        log.info(DASHED_LINE_SEPARATOR);
        bill.getOrderedItems().forEach(orderItem -> {
            log.info("{}\t\t  {}\t\t\t  {}\t\t  {}", orderItem.getName(), orderItem.getPricePerUnit(), orderItem.getQuantity(), orderItem.getTotalItemPrice());
        });
        log.info(DASHED_LINE_SEPARATOR);
        log.info("Total: {}", charges.getItemCharges());
        log.info("Parcel charges: {}", charges.getParcelCharges());
        log.info("Amount Payable: {}", charges.getAmountPayable());
    }
}
