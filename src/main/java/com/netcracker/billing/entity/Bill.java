package com.netcracker.billing.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "bills", uniqueConstraints = @UniqueConstraint(columnNames = {"bill_id"}))
public class Bill {
    @Id
    @GeneratedValue
    @Column(name = "bill_id")
    private Integer id;

    private LocalDate generatedDate;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private Set<OrderItem> orderedItems;

    private boolean isParcelOrder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "charges_id")
    private Charges charges;
}
