package com.netcracker.billing.services;

import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.entity.OrderItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BillingService {
    Bill generateBill(Set<OrderItem> orderedItems, boolean isParcelOrder);

    List<Bill> fetchAllBills();

    List<Bill> fetchBills(LocalDate from, LocalDate to);
}
