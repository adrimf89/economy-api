package com.adri.economy.dto;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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
    @JsonIgnore
    private long ownerId;
}
