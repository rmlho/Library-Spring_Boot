package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBooksRepository extends JpaRepository<BooksEntity, Long> {
}
