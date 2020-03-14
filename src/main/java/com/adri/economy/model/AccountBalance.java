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
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"date", "account_id"})
})
public class AccountBalance {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
