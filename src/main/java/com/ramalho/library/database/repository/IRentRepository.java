package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRentRepository extends JpaRepository<RentEntity, Long> {

}
