package com.adri.economy.controller;

import com.adri.economy.dto.FormRoleDTO;
import com.adri.economy.dto.RoleDTO;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.service.RoleService;
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
@RequestMapping("/api/v1/roles")
@Slf4j
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findRole(@PathVariable Long roleId){
        RoleDTO roleDTO = roleService.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for this id "+roleId));

        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping
    public ResponseEntity<PagedModel<RoleDTO>> findAll(
            Pageable pageable, PagedResourcesAssembler assembler){

        return ResponseEntity.ok(assembler.toModel(roleService.findAll(pageable), linkTo(RoleController.class).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody FormRoleDTO form){
        RoleDTO roleDTO = roleService.createRole(form.getRoleName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(roleDTO.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    //@PutMapping("/{id}")
    //public ResponseEntity<AppUserDTO> update(@PathVariable String id, @Valid @RequestBody FormRoleDTO form){
    //    AppUserDTO appUser = roleService. updateUser(UUID.fromString(id), form);
    //
    //    return ResponseEntity.ok(appUser);
    //}
}
