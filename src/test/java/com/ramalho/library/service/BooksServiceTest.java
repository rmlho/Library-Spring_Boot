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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
                                .birthday(LocalDate.of(2000, 02, 01))
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

    @Test
    void sabeBooks_whenAuthorIdNotFound() {
       BooksEntityDTO booksEntityDTO = createBooksDTO();

       Mockito.when(authorRepository.findById(booksEntityDTO.getAuthorid()))
               .thenReturn(Optional.empty());

       assertThrows(NotFoundException.class, () -> booksService.save(booksEntityDTO));
       Mockito.verify(booksRepository, Mockito.never()).save(any(BooksEntity.class));
    }

    @Test
    void sabeBooks_whenPublisherIdNotFound() {
        BooksEntityDTO booksEntityDTO = createBooksDTO();

        Mockito.when(authorRepository.findById(booksEntityDTO.getAuthorid()))
                .thenReturn(Optional.of(AuthorEntity.builder()
                        .id(1L)
                        .name("Test Author")
                        .birthday(LocalDate.of(2000, 02, 01))
                        .build()));

        Mockito.when(publisherRepository.findById(booksEntityDTO.getPublisherid()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> booksService.save(booksEntityDTO));
        Mockito.verify(booksRepository, Mockito.never()).save(any(BooksEntity.class));
    }

    @Test
    void findAllBooks() {
        BooksEntity booksEntity = createBooks();

        List<BooksEntity> booksEntityList = List.of(booksEntity);
        Page<BooksEntity> booksEntityPage = new PageImpl<>(booksEntityList);

        Mockito.when(booksRepository.findAll(any(Pageable.class)))
                .thenReturn(booksEntityPage);

        Pageable page = PageRequest.of(0, 10);
        Page<BooksEntity> pageBooks = booksService.findAll(page);

        assertNotNull(pageBooks);
        assertEquals(1, pageBooks.getTotalElements());
        assertEquals(1, pageBooks.getTotalPages());
        assertEquals("Test Book", pageBooks.getContent().getFirst().getName());
        assertEquals(0, page.getPageNumber());
        assertEquals(10, page.getPageSize());
    }

    @Test
    void updateBooks_whenIdsFound() throws NotFoundException {
        BooksEntity booksEntity = createBooks();
        BooksEntityDTO booksEntityDTO = createBooksDTO();

        Mockito.when(authorRepository.findById(booksEntityDTO.getAuthorid()))
                .thenReturn(Optional.of(AuthorEntity.builder()
                        .id(1L)
                        .name("Test Author")
                        .birthday(LocalDate.of(2000, 02, 01))
                        .build()));

        Mockito.when(publisherRepository.findById(1L))
                .thenReturn(Optional.of(PublisherEntity.builder()
                        .id(1L)
                        .name("Test Publisher")
                        .location("Test Location Publisher")
                        .build()));

        Mockito.when(booksRepository.findById(booksEntity.getId()))
                .thenReturn(Optional.of(booksEntity));

        Mockito.when(booksRepository.save(any(BooksEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BooksEntity books = booksService.update(1L, booksEntityDTO);

        assertNotNull(books);
        assertEquals(1L, books.getId());
        assertEquals(1L, books.getAuthor().getId());
        assertEquals(1L, books.getPublisher().getId());
    }
}