package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    void findAllAuthor() {
        AuthorEntity authorEntity = createAuthor();

        List<AuthorEntity> authorEntityList = List.of(authorEntity);

        Page<AuthorEntity> authorEntityPage = new PageImpl<>(authorEntityList);

        Mockito.when(authorRepository.findAll(any(Pageable.class)))
                .thenReturn(authorEntityPage);

        Pageable page = PageRequest.of(0, 10);

        Page<AuthorEntity> pageAuthor = authorService.findAll(page);

        assertNotNull(pageAuthor);
        assertEquals(1, pageAuthor.getTotalElements());
        assertEquals(1, pageAuthor.getTotalPages());
        assertEquals("Test Author", pageAuthor.getContent().getFirst().getName());
        assertEquals(0, page.getPageNumber());
        assertEquals(10, page.getPageSize());
    }

}