package com.adri.economy.service;

import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.RoleDTO;
import com.adri.economy.dto.FormUserDTO;
import com.adri.economy.exception.ResourceAlreadyExistsException;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.model.AppUser;
import com.adri.economy.model.Role;
import com.adri.economy.repository.AppUserRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<AppUserDTO> findByUsername(String username){
        return appUserRepository.findByUsername(username)
                .map(appUser -> Optional.of(Mapper.mapToAppUserDTO(appUser)))
                .orElse(Optional.empty());
    }

    public Page<AppUserDTO> findAll(Pageable pageable){
        return appUserRepository.findAll(pageable)
                .map(user -> Mapper.mapToAppUserDTO(user));
    }

    public AppUserDTO createUser(FormUserDTO signUpUser) {
        if (appUserRepository.existsByUsername(signUpUser.getUserName())){
            throw new ResourceAlreadyExistsException("Username already exists: "+signUpUser.getUserName());
        }

        Role role = null;
        Optional<RoleDTO> roleDTO = roleService.findByName(signUpUser.getRole());
        if (roleDTO.isPresent()){
            role = Mapper.mapToRole(roleDTO.get());
        } else {
            role = Mapper.mapToRole(roleService.createRole(signUpUser.getRole()));
        }

        AppUser user = new AppUser();
        user.setId(UUID.randomUUID());
        user.setUsername(signUpUser.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(signUpUser.getPassword()));
        user.setFirstName(signUpUser.getFirstName());
        user.setLastName(signUpUser.getLastName());
        user.setCreationDate(new Date());
        user.setRoles(Arrays.asList(role));

        return Mapper.mapToAppUserDTO(appUserRepository.save(user));
    }

    public AppUserDTO updateUser(UUID userId, FormUserDTO formUser) {
        if (!appUserRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found for id: "+userId);
        }

        Role role = null;
        Optional<RoleDTO> roleDTO = roleService.findByName(formUser.getRole());
        if (roleDTO.isPresent()){
            role = Mapper.mapToRole(roleDTO.get());
        } else {
            role = Mapper.mapToRole(roleService.createRole(formUser.getRole()));
        }

        AppUser appUser = appUserRepository.getOne(userId);

        appUser.setUsername(formUser.getUserName());
        appUser.setPassword(bCryptPasswordEncoder.encode(formUser.getPassword()));
        appUser.setFirstName(formUser.getFirstName());
        appUser.setLastName(formUser.getLastName());
        appUser.setRoles(Arrays.asList(role));

        return Mapper.mapToAppUserDTO(appUserRepository.save(appUser));
    }
}
