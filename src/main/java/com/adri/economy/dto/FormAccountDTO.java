package com.adri.economy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormAccountDTO {

    private String iban;
    private String description;
    private String currency;
    private long ownerId;
}
