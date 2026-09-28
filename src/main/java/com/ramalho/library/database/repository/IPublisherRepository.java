package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPublisherRepository extends JpaRepository<PublisherEntity, Long> {
    List<PublisherEntity> findAllByNameContaining(String name);
}
