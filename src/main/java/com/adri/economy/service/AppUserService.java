package com.adri.economy.service;

import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.RoleDTO;
import com.adri.economy.dto.SignUpUserDTO;
import com.adri.economy.exception.ResourceAlreadyExistsException;
import com.adri.economy.exception.ResourceNotFoundException;
import com.adri.economy.model.AppUser;
import com.adri.economy.model.Role;
import com.adri.economy.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserDTO findByUsername(String username){
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username "+username));

        return mapToDTO(appUser);
    }

    public Page<AppUserDTO> findAll(Pageable pageable){
        Page<AppUser> page = appUserRepository.findAll(pageable);

        return page.map(user -> mapToDTO(user));
    }

    public AppUserDTO createUser(SignUpUserDTO signUpUser) {
        if (appUserRepository.existsByUsername(signUpUser.getUserName())){
            throw new ResourceAlreadyExistsException("Username already exists: "+signUpUser.getUserName());
        }

        //UUID roleId = UUID.fromString(signUpUser.getRole());
        //Role userRole = roleDao.findById(roleId);
        //if (!userRole.isPresent()){
        //    throw new ResourceNotFoundException("Role: "+roleId);
        //}

        AppUser user = new AppUser();
        user.setId(UUID.randomUUID());
        user.setUsername(signUpUser.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(signUpUser.getPassword()));
        user.setFirstName(signUpUser.getFirstName());
        user.setLastName(signUpUser.getLastName());
        user.setCreationDate(new Date());
        //user.setRoles(Arrays.asList(userRole.get()));

        return mapToDTO(appUserRepository.save(user));
    }

    private AppUserDTO mapToDTO(AppUser appUser){
        return AppUserDTO.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .creationDate(appUser.getCreationDate())
                .deletedDate(appUser.getDeletedDate())
                .roles(appUser.getRoles().stream()
                            .map(role -> mapToRoleDTO(role))
                            .collect(Collectors.toList()))
                .build();
    }

    private RoleDTO mapToRoleDTO(Role role){
        return RoleDTO.builder()
                .id(role.getId())
                .role(role.getRole())
                .build();
    }
}
