package com.adri.economy.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceDTO {

    private Date date;
    private BigDecimal balance;
}
