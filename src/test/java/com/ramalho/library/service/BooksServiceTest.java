package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.database.repository.IBooksRepository;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.BooksEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private IBooksRepository booksRepository;

    @Mock
    private IAuthorRepository authorRepository;

    @Mock
    private IPublisherRepository publisherRepository;

    @InjectMocks
    private BooksService booksService;

    private BooksEntity createBooks() {
        return BooksEntity.builder()
                .id(1L)
                .name("Test Book")
                .gender("Test Gender")
                //Create Author
                .author(AuthorEntity.builder()
                        .id(1L)
                        .name("Test Author")
                        .birthday(LocalDate.of(200, 02, 01))
                        .build())
                .edition("Test Edition")
                //Create Publisher
                .publisher(PublisherEntity.builder()
                        .id(1L)
                        .name("Test Publisher")
                        .location("Test Location Publisher")
                        .build())
                .registrationDate(LocalDateTime.of(200, 02, 01, 23, 10, 20))
                .build();
    }

    private BooksEntityDTO createBooksDTO() {
        return BooksEntityDTO.builder()
                .name("Test Book")
                .gender("Test Gender")
                .authorid(1L)
                .edition("Test Edition")
                .publisherid(1L)
                .build();
    }

    @Test
    void saveBooks() throws NotFoundException {
        BooksEntity booksEntity = createBooks();
        BooksEntityDTO booksEntityDTO = createBooksDTO();

        Mockito.when(authorRepository.findById(1L))
                        .thenReturn(Optional.of(AuthorEntity.builder()
                                .id(1L)
                                .name("Test Author")
                                .birthday(LocalDate.of(200, 02, 01))
                                .build()));

        Mockito.when(publisherRepository.findById(1L))
                .thenReturn(Optional.of(PublisherEntity.builder()
                        .id(1L)
                        .name("Test Publisher")
                        .location("Test Location Publisher")
                        .build()));

        Mockito.when(booksRepository.save(any(BooksEntity.class)))
                .thenReturn(booksEntity);

        BooksEntity books = booksService.save(booksEntityDTO);

        assertNotNull(books);
        assertEquals(1L, books.getId());
        assertEquals("Test Book", books.getName());
    }
}