package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.BooksEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBooksRepository extends JpaRepository<BooksEntity, Long> {
    List<BooksEntity> findAllByNameContaining(String name);
    List<BooksEntity> findByGenderContainingIgnoreCase(String gender);
    List<BooksEntity> findAllByOrderByRegistrationDateDesc();

    boolean existsByNameIgnoreCaseAndEditionIgnoreCase(String name, String edition);

    Optional<BooksEntity> findByName(@NotBlank String name);
}
