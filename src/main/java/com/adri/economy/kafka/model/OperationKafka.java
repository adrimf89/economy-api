package com.adri.economy.kafka.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationKafka {

    private long id;
    private BigDecimal amount;
    private BigDecimal currencyAmount;
    private String currency;
    private String timestamp;
    private long accountId;
    private Long categoryId;
}
