package com.netcracker.billing.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items", uniqueConstraints = @UniqueConstraint(columnNames = {"order_item_id"}))
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Integer orderItemId;

    private int quantity;

    @Column(name = "total_item_price")
    private BigDecimal totalItemPrice;

    @Column(name = "item_code")
    private int itemCode;

    private String name;

    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @ManyToOne
    @JoinColumn
    private Bill bill;
}
