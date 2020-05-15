package com.adri.economy.service;

import com.adri.economy.dto.AccountDTO;
import com.adri.economy.dto.FormOperationDTO;
import com.adri.economy.dto.OperationCategoryDTO;
import com.adri.economy.dto.OperationDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.kafka.model.OperationKafka;
import com.adri.economy.model.Account;
import com.adri.economy.model.Operation;
import com.adri.economy.model.OperationCategory;
import com.adri.economy.repository.OperationRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class OperationService {

    private final OperationRepository operationRepository;

    private final OperationCategoryService operationCategoryService;

    private final AccountService accountService;

    private final KafkaTemplate<Long, OperationKafka> operationKafkaTemplate;

    @Value(value = "${kafka.topic.operation}")
    private String operationTopic;

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

        sendOperationToKafka(operation);

        return Mapper.mapToOperationDTO(operation);
    }

    private void sendOperationToKafka(Operation operation){
        OperationKafka message = OperationKafka.builder()
                .id(operation.getId())
                .amount(operation.getAmount())
                .accountId(operation.getAccount().getId())
                .currency(operation.getCurrency())
                .currencyAmount(operation.getCurrencyAmount())
                .timestamp(operation.getDate().toInstant().toString())
                .categoryId(operation.getCategory() != null ? operation.getCategory().getId() : null)
                .build();

        ListenableFuture<SendResult<Long, OperationKafka>> future = operationKafkaTemplate.send(operationTopic, message.getAccountId(), message);

        future.addCallback(new ListenableFutureCallback<SendResult<Long, OperationKafka>>() {

            @Override
            public void onSuccess(SendResult<Long, OperationKafka> result) {
                log.debug("Operation with id {} sent correctly. Offset {}", message.getId(), result.getRecordMetadata()
                        .offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Operation with id {} sent correctly. Due to {}", message.getId(), ex.getMessage());
            }
        });
    }
}
