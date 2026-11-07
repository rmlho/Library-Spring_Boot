package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.PublisherEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class IPublisherRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    IPublisherRepository publisherRepository;

    private PublisherEntity createPublisher(String name, String location) {
        PublisherEntity publisher = PublisherEntity.builder()
                .name(name)
                .location(location)
                .build();

        this.entityManager.persist(publisher);
        return publisher;
    }

    @Test
    @DisplayName("Should get Publisher name successfully database.")
    void findAllByNameContaining() {
        createPublisher("Name 1", "Location 1");
        createPublisher("Name 2", "Location 2");

        List<PublisherEntity> result = publisherRepository.findAllByNameContaining("Name");

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Name 1");
    }

    @Test
    @DisplayName("Should get Publisher name is empty database.")
    void findAllByNameContainingIsEmpty() {
        createPublisher("Name 1", "Location 1");

        List<PublisherEntity> result = publisherRepository.findAllByNameContaining("NULL");

        assertThat(result.isEmpty());
    }
}