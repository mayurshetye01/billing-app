package com.netcracker.billing.services;

import com.netcracker.billing.config.ApplicationParameters;
import com.netcracker.billing.entity.Bill;
import com.netcracker.billing.entity.Charges;
import com.netcracker.billing.entity.OrderItem;
import com.netcracker.billing.repository.BillingRepository;
import com.netcracker.billing.services.impl.BillingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillingServiceTest {

    @InjectMocks
    BillingServiceImpl billingService;

    @Mock
    ApplicationParameters applicationParameters;

    @Mock
    BillingRepository billingRepository;

    @Test
    void testGenerateBill() {
        Set<OrderItem> testOrderedItems = getTestOrderedItems();
        Bill result = billingService.generateBill(testOrderedItems, false);
        assertEquals(testOrderedItems.size(), result.getOrderedItems().size());
        Charges resultCharges = result.getCharges();
        assertEquals(BigDecimal.ZERO, resultCharges.getParcelCharges());
        assertEquals(BigDecimal.valueOf(140), resultCharges.getItemCharges());
        assertEquals(BigDecimal.valueOf(140), resultCharges.getAmountPayable());
    }

    @Test
    void testGenerateBillWithParcelCharges() {
        Set<OrderItem> testOrderedItems = getTestOrderedItems();

        when(applicationParameters.getParcelCharges()).thenReturn(BigDecimal.valueOf(20));

        Bill result = billingService.generateBill(testOrderedItems, true);

        assertEquals(testOrderedItems.size(), result.getOrderedItems().size());
        Charges resultCharges = result.getCharges();
        assertEquals(BigDecimal.valueOf(20), resultCharges.getParcelCharges());
        assertEquals(BigDecimal.valueOf(140), resultCharges.getItemCharges());
        assertEquals(BigDecimal.valueOf(160), resultCharges.getAmountPayable());
    }

    @Test
    void testFetchAllBills() {
        List<Bill> testBills = getTestBills();
        when(billingRepository.findAll()).thenReturn(testBills);
        List<Bill> result = billingService.fetchAllBills();
        assertEquals(testBills.size(), result.size());
        assertTrue(testBills.containsAll(result));
    }

    @Test
    void testFetchBillsWithingDateRange() {
        List<Bill> testBills = getTestBills();
        List<Bill> filteredBills = testBills.
                stream().
                filter(bill -> Month.JANUARY.equals(bill.getGeneratedDate().getMonth()))
                .collect(Collectors.toList());
        when(billingRepository.findByGeneratedDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(filteredBills);
        LocalDate from = LocalDate.now().withMonth(Month.JANUARY.getValue());
        LocalDate to = LocalDate.now().withMonth(Month.DECEMBER.getValue());
        List<Bill> result = billingService.fetchBills(from, to);
        assertEquals(1, result.size());
    }


    private Set<OrderItem> getTestOrderedItems() {
        Set<OrderItem> testOrderedItems = new HashSet<>();
        testOrderedItems.add(getTestOrderItem(1, BigDecimal.valueOf(10), 1));
        testOrderedItems.add(getTestOrderItem(2, BigDecimal.valueOf(20), 2));
        testOrderedItems.add(getTestOrderItem(3, BigDecimal.valueOf(30), 3));

        return testOrderedItems;
    }

    private OrderItem getTestOrderItem(int itemCode, BigDecimal pricePerUnit, int quantity) {
        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setItemCode(itemCode);
        testOrderItem.setPricePerUnit(pricePerUnit);
        testOrderItem.setQuantity(quantity);

        return testOrderItem;
    }


    private List<Bill> getTestBills() {
        List<Bill> testBills = new ArrayList<>();
        testBills.add(getTestBill(1, Month.JANUARY));
        testBills.add(getTestBill(2, Month.FEBRUARY));

        return testBills;
    }

    private Bill getTestBill(int id, Month month) {
        Bill testBill = new Bill();
        testBill.setId(1);
        testBill.setGeneratedDate(LocalDate.of(2020, month, 1));
        testBill.setCharges(new Charges());
        return testBill;
    }
}
