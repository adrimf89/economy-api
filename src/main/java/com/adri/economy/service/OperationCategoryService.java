package com.adri.economy.service;

import com.adri.economy.dto.FormOperationCategoryDTO;
import com.adri.economy.dto.OperationCategoryDTO;
import com.adri.economy.exception.ResourceAlreadyExistsException;
import com.adri.economy.model.OperationCategory;
import com.adri.economy.repository.OperationCategoryRepository;
import com.adri.economy.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OperationCategoryService {

    private final OperationCategoryRepository operationCategoryRepository;

    public Optional<OperationCategoryDTO> findById(long id){
        return operationCategoryRepository.findById(id)
                .map(cat -> Optional.of(Mapper.mapToOperationCategoryDTO(cat)))
                .orElse(Optional.empty());
    }

    public Page<OperationCategoryDTO> findAll(Pageable pageable){
        return operationCategoryRepository.findAll(pageable)
                .map(cat -> Mapper.mapToOperationCategoryDTO(cat));
    }

    @Transactional
    public OperationCategoryDTO createOperationCategory(FormOperationCategoryDTO form) {
        if (operationCategoryRepository.existsByName(form.getName())){
            throw new ResourceAlreadyExistsException("Category already exists for given name: "+form.getName());
        }

        OperationCategory category = new OperationCategory();
        category.setName(form.getName());
        category.setDescription(form.getDescription());
        category.setIcon(form.getIcon());
        category.setCreationDate(new Date());

        return Mapper.mapToOperationCategoryDTO(operationCategoryRepository.save(category));
    }
}
