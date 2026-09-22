package com.ramalho.library.database.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Author", nullable = false)
    private AuthorEntity Author;

    @Column(nullable = false)
    private String edition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Publisher", nullable = false)
    private PublisherEntity Publusher;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
}
