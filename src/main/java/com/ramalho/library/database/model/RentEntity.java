package com.ramalho.library.database.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rents_book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "users", nullable = false)
    private UsersEntity users;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "books", nullable = false)
    private BooksEntity books;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "registration_date_rent")
    private LocalDateTime orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "returned_rent")
    private LocalDate returnedAt;

    @Enumerated(EnumType.STRING)
    private EnumStatusRent statusRent;
}
