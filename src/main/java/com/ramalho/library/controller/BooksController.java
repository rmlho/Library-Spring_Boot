package com.ramalho.library.controller;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.dto.BooksEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.BooksService;
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
@RequestMapping("/v1/books")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BooksController {

    private final BooksService booksService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BooksEntity create(@RequestBody BooksEntityDTO book) throws NotFoundException {
        log.info("Request to create book.");
        return booksService.save(book);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BooksEntity> getAll(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request to find all books.");
        return booksService.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BooksEntity update(@PathVariable Long id, @RequestBody BooksEntityDTO book) throws NotFoundException {
        log.info("Request to update book.");
        return booksService.update(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        log.info("Request to delete book.");
        booksService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BooksEntity> getId(@PathVariable Long id) {
        log.info("Request to get id book.");
        return booksService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<BooksEntity> getByName(@PathVariable String name) {
        log.info("Request to find by name book.");
        return booksService.findByName(name);
    }

    @GetMapping("/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<BooksEntity> getByGender(@PathVariable String gender) {
        log.info("Request to find by gender books.");
        return booksService.findByGender(gender);
    }

    @GetMapping("/registrationDate")
    @ResponseStatus(HttpStatus.OK)
    public List<BooksEntity> getAllByRegistrationDate() {
        log.info("Request to get registration date books.");
        return booksService.orderByRegistrationDate();
    }
}
