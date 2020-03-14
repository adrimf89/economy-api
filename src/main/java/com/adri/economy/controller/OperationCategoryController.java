package com.adri.economy.controller;

import com.adri.economy.dto.FormOperationCategoryDTO;
import com.adri.economy.dto.OperationCategoryDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.service.OperationCategoryService;
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
@RequestMapping("/api/v1/operation-categories")
@Slf4j
@RequiredArgsConstructor
public class OperationCategoryController {

    private final OperationCategoryService operationCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<OperationCategoryDTO> findOperationCategory(@PathVariable Long id){
        OperationCategoryDTO operationCategoryDTO = operationCategoryService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation category not found for this id "+id));

        return ResponseEntity.ok(operationCategoryDTO);
    }

    @GetMapping
    public ResponseEntity<PagedModel<OperationCategoryDTO>> findAll(
            Pageable pageable, PagedResourcesAssembler assembler){

        return ResponseEntity.ok(assembler.toModel(operationCategoryService.findAll(pageable), linkTo(OperationCategoryController.class).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<OperationCategoryDTO> createOperationCategory(@Valid @RequestBody FormOperationCategoryDTO form){
        OperationCategoryDTO accountDTO = operationCategoryService.createOperationCategory(form);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(accountDTO.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
