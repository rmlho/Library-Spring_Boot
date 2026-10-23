package com.ramalho.library.service;

import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.PublisherEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private IPublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherEntity createPublisher() {
        return PublisherEntity.builder()
                .id(1L)
                .name("Test Publisher")
                .location("Test location")
                .build();
    }

    private PublisherEntityDTO createPublisherDTO() {
        return PublisherEntityDTO.builder()
                .name("Test Publisher")
                .location("Test location")
                .build();
    }

    @Test
    void savePublisher() {
        PublisherEntity publisherSave = createPublisher();
        PublisherEntityDTO publisherSaveDTO = createPublisherDTO();

        Mockito.when(publisherRepository.save(any(PublisherEntity.class)))
                .thenReturn(publisherSave);

        PublisherEntity publisher = publisherService.save(publisherSaveDTO);

        assertNotNull(publisher);
        assertEquals(1L, publisher.getId());
        assertEquals("Test Publisher", publisher.getName());

    }

    @Test
    void findAllPublishers() {
        PublisherEntity publisherSave = createPublisher();

        List<PublisherEntity> publisherEntities = List.of(publisherSave);

        Page<PublisherEntity> pagebleEntities = new PageImpl<>(publisherEntities);

        Mockito.when(publisherRepository.findAll(any(Pageable.class)))
                .thenReturn(pagebleEntities);

        Pageable pageable = PageRequest.of(0, 10);

        Page<PublisherEntity> page = publisherService.findAll(pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals("Test Publisher", page.getContent().getFirst().getName());
    }

    @Test
    void updatePublisher() throws NotFoundException {
        PublisherEntity oldPublisher = createPublisher();
        PublisherEntityDTO newPublisherDTO = createPublisherDTO();

        Mockito.when(publisherRepository.findById(1L)).
                thenReturn(Optional.of(oldPublisher));

        Mockito.when(publisherRepository.save(any(PublisherEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PublisherEntity publisher = publisherService.update(1L, newPublisherDTO);

        assertNotNull(publisher);
        assertEquals(1L, publisher.getId());
        assertEquals(publisher.getName(), oldPublisher.getName());
        assertEquals("Test Publisher", publisher.getName());
    }

    @Test
    void update_NotFound_whenIdNotFound() {
        PublisherEntityDTO oldPublisher = createPublisherDTO();

        Mockito.when(publisherRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> publisherService.update(99L, oldPublisher));
    }

    @Test
    void deletePublisher() throws NotFoundException {
        PublisherEntity publisherSave = createPublisher();

        Mockito.when(publisherRepository.findById(1L))
                .thenReturn(Optional.of(publisherSave));

        publisherService.delete(1L);

        verify(publisherRepository, Mockito.times(1)).delete(publisherSave);

    }

    @Test
    void delete_NotFound_whenIdNotFound() {
        Mockito.when(publisherRepository.findById(1L))
                        .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> publisherService.delete(1L));
    }

    @Test
    void findByIdPublisher() {
        PublisherEntity publisherDB = createPublisher();

        Mockito.when(publisherRepository.findById(1L))
                .thenReturn(Optional.of(publisherDB));

        Optional<PublisherEntity> publisherSearch = publisherService.findById(1L);

        assertTrue(publisherSearch.isPresent());
        assertEquals(publisherDB.getId(), publisherSearch.get().getId());
        assertEquals(publisherDB.getName(), publisherSearch.get().getName());
    }

    @Test
    void findByNameContaining_whenNameToExist() {
        PublisherEntity publisherDB = createPublisher();

        Mockito.when(publisherRepository.findAllByNameContaining("Test"))
                .thenReturn(List.of(publisherDB));

        List<PublisherEntity> publisherEntityList = publisherService.findByName("Test");

        assertNotNull(publisherEntityList);
        assertFalse(publisherEntityList.isEmpty());
        assertEquals(publisherDB.getName(), publisherEntityList.getFirst().getName());
    }

    @Test
    void findByNameContaining_whenNameNotToExist() {
        Mockito.when(publisherRepository.findAllByNameContaining("Name Not Exist"))
                .thenReturn(List.of());

        List<PublisherEntity> publisherEntityList = publisherService.findByName("Name Not Exist");

        assertNotNull(publisherEntityList);
        assertTrue(publisherEntityList.isEmpty());
    }
}