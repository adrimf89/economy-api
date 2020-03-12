package com.adri.economy.repository;

import com.adri.economy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByIban(String iban);
}
