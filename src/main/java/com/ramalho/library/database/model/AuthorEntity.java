package com.ramalho.library.database.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "author")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "author_name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthday;
}
