package com.adri.economy.repository;

import com.adri.economy.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    Page<Operation> findByAccountIdOrderByDateDesc(long id, Pageable pageable);

    List<Operation> findByAccountIdAndDateGreaterThanEqualOrderByDateAsc(long id, Date date);
}
