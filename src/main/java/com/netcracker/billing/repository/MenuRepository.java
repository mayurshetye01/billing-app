package com.netcracker.billing.repository;

import com.netcracker.billing.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Integer> {

}
