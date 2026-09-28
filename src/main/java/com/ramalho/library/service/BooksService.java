package com.ramalho.library.service;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class BooksService {

    private final IBooksRepository booksRepository;
    private final IAuthorRepository authorRepository;
    private final IPublisherRepository publisherRepository;

    public BooksEntity save(@Valid BooksEntityDTO booksEntityDTO) throws NotFoundException {

        AuthorEntity author = authorRepository.findById(booksEntityDTO.getAuthorid())
                .orElseThrow(() -> new NotFoundException("Author not found!"));

        PublisherEntity publisher = publisherRepository.findById(booksEntityDTO.getPublisherid())
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

    @Transactional
    public BooksEntity update(Long id, @Valid BooksEntityDTO booksEntity) throws NotFoundException {
        AuthorEntity author = authorRepository.findById(booksEntity.getAuthorid())
                .orElseThrow(() -> new NotFoundException("Author not found!"));

        PublisherEntity publisher = publisherRepository.findById(booksEntity.getPublisherid())
                .orElseThrow(() -> new NotFoundException("Publisher not found!"));

        booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id not found!"));

        BooksEntity book;


        book = BooksEntity.builder()
                .id(id)
                .name(booksEntity.getName())
                .gender(booksEntity.getGender())
                .author(author)
                .edition(booksEntity.getEdition())
                .publisher(publisher)
                .build();


        return booksRepository.save(book);
    }

    public void delete(Long id) throws NotFoundException {
        BooksEntity book = booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        booksRepository.delete(book);
    }

    public Optional<BooksEntity> findById(Long id) {
        return booksRepository.findById(id);

    }

    public List<BooksEntity> finByName(String name) {
        return booksRepository.findByNameContaining(name);
    }

}
