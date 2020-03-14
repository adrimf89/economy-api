package com.adri.economy.service;

import com.adri.economy.model.AccountMonthStatistics;
import com.adri.economy.model.Operation;
import com.adri.economy.repository.AccountMonthStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class AccountMonthStatisticsService {

    private final AccountMonthStatisticsRepository accountMonthStatisticsRepository;

    @Transactional
    public void updateAccountStatistics(Operation operation){
        final long accountId = operation.getAccount().getId();

        Calendar cal = Calendar.getInstance();
        cal.setTime(operation.getDate());
        final int month = cal.get(Calendar.MONTH)+1;
        final int year = cal.get(Calendar.YEAR);
        final long categoryId = operation.getCategory().getId();

        AccountMonthStatistics totalStats = accountMonthStatisticsRepository
                .findByAccountIdAndMonthAndYearAndCategoryIsNull(accountId, month, year)
                .orElse(new AccountMonthStatistics());
        AccountMonthStatistics categoryStats = accountMonthStatisticsRepository
                .findByAccountIdAndMonthAndYearAndCategoryId(accountId, month, year, categoryId)
                .orElse(new AccountMonthStatistics());

        if (totalStats.getId() == 0){
            totalStats.setAccount(operation.getAccount());
            totalStats.setMonth(month);
            totalStats.setYear(year);
            if (operation.getAmount().compareTo(BigDecimal.ZERO) > 0){
                totalStats.setIncomes(operation.getAmount());
                totalStats.setExpenses(BigDecimal.ZERO);
            } else {
                totalStats.setExpenses(operation.getAmount().negate());
                totalStats.setIncomes(BigDecimal.ZERO);
            }
        } else {
            if (operation.getAmount().compareTo(BigDecimal.ZERO) > 0){
                totalStats.setIncomes(totalStats.getIncomes().add(operation.getAmount()));
            } else {
                totalStats.setExpenses(totalStats.getExpenses().add(operation.getAmount().negate()));
            }
        }

        if (categoryStats.getId() == 0){
            categoryStats.setAccount(operation.getAccount());
            categoryStats.setMonth(month);
            categoryStats.setYear(year);
            categoryStats.setCategory(operation.getCategory());
            if (operation.getAmount().compareTo(BigDecimal.ZERO) > 0){
                categoryStats.setIncomes(operation.getAmount());
                categoryStats.setExpenses(BigDecimal.ZERO);
            } else {
                categoryStats.setExpenses(operation.getAmount().negate());
                categoryStats.setIncomes(BigDecimal.ZERO);
            }
        } else {
            if (operation.getAmount().compareTo(BigDecimal.ZERO) > 0){
                categoryStats.setIncomes(categoryStats.getIncomes().add(operation.getAmount()));
            } else {
                categoryStats.setExpenses(categoryStats.getExpenses().add(operation.getAmount().negate()));
            }
        }

        accountMonthStatisticsRepository.save(totalStats);
        accountMonthStatisticsRepository.save(categoryStats);
    }
}
