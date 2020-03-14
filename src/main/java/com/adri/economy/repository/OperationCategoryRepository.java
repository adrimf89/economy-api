package com.adri.economy.repository;

import com.adri.economy.model.OperationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationCategoryRepository extends JpaRepository<OperationCategory, Long> {

    boolean existsByName(String name);
}
