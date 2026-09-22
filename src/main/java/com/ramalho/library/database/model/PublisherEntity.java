package com.ramalho.library.database.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publisher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "publisher_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;
}
