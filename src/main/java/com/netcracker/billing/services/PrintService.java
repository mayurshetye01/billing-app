package com.netcracker.billing.services;

import com.netcracker.billing.entity.Bill;

public interface PrintService {
    void print(Bill bill);
}
