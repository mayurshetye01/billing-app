package com.netcracker.billing.repository;

import com.netcracker.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findByGeneratedDateBetween(LocalDate from, LocalDate to);
}
