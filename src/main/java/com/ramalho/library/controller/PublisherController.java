package com.ramalho.library.controller;

import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.PublisherEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/publisher")
@Validated
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherEntity create(@RequestBody PublisherEntityDTO publisher) {
        return publisherService.save(publisher);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PublisherEntity> getAll(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return publisherService.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherEntity update(@PathVariable Long id, @RequestBody PublisherEntityDTO publisher) throws NotFoundException {
        return publisherService.update(id, publisher);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        publisherService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherEntity> getById(@PathVariable Long id) {
        return publisherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<PublisherEntity> getByName(@PathVariable String name) {
        return publisherService.findByName(name);
    }



}
