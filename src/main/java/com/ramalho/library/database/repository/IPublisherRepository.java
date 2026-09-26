package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPublisherRepository extends JpaRepository<PublisherEntity, Long> {
}
