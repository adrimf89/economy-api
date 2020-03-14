package com.adri.economy.repository;

import com.adri.economy.model.AccountMonthStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountMonthStatisticsRepository extends JpaRepository<AccountMonthStatistics, Long> {

    Optional<AccountMonthStatistics> findByAccountIdAndMonthAndYearAndCategoryIsNull(long id, int month, int year);

    Optional<AccountMonthStatistics> findByAccountIdAndMonthAndYearAndCategoryId(long accountId, int month, int year, long categoryId);
}
