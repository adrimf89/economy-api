package com.adri.economy.service;

import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.kafka.model.OperationKafka;
import com.adri.economy.model.AccountMonthStatistics;
import com.adri.economy.model.Operation;
import com.adri.economy.repository.AccountMonthStatisticsRepository;
import com.adri.economy.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountMonthStatisticsService {

    private final OperationRepository operationRepository;
    private final AccountMonthStatisticsRepository accountMonthStatisticsRepository;

    @KafkaListener(topics = "${kafka.topic.operation}", groupId = "${kafka.username}-${kafka.group.stats}", containerFactory = "statsKafkaListenerContainerFactory")
    public void balanceListener(OperationKafka operation, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Long key) {
        log.debug("Statistics calculation - Account: {}, operation id: {}, Time: {}",
                key,
                operation.getId(),
                operation.getTimestamp());

        try {
            updateAccountStatistics(operation.getId());
        } catch (Exception e) {
            log.error("Error calculating statistics for operation {}", operation.getId(), e);
        }
    }

    @Transactional
    public void updateAccountStatistics(long operationId){
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new ResourceNotFoundException("Operation not found for id: "+operationId));

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
