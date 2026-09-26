package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
