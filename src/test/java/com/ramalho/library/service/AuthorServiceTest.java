package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private IAuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorEntity createAuthor() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Test Author")
                .birthday(LocalDate.ofEpochDay(01/03/2000))
                .build();
    }

    private AuthorEntityDTO createAuthorDTO() {
        return AuthorEntityDTO.builder()
                .name("Test author")
                .birthday(LocalDate.ofEpochDay(01/03/2000))
                .build();
    }

    @Test
    void saveAuthor() {
        AuthorEntityDTO authorEntityDTO =  createAuthorDTO();
        AuthorEntity authorEntity = createAuthor();

        Mockito.when(authorRepository.save(any(AuthorEntity.class)))
                .thenReturn(authorEntity);

        AuthorEntity author = authorService.save(authorEntityDTO);

        assertNotNull(author);
        assertEquals(1L, author.getId());
        assertEquals("Test Author", author.getName());
    }

}