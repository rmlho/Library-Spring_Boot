package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBooksRepository extends JpaRepository<BooksEntity, Long> {
    List<BooksEntity> findAllByNameContaining(String name);
    List<BooksEntity> findByGenderContainingIgnoreCase(String gender);
    List<BooksEntity> findAllByOrderByRegistrationDateDesc();

    boolean existsByNameIgnoreCase(String name);
}
