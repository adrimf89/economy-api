package com.adri.economy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;

    private String info;

    @Column(nullable = false)
    private BigDecimal amount;

    private BigDecimal currencyAmount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "operation_category_id")
    private OperationCategory category;
}
