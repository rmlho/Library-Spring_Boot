package com.ramalho.library.service;

import com.ramalho.library.database.repository.IBooksRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private IBooksRepository booksRepository;

    @InjectMocks
    private BooksService booksService;
}