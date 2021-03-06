package com.adri.economy.controller;

import com.adri.economy.dto.AccountDTO;
import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.FormAccountDTO;
import com.adri.economy.dto.OperationDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.service.AccountService;
import com.adri.economy.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountController extends AbstractController{

    private final AccountService accountService;
    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findAccount(@PathVariable Long id){
        AccountDTO accountDTO = accountService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id "+id));

        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping
    public ResponseEntity<PagedModel<AccountDTO>> findAll(
            Pageable pageable, PagedResourcesAssembler assembler){

        return ResponseEntity.ok(assembler.toModel(accountService.findAll(pageable), linkTo(AccountController.class).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody FormAccountDTO form){
        AppUserDTO appUserDTO = appUserService.findByUsername(this.getLoggedUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find logged user "));
        form.setOwnerId(appUserDTO.getId());

        AccountDTO accountDTO = accountService.createAccount(form);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(accountDTO.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/operations")
    public ResponseEntity<PagedModel<OperationDTO>> findOperations(
            @PathVariable Long id, Pageable pageable, PagedResourcesAssembler assembler){

        return ResponseEntity.ok(assembler.toModel(accountService.findOperationByAccountId(id, pageable), linkTo(AccountController.class).withSelfRel()));
    }
}
