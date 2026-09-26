package com.ramalho.library.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.database.repository.IBooksRepository;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.BooksEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final IBooksRepository booksRepository;
    private final IAuthorRepository authorRepository;
    private final IPublisherRepository publisherRepository;

    public BooksEntity save(@Valid BooksEntityDTO booksEntityDTO) throws NotFoundException {

        AuthorEntity author = authorRepository.findById(booksEntityDTO.getAuthor().getId())
                .orElseThrow(() -> new NotFoundException("Author not found!"));

        PublisherEntity publisher = publisherRepository.findById(booksEntityDTO.getPublisher().getId())
                .orElseThrow(() -> new NotFoundException("Publisher not found!"));

        BooksEntity book = BooksEntity.builder()
                .name(booksEntityDTO.getName())
                .gender(booksEntityDTO.getGender())
                .author(author)
                .edition(booksEntityDTO.getEdition())
                .publisher(publisher)
                .build();

        return booksRepository.save(book);
    }

    public List<BooksEntity> findAll() {
        return booksRepository.findAll();
    }




}
