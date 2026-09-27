package com.ramalho.library.controller;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.dto.BooksEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BooksEntity update(@PathVariable Long id, @RequestBody BooksEntityDTO book) throws NotFoundException {
        return booksService.save(book);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) throws NotFoundException {
       booksService.delete(id);
    }
}
