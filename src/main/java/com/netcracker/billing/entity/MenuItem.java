package com.netcracker.billing.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "menu", uniqueConstraints = @UniqueConstraint(columnNames = {"item_code"}))
public class MenuItem {
    @Id
    @GeneratedValue
    @Column(name = "item_code")
    private int itemCode;

    private String name;

    private BigDecimal pricePerUnit;

    private boolean isAvailable;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        if (!super.equals(obj))
            return false;
        MenuItem menuItem = (MenuItem) obj;
        return itemCode == menuItem.getItemCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemCode);
    }
}
