package com.adri.economy.util;

import com.adri.economy.dto.*;
import com.adri.economy.model.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mapper {

    public static AppUserDTO mapToAppUserDTO(AppUser appUser){
        return AppUserDTO.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .creationDate(appUser.getCreationDate())
                .deletedDate(appUser.getDeletedDate())
                .roles(CollectionUtils.isEmpty(appUser.getRoles()) ?
                        new ArrayList<>() :
                        appUser.getRoles().stream()
                            .map(role -> mapToRoleDTO(role))
                            .collect(Collectors.toList()))
                .build();
    }

    public static Role mapToRole(RoleDTO roleDTO){
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setRole(roleDTO.getRole());
        return role;
    }

    public static RoleDTO mapToRoleDTO(Role role){
        return RoleDTO.builder()
                .id(role.getId())
                .role(role.getRole())
                .build();
    }

    public static AccountDTO mapToAccountDTO(Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .iban(account.getIban())
                .currency(account.getCurrency())
                .description(account.getDescription())
                .creationDate(account.getCreationDate())
                .deletedDate(account.getDeletedDate())
                .owner(account.getOwner() == null ? null : mapToAppUserDTO(account.getOwner()))
                .build();
    }

    public static AccountDTO mapToAccountDTO(Account account, AccountBalance accountBalance){
        AccountDTO accountDTO = mapToAccountDTO(account);

        accountDTO.setCurrentBalance(AccountBalanceDTO.builder()
                .date(accountBalance.getDate())
                .balance(accountBalance.getBalance())
                .build());

        return accountDTO;
    }

    public static OperationDTO mapToOperationDTO(Operation operation){
        return OperationDTO.builder()
                .id(operation.getId())
                .description(operation.getDescription())
                .info(operation.getInfo())
                .date(operation.getDate())
                .amount(operation.getAmount())
                .currencyAmount(operation.getCurrencyAmount())
                .currency(operation.getCurrency())
                .accountId(operation.getAccount().getId())
                .category(operation.getCategory() == null ? null : mapToOperationCategoryDTO(operation.getCategory()))
                .build();
    }

    public static OperationCategoryDTO mapToOperationCategoryDTO(OperationCategory operationCategory){
        return OperationCategoryDTO.builder()
                .id(operationCategory.getId())
                .name(operationCategory.getName())
                .description(operationCategory.getDescription())
                .creationDate(operationCategory.getCreationDate())
                .deletedDate(operationCategory.getDeletedDate())
                .icon(operationCategory.getIcon())
                .build();
    }
}
