package com.adri.economy.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String role;

    @Column(nullable = false)
    private Date creationDate;

    private Date deletedDate;
}
