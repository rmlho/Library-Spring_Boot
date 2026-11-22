package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.LateFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILateFeeRepository extends JpaRepository<LateFeeEntity, Long> {
    Optional<LateFeeEntity> findByRentId(Long rentId);
}
