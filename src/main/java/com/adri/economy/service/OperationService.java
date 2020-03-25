package com.adri.economy.service;

import com.adri.economy.dto.AccountDTO;
import com.adri.economy.dto.FormOperationDTO;
import com.adri.economy.dto.OperationCategoryDTO;
import com.adri.economy.dto.OperationDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.model.Account;
import com.adri.economy.model.Operation;
import com.adri.economy.model.OperationCategory;
import com.adri.economy.repository.OperationRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OperationService {

    private final OperationRepository operationRepository;

    private final OperationCategoryService operationCategoryService;
    private final AccountService accountService;

    public Optional<OperationDTO> findById(long id){
        return operationRepository.findById(id)
                .map(operation -> Optional.of(Mapper.mapToOperationDTO(operation)))
                .orElse(Optional.empty());
    }

    @Transactional
    public OperationDTO createOperation(FormOperationDTO form) {
        AccountDTO accountDTO = accountService.findById(form.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for id: "+form.getAccountId()));
        Account account = new Account();
        account.setId(accountDTO.getId());

        OperationCategory operationCategory = null;
        if (form.getCategoryId() != null){
            OperationCategoryDTO operationCategoryDTO = operationCategoryService.findById(form.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found for id: "+form.getCategoryId()));

            operationCategory = new OperationCategory();
            operationCategory.setId(operationCategoryDTO.getId());
        }

        Operation operation = new Operation();
        operation.setDescription(form.getDescription());
        operation.setInfo(form.getInfo());
        operation.setAmount(form.getAmount());
        operation.setCurrencyAmount(form.getCurrencyAmount());
        operation.setCurrency(form.getCurrency());
        operation.setDate(form.getDate());
        operation.setAccount(account);
        operation.setCategory(operationCategory);
        operation = operationRepository.save(operation);

        return Mapper.mapToOperationDTO(operation);
    }


}
