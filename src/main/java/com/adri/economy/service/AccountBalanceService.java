package com.adri.economy.service;

import com.adri.economy.model.Account;
import com.adri.economy.model.AccountBalance;
import com.adri.economy.model.Operation;
import com.adri.economy.repository.AccountBalanceRepository;
import com.adri.economy.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class AccountBalanceService {

    private final OperationRepository operationRepository;
    private final AccountBalanceRepository  accountBalanceRepository;

    @Transactional
    public void updateAccountBalance(Operation operation){
        final Account account = operation.getAccount();
        List<Operation> operations = operationRepository.findByAccountIdAndDateGreaterThanEqualOrderByDateAsc(account.getId(), operation.getDate());

        Map<Date, List<Operation>> operationsByDate = operations.stream()
                .collect(groupingBy(op -> {
                    Date date = op.getDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                }));

        List<AccountBalance> balances = operationsByDate.entrySet().stream()
                .map(entry -> {
                    BigDecimal totalAmount = entry.getValue().stream()
                            .map(Operation::getAmount)
                            .reduce(BigDecimal.ZERO, (amount1, amount2) -> amount1.add(amount2));

                    AccountBalance balance = new AccountBalance();
                    balance.setAccount(account);
                    balance.setDate(entry.getKey());
                    balance.setBalance(totalAmount);

                    return balance;
                })
                .sorted(Comparator.comparing(AccountBalance::getDate))
                .collect(Collectors.toList());

        Calendar cal = Calendar.getInstance();
        cal.setTime(operation.getDate());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date operationDate = cal.getTime();

        Optional<AccountBalance> lastAccountBalance = accountBalanceRepository.findFirstByAccountIdAndDateLessThanOrderByDateDesc(account.getId(), operationDate);
        AtomicReference<BigDecimal> previousDayBalance = new AtomicReference<>(BigDecimal.ZERO);
        if (lastAccountBalance.isPresent()){
            previousDayBalance.set(lastAccountBalance.get().getBalance());
        }

        balances.forEach(balance -> {
            Optional<AccountBalance> result = accountBalanceRepository.findByAccountIdAndDate(balance.getAccount().getId(), balance.getDate());
            if (result.isPresent()){
                AccountBalance accountBalance = result.get();
                accountBalance.setBalance(previousDayBalance.get().add(balance.getBalance()));
                accountBalanceRepository.save(accountBalance);
                previousDayBalance.set(accountBalance.getBalance());
            } else {
                balance.setBalance(previousDayBalance.get().add(balance.getBalance()));
                accountBalanceRepository.save(balance);
                previousDayBalance.set(balance.getBalance());
            }
        });
    }
}
