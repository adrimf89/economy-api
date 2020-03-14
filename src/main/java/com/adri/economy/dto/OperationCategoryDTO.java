package com.adri.economy.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationCategoryDTO {

    private long id;
    private String name;
    private String description;
    private String icon;
    private Date creationDate;
    private Date deletedDate;
}
