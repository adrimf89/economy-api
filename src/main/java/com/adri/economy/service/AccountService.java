package com.adri.economy.service;

import com.adri.economy.dto.AccountDTO;
import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.FormAccountDTO;
import com.adri.economy.dto.OperationDTO;
import com.adri.economy.exception.ResourceAlreadyExistsException;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.model.Account;
import com.adri.economy.model.AccountBalance;
import com.adri.economy.model.AppUser;
import com.adri.economy.repository.AccountBalanceRepository;
import com.adri.economy.repository.AccountRepository;
import com.adri.economy.repository.OperationRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final OperationRepository operationRepository;
    private final AppUserService appUserService;

    public Optional<AccountDTO> findById(long id){
        return accountRepository.findById(id)
                .map(account -> Optional.of(Mapper.mapToAccountDTO(account, accountBalanceRepository.findFirstByAccountIdOrderByDateDesc(id))))
                .orElse(Optional.empty());
    }

    public Page<AccountDTO> findAll(Pageable pageable){
        return accountRepository.findAll(pageable)
                .map(account -> Mapper.mapToAccountDTO(account, accountBalanceRepository.findFirstByAccountIdOrderByDateDesc(account.getId())));
    }

    @Transactional
    public AccountDTO createAccount(FormAccountDTO form) {
        if (accountRepository.existsByIban(form.getIban())){
            throw new ResourceAlreadyExistsException("Account already exists for given IBAN: "+form.getIban());
        }

        AppUserDTO appUserDTO = appUserService.findById(form.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: "+form.getOwnerId()));
        AppUser owner = new AppUser();
        owner.setId(appUserDTO.getId());

        Account account = new Account();
        account.setIban(form.getIban());
        account.setCurrency(form.getCurrency());
        account.setDescription(form.getDescription());
        account.setCreationDate(new Date());
        account.setOwner(owner);

        account = accountRepository.save(account);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAccount(account);
        accountBalance.setDate(new Date());
        accountBalance.setBalance(form.getInitialBalance() == null ? BigDecimal.ZERO : form.getInitialBalance());
        accountBalanceRepository.save(accountBalance);


        return Mapper.mapToAccountDTO(account, accountBalance);
    }

    public Page<OperationDTO> findOperationByAccountId(long accountId, Pageable pageable){
        return operationRepository.findByAccountIdOrderByDateDesc(accountId, pageable)
                .map(operation -> Mapper.mapToOperationDTO(operation));
    }
}
