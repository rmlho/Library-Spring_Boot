package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRolesRepository extends JpaRepository<UsersEntity, Long> {
    Optional<UsersEntity> findByEmail(String email);
}
