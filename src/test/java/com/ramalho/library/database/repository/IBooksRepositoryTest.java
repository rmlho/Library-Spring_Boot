package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.PublisherEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class IBooksRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    IBooksRepository booksRepository;

    private AuthorEntity createAuthor(String name, LocalDate birthday) {
        AuthorEntity author = AuthorEntity.builder()
                .name(name)
                .birthday(birthday)
                .build();

        this.entityManager.persist(author);
        return author;
    }

    private PublisherEntity createPublisher(String name, String location) {
        PublisherEntity publisher = PublisherEntity.builder()
                .name(name)
                .location(location)
                .build();

        this.entityManager.persist(publisher);
        return publisher;
    }

    private BooksEntity createBook(String name, String gender, String edition, AuthorEntity author, PublisherEntity publisher) {
        BooksEntity book = BooksEntity.builder()
                .name(name)
                .gender(gender)
                .author(author)
                .edition(edition)
                .publisher(publisher)
                .build();

        this.entityManager.persist(book);
        return book;
    }

    @Test
    @DisplayName("Should get all name books successfully database.")
    void findAllByNameContaining() {
        AuthorEntity author = createAuthor("Name author", LocalDate.of(2000, 01, 01));
        PublisherEntity publisher = createPublisher("Name Publisher", "Location Publisher");
        createBook("Lord of the ring", "Fantasy", "Deluxe", author, publisher);
        createBook("Cyberpunk 2077", "Dystopian", "Basic", author, publisher);

        List<BooksEntity> result = booksRepository.findAllByNameContaining("Lord");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Lord of the ring");
    }

    @Test
    @DisplayName("Should get all name books is empty database.")
    void findAllByNameContainingIsEmpty() {
        AuthorEntity author = createAuthor("Name author", LocalDate.of(2000, 01, 01));
        PublisherEntity publisher = createPublisher("Name Publisher", "Location Publisher");
        createBook("Lord of the ring", "Fantasy", "Deluxe", author, publisher);
        createBook("Cyberpunk 2077", "Dystopian", "Basic", author, publisher);

        List<BooksEntity> result = booksRepository.findAllByNameContaining("NULL");

        assertThat(result.isEmpty());
    }

    @Test
    @DisplayName("Should get all gender books successfully database.")
    void findByGenderContainingIgnoreCase() {
        AuthorEntity author = createAuthor("Name author", LocalDate.of(2000, 01, 01));
        PublisherEntity publisher = createPublisher("Name Publisher", "Location Publisher");
        createBook("Lord of the ring", "Fantasy", "Deluxe", author, publisher);
        createBook("Cyberpunk 2077", "Dystopian", "Basic", author, publisher);

        List<BooksEntity> result = booksRepository.findByGenderContainingIgnoreCase("Fantasy");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getGender()).isEqualTo("Fantasy");
    }

    @Test
    @DisplayName("Should get all gender books empty database.")
    void findByGenderContainingIgnoreCaseIsEmpty() {
        AuthorEntity author = createAuthor("Name author", LocalDate.of(2000, 01, 01));
        PublisherEntity publisher = createPublisher("Name Publisher", "Location Publisher");
        createBook("Lord of the ring", "Fantasy", "Deluxe", author, publisher);
        createBook("Cyberpunk 2077", "Dystopian", "Basic", author, publisher);

        List<BooksEntity> result = booksRepository.findByGenderContainingIgnoreCase("NULL");

        assertThat(result.isEmpty());
    }

    @Test
    @DisplayName("Should get books order by registration date successfully database.")
    void findAllByOrderByRegistrationDateDesc() {
        AuthorEntity author = createAuthor("Name author", LocalDate.of(2000, 01, 01));
        PublisherEntity publisher = createPublisher("Name Publisher", "Location Publisher");
        createBook("Lord of the ring", "Fantasy", "Deluxe", author, publisher);
        createBook("Cyberpunk 2077", "Dystopian", "Basic", author, publisher);

        List<BooksEntity> result = booksRepository.findAllByOrderByRegistrationDateDesc();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Cyberpunk 2077");
    }
}