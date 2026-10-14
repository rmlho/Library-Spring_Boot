package com.ramalho.library.service;

import com.ramalho.library.database.model.PublisherEntity;
import com.ramalho.library.database.repository.IPublisherRepository;
import com.ramalho.library.dto.PublisherEntityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

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
        assertEquals(1, publisher.getId());
        assertEquals("Test Publisher", publisher.getName());

    }
}