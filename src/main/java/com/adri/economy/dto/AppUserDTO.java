package com.adri.economy.dto;

import lombok.*;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private Date creationDate;
    private Date deletedDate;
    private Collection<RoleDTO> roles;
}
