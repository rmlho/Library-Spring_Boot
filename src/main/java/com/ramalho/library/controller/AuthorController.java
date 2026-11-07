package com.ramalho.library.controller;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.dto.AuthorEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/author")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorEntity create(@RequestBody AuthorEntityDTO author) {
        log.info("Request to create author.");
        return authorService.save(author);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AuthorEntity> getAll(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request to find all authors.");
        return authorService.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorEntity update(@PathVariable Long id, @RequestBody AuthorEntityDTO author) throws NotFoundException {
        log.info("Request to update author.");
        return authorService.update(id, author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        log.info("Request to delete author.");
        authorService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorEntity> getId(@PathVariable Long id) {
        log.info("Request to get id author.");
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorEntity> getByName(@PathVariable String name) {
        log.info("Request to get by name author.");
        return authorService.findByName(name);
    }

}
