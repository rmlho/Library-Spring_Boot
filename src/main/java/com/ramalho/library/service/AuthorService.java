package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
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
public class AuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorEntity save(@Valid AuthorEntityDTO authorEntity) {
        log.info("The process of saving the author begins!");

        AuthorEntity author = AuthorEntity.builder()
                .name(authorEntity.getName())
                .birthday(authorEntity.getBirthday())
                .build();

        AuthorEntity authorSave = authorRepository.save(author);
        log.info("Author {} successfully registered.", authorSave.getName());
        return authorSave;
    }

    public Page<AuthorEntity> findAll(Pageable pageable) {
        log.info("The process of find all the author begins!");

        Page<AuthorEntity> authorFindAll = authorRepository.findAll(pageable);
        log.info("Author find all successfully.");
        return authorFindAll;
    }

    @Transactional
    public AuthorEntity update(Long id, @Valid AuthorEntityDTO authorEntity) throws NotFoundException {
        log.info("The process of update the author begins!");

        authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author id not found in database. for update");
                    return new NotFoundException("Author id not found!");
                        });

        AuthorEntity author = AuthorEntity.builder()
                .id(id)
                .name(authorEntity.getName())
                .birthday(authorEntity.getBirthday())
                .build();

        AuthorEntity authorUpdate = authorRepository.save(author);
        log.info("Author update successfully.");
        return authorUpdate;
    }

    public void delete(Long id) throws NotFoundException {
        log.info("The process of delete the author begins!");

        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author id not found in database for delete.");
                    return new NotFoundException("Author id not found!");
                        });

        authorRepository.delete(author);
    }

    public Optional<AuthorEntity> findById(Long id) {
        log.info("The process of find by id the author begins!");

        Optional<AuthorEntity> authorFindById = authorRepository.findById(id);
        log.info("Author find by id successfully.");
        return authorFindById;
    }

    public List<AuthorEntity> findByName(String name) {
        log.info("The process of find by name the author begins!");

        List<AuthorEntity> authorFindByName = authorRepository.findAllByNameContaining(name);
        log.info("Author find by name successfully.");
        return authorFindByName;
    }
}
