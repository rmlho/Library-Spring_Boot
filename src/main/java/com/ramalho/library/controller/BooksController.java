package com.ramalho.library.controller;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.dto.BooksEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/books")
@Validated
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BooksEntity create(@RequestBody BooksEntityDTO book) throws NotFoundException {
        return booksService.save(book);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BooksEntity> getAll() {
        return booksService.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BooksEntity update(@PathVariable Long id, @RequestBody BooksEntityDTO book) throws NotFoundException {
        return booksService.update(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
       booksService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BooksEntity> getId(@PathVariable Long id) {
        return booksService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<BooksEntity> getByName(@PathVariable String name) {
        return booksService.finByName(name);
    }

}
