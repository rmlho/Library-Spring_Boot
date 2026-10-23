package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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

}