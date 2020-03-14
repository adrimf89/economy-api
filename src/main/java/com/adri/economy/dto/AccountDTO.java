package com.adri.economy.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private long id;
    private String iban;
    private String description;
    private String currency;
    private Date creationDate;
    private Date deletedDate;
    private AppUserDTO owner;
    private AccountBalanceDTO currentBalance;
}
