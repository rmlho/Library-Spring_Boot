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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class BooksService {

    private final IBooksRepository booksRepository;
    private final IAuthorRepository authorRepository;
    private final IPublisherRepository publisherRepository;

    public BooksEntity save(@Valid BooksEntityDTO booksEntityDTO) throws NotFoundException {

        log.info("The process of saving the book begins!");

        AuthorEntity author = authorRepository.findById(booksEntityDTO.getAuthorid())
                .orElseThrow(() -> {
                    log.warn("Author id {} not found.", booksEntityDTO.getAuthorid());
                    return new NotFoundException("Author not found!");
                        });

        PublisherEntity publisher = publisherRepository.findById(booksEntityDTO.getPublisherid())
                .orElseThrow(() -> {
                    log.warn("Publisher id {} not found.", booksEntityDTO.getPublisherid());
                    return new NotFoundException("Publisher not found!");
                });

        BooksEntity book = BooksEntity.builder()
                .name(booksEntityDTO.getName())
                .gender(booksEntityDTO.getGender())
                .author(author)
                .edition(booksEntityDTO.getEdition())
                .publisher(publisher)
                .build();

        BooksEntity savedBook = booksRepository.save(book);
        log.info("Book {} successfully registered.", savedBook.getName());
        return savedBook;
    }

    public Page<BooksEntity> findAll(Pageable pageable) {
        Page<BooksEntity> booksEntityPage = booksRepository.findAll(pageable);
        log.info("Book search completed successfully.");
        return booksEntityPage;
    }

    @Transactional
    public BooksEntity update(Long id, @Valid BooksEntityDTO booksEntity) throws NotFoundException {
        log.info("The process of update the book begins!");

        AuthorEntity author = authorRepository.findById(booksEntity.getAuthorid())
                .orElseThrow(() -> {
                    log.warn("Author id not found in database.");
                    return new NotFoundException("Author id not Found!");
                        });

        PublisherEntity publisher = publisherRepository.findById(booksEntity.getPublisherid())
                .orElseThrow(() -> {
                    log.warn("Publisher id not found in database.");
                    return new NotFoundException("Publisher id not found!");
                        });

        booksRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book id not found in database.");
                    return new NotFoundException("Publisher id not found");
                        });

        BooksEntity book;


        book = BooksEntity.builder()
                .id(id)
                .name(booksEntity.getName())
                .gender(booksEntity.getGender())
                .author(author)
                .edition(booksEntity.getEdition())
                .publisher(publisher)
                .build();


        BooksEntity bookUpdate = booksRepository.save(book);
        log.info("Books update successfully.");
        return bookUpdate;
    }

    public void delete(Long id) throws NotFoundException {
        log.info("Search book by id.");
        BooksEntity book = booksRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Searching by ID did not return a book.");
                    return new NotFoundException("Id not found!");
                        });

        booksRepository.delete(book);
    }

    public Optional<BooksEntity> findById(Long id) {
        Optional<BooksEntity> book = booksRepository.findById(id);
        log.info("The method's optional parameter returned something.");
        return book;
    }

    public List<BooksEntity> findByName(String name) {
        List<BooksEntity> booksEntityList = booksRepository.findAllByNameContaining(name);
        log.info("Book successfully found by name.");
        return booksEntityList;
    }

    public List<BooksEntity> findByGender(String gender) {
        List<BooksEntity> booksEntityList = booksRepository.findByGenderContainingIgnoreCase(gender);
        log.info("Book successfully found by gender.");
        return booksEntityList;
    }

    public List<BooksEntity> orderByRegistrationDate() {
        List<BooksEntity> booksEntityList = booksRepository.findAllByOrderByRegistrationDateDesc();
        log.info("Book successfully found by order registration.");
        return booksEntityList;
    }

}
