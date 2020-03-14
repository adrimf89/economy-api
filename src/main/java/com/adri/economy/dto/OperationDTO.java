package com.adri.economy.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDTO {

    private long id;
    private String description;
    private String info;
    private BigDecimal amount;
    private BigDecimal currencyAmount;
    private String currency;
    private Date date;
    private long accountId;
    private OperationCategoryDTO category;
}
