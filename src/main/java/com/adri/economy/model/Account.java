package com.adri.economy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Date creationDate;

    private Date deletedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser owner;
}
