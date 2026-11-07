package com.ramalho.library.service;

import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.PublisherEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublisherService {

    private final IPublisherRepository publisherRepository;
    private final RepositoryMethodInvocationListener repositoryMethodInvocationListener;

    public PublisherEntity save(@Valid PublisherEntityDTO publisherEntity) {

        log.info("The process of saving the publisher begins!");

        PublisherEntity publisher = PublisherEntity.builder()
                .name(publisherEntity.getName())
                .location(publisherEntity.getLocation())
                .build();

        PublisherEntity publisherSave = publisherRepository.save(publisher);
        log.info("Publisher {} successfully registered.", publisherSave.getName());
        return publisherSave;
    }

    public Page<PublisherEntity> findAll(Pageable pageable) {
        Page<PublisherEntity> publisherFindAll = publisherRepository.findAll(pageable);
        log.info("Book update completed successfully.");
        return publisherFindAll;
    }

    @Transactional
    public PublisherEntity update(Long id, @Valid PublisherEntityDTO publisherEntity) throws NotFoundException {

        log.info("The process of update the publisher begins!");

        publisherRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Publisher id not found in database for update.");
                    return new NotFoundException("Publisher id not found!");
                        });

        PublisherEntity publisher = PublisherEntity.builder()
                .id(id)
                .name(publisherEntity.getName())
                .location(publisherEntity.getLocation())
                .build();

        PublisherEntity publisherUpdate = publisherRepository.save(publisher);
        log.info("Publisher update completed successfully.");
        return publisherUpdate;
    }

    public void delete(Long id) throws NotFoundException {
        log.info("The process of delete the publisher begins!");

        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Publisher id not found in database for delete.");
                    return new NotFoundException("Publisher id not found!");
                        });

        publisherRepository.delete(publisher);
    }

    public Optional<PublisherEntity> findById(Long id) {
        log.info("The process of find by id the publisher begins!");

        Optional<PublisherEntity> publisherFindById = publisherRepository.findById(id);
        log.info("Publisher find by id completed successfully.");
        return publisherFindById;
    }

    public List<PublisherEntity> findByName(String name) {
        log.info("The process of find by name the publisher begins!");

        List<PublisherEntity> publisherFindByName = publisherRepository.findAllByNameContaining(name);
        log.info("Publisher find by name completed successfully.");
        return publisherFindByName;
    }
}
