package com.ramalho.library.service;

import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.PublisherEntityDTO;
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
public class PublisherService {

    private final IPublisherRepository publisherRepository;

    public PublisherEntity save(@Valid PublisherEntityDTO publisherEntity) {
        PublisherEntity publisher = PublisherEntity.builder()
                .name(publisherEntity.getName())
                .location(publisherEntity.getLocation())
                .build();

        return publisherRepository.save(publisher);
    }

    public List<PublisherEntity> findAll() {
        return publisherRepository.findAll();
    }

    @Transactional
    public PublisherEntity update(Long id, @Valid PublisherEntityDTO publisherEntity) throws NotFoundException {
        publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not found!"));

        PublisherEntity publisher = PublisherEntity.builder()
                .name(publisherEntity.getName())
                .location(publisherEntity.getLocation())
                .build();

        return publisherRepository.save(publisher);
    }

    public void delete(Long id) throws NotFoundException {
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not found!"));

        publisherRepository.delete(publisher);
    }
}
