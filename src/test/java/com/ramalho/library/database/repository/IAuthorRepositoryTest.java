package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.AuthorEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class IAuthorRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    IAuthorRepository authorRepository;

    private AuthorEntity createAuthor(String name, LocalDate birthday) {
        AuthorEntity author = AuthorEntity.builder()
                .name(name)
                .birthday(birthday)
                .build();

        this.entityManager.persist(author);
        return author;
    }

    @Test
    @DisplayName("Should get author name successfully database.")
    void findAllByNameContaining() {

        createAuthor("Machado de Assis", LocalDate.of(2001, 01, 01));
        createAuthor("Clarisse Lispector", LocalDate.of(2002, 02, 02));

        List<AuthorEntity> result = authorRepository.findAllByNameContaining("Machado");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Machado de Assis");
    }

    @Test
    @DisplayName("Should get author name is empty database.")
    void findAllByNameContainingIsEmpty() {

        createAuthor("Machado de Assis", LocalDate.of(2001, 01, 01));
        createAuthor("Clarisse Lispector", LocalDate.of(2002, 02, 02));

        List<AuthorEntity> result = authorRepository.findAllByNameContaining("NULL");

        assertThat(result.isEmpty());
    }
}