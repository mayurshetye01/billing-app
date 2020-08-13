package com.netcracker.billing.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "charges", uniqueConstraints = @UniqueConstraint(columnNames = {"charges_id"}))
public class Charges {
    @Id
    @GeneratedValue
    @Column(name = "charges_id")
    private Integer id;
    
    private BigDecimal itemCharges;
    private BigDecimal parcelCharges;
    private BigDecimal amountPayable;

}
