package com.ramalho.library.service;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.repository.IAuthorRepository;
import com.ramalho.library.dto.AuthorEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorEntity save(@Valid AuthorEntityDTO authorEntity) {
        AuthorEntity author = AuthorEntity.builder()
                .name(authorEntity.getName())
                .birthday(authorEntity.getBirthday())
                .build();

        return authorRepository.save(author);
    }

    public List<AuthorEntity> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    public AuthorEntity update(Long id, @Valid AuthorEntityDTO authorEntity) throws NotFoundException {
        authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id not found!"));

        AuthorEntity author = AuthorEntity.builder()
                .name(authorEntity.getName())
                .birthday(authorEntity.getBirthday())
                .build();

        return authorRepository.save(author);
    }

    public void delete(Long id) throws NotFoundException {
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not found!"));

        authorRepository.delete(author);
    }
}
