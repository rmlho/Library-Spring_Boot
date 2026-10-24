package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IBooksRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
import com.ramalho.library.dto.BooksEntityDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private IBooksRepository booksRepository;

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
                        .birthday(LocalDate.ofEpochDay(01/02/2000))
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
}