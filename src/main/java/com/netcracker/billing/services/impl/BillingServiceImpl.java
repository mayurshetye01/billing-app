package com.netcracker.billing.services.impl;

import com.netcracker.billing.config.ApplicationParameters;
import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.entity.Charges;
import com.netcracker.billing.entity.OrderItem;
import com.netcracker.billing.repository.BillingRepository;
import com.netcracker.billing.services.BillingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class BillingServiceImpl implements BillingService {
    private final ApplicationParameters applicationParameters;
    private final BillingRepository billingRepository;

    public BillingServiceImpl(ApplicationParameters applicationParameters, BillingRepository billingRepository) {
        this.applicationParameters = applicationParameters;
        this.billingRepository = billingRepository;
    }


    @Override
    public Bill generateBill(final Set<OrderItem> orderedItems, final boolean isParcelOrder) {
        validate(orderedItems);
        Bill bill = generateBaseBill(orderedItems, isParcelOrder);
        addCharges(bill);
        addToHistory(bill);
        return bill;
    }

    @Override
    public List<Bill> fetchAllBills() {
        return billingRepository.findAll();
    }

    @Override
    public List<Bill> fetchBills(LocalDate from, LocalDate to) {
        return billingRepository.findByGeneratedDateBetween(from, to);
    }

    private void addToHistory(Bill bill) {
        billingRepository.save(bill);
    }

    private Bill generateBaseBill(final Set<OrderItem> orderedItems, final boolean isParcelOrder) {
        Bill bill = new Bill();
        bill.setGeneratedDate(LocalDate.now());
        bill.setOrderedItems(orderedItems);
        bill.setParcelOrder(isParcelOrder);

        return bill;
    }

    private void addCharges(final Bill bill) {
        initCharges(bill);
        addItemCharges(bill);
        addParcelCharges(bill);
        setAmountPayable(bill);
    }

    private void initCharges(final Bill bill) {
        Charges charges = new Charges();
        bill.setCharges(charges);
    }

    private void addParcelCharges(final Bill bill) {
        if (bill.isParcelOrder()) {
            BigDecimal parcelCharges = applicationParameters.getParcelCharges();
            bill.getCharges().setParcelCharges(parcelCharges);
        } else
            bill.getCharges().setParcelCharges(BigDecimal.ZERO);
    }

    private void addItemCharges(final Bill bill) {
        Set<OrderItem> orderedItems = bill.getOrderedItems();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : orderedItems) {
            int quantity = orderItem.getQuantity();
            BigDecimal pricePerUnit = orderItem.getPricePerUnit();
            BigDecimal unitPrice = pricePerUnit.multiply(new BigDecimal(quantity));
            orderItem.setTotalItemPrice(unitPrice);
            totalPrice = totalPrice.add(unitPrice);
        }

        bill.getCharges().setItemCharges(totalPrice);
    }

    private void setAmountPayable(final Bill bill) {
        Charges charges = bill.getCharges();
        BigDecimal orderCharges = charges.getItemCharges();
        BigDecimal parcelCharges = charges.getParcelCharges();
        BigDecimal amountPayable = orderCharges.add(parcelCharges);

        bill.getCharges().setAmountPayable(amountPayable);
    }

    private void validate(final Set<OrderItem> orderedItems) {
        if (orderedItems == null || orderedItems.isEmpty())
            throw new IllegalArgumentException("Ordered items are empty");

        orderedItems.forEach(orderedItem -> {
            if (orderedItem == null)
                throw new IllegalArgumentException("Ordered item is empty");
        });
    }

}
