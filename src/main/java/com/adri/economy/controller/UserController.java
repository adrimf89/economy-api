package com.adri.economy.controller;

import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.FormUserDTO;
import com.adri.economy.exception.ResourceNotFoundException;
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
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final AppUserService appUserService;

    @GetMapping("/{username}")
    public ResponseEntity<AppUserDTO> findUser(@PathVariable String username){
        AppUserDTO appUserDTO = appUserService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username "+username));

        return ResponseEntity.ok(appUserDTO);
    }

    @GetMapping
    public ResponseEntity<PagedModel<AppUserDTO>> findAll(
            Pageable pageable, PagedResourcesAssembler assembler){

        return ResponseEntity.ok(assembler.toModel(appUserService.findAll(pageable), linkTo(UserController.class).withSelfRel()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AppUserDTO> signUp(@Valid @RequestBody FormUserDTO signUpUser){
        AppUserDTO appUser = appUserService.createUser(signUpUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userName}")
                .buildAndExpand(appUser.getUsername()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> update(@PathVariable Long id, @Valid @RequestBody FormUserDTO form){
        AppUserDTO appUser = appUserService.updateUser(id, form);

        return ResponseEntity.ok(appUser);
    }
}
