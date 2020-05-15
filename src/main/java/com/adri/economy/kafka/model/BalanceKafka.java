package com.adri.economy.kafka.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceKafka {

    private int count;
    private BigDecimal balance;
    private String time;
}
