package com.adri.economy.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormAccountDTO {

    private String iban;
    private String description;
    private String currency;
    private BigDecimal initialBalance;
    private long ownerId;
}
