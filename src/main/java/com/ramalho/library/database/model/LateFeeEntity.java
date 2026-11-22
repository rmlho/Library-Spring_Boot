package com.ramalho.library.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "late_fee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LateFeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rents", unique = true, nullable = false)
    private RentEntity rent;

    @Column(precision = 10, scale = 2)
    private BigDecimal fine;

    @Column(nullable = false)
    private Integer daysOverdue;

    @Column(nullable = false)
    private LocalDate dateGenerated;

    private LocalDate paymentDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumFineStatus status;
}
