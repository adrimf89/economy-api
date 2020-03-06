package com.adri.economy.service;

import com.adri.economy.dto.RoleDTO;
import com.adri.economy.exception.ResourceAlreadyExistsException;
import com.adri.economy.model.Role;
import com.adri.economy.repository.RoleRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<RoleDTO> findById(UUID id){
        return roleRepository.findById(id)
                .map(role -> Optional.of(Mapper.mapToRoleDTO(role)))
                .orElse(Optional.empty());
    }

    public Optional<RoleDTO> findByName(String roleName){
        return roleRepository.findByRole(roleName)
                .map(role -> Optional.of(Mapper.mapToRoleDTO(role)))
                .orElse(Optional.empty());
    }

    public Page<RoleDTO> findAll(Pageable pageable){
        return roleRepository.findAll(pageable)
                .map(role -> Mapper.mapToRoleDTO(role));
    }

    public RoleDTO createRole(String roleName) {
        if (roleRepository.existsByRole(roleName)){
            throw new ResourceAlreadyExistsException("Role already exists: "+roleName);
        }

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setRole(roleName);
        role.setCreationDate(new Date());

        return Mapper.mapToRoleDTO(roleRepository.save(role));
    }
}
