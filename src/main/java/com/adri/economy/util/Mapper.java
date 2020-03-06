package com.adri.economy.util;

import com.adri.economy.dto.AppUserDTO;
import com.adri.economy.dto.RoleDTO;
import com.adri.economy.model.AppUser;
import com.adri.economy.model.Role;
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
}
