package com.adri.economy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"month", "year", "account_id", "operation_category_id"})
})
public class AccountMonthStatistics {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private BigDecimal expenses;

    @Column(nullable = false)
    private BigDecimal incomes;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "operation_category_id")
    private OperationCategory category;
}
