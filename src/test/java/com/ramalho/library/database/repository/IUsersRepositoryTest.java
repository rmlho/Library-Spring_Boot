package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.UsersEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class IUsersRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    IUsersRepository usersRepository;

    private UsersEntity createUsers(String email, String password) {
        UsersEntity users = UsersEntity.builder()
                .email(email)
                .password(password)
                .build();

        this.entityManager.persist(users);
        return users;
    }

    @Test
    @DisplayName("Should get email user successfully database.")
    void findByEmail() {
        createUsers("Test@.com", "1234");

        Optional<UsersEntity> result = usersRepository.findByEmail("Test@.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("Test@.com");
    }

    @Test
    @DisplayName("Should get email user empty database.")
    void findByEmailIsEmpty() {
        createUsers("Test@.com", "1234");

        Optional<UsersEntity> result = usersRepository.findByEmail("Null@.com");

        assertThat(result).isNotNull();
        assertThat(result.isEmpty());
    }
}