package com.adri.economy.repository;

import com.adri.economy.model.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    AccountBalance findFirstByAccountIdOrderByDateDesc(long id);

    Optional<AccountBalance> findFirstByAccountIdAndDateLessThanOrderByDateDesc(long id, Date date);

    Optional<AccountBalance> findByAccountIdAndDate(long id, Date date);

}
