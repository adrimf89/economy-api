package com.adri.economy.controller;

import com.adri.economy.dto.FormOperationDTO;
import com.adri.economy.dto.OperationDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.service.AMQPService;
import com.adri.economy.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/operations")
@Slf4j
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;
    private final AMQPService amqpService;

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> findOperation(@PathVariable Long id){
        OperationDTO operationDTO = operationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation not found for this id "+id));

        return ResponseEntity.ok(operationDTO);
    }

    @PostMapping
    public ResponseEntity<OperationDTO> createOperation(@Valid @RequestBody FormOperationDTO form){
        OperationDTO operationDTO = operationService.createOperation(form);

        amqpService.sendOperationCreation(operationDTO.getId(), operationDTO.getAccountId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(operationDTO.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
