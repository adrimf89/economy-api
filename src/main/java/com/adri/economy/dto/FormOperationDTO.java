package com.adri.economy.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormOperationDTO {

    private String description;
    private String info;
    private BigDecimal amount;
    private BigDecimal currencyAmount;
    private String currency;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private long accountId;
    private Long categoryId;
}
