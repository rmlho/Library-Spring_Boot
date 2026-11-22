package com.ramalho.library.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LateFeeResponseDTO {

    private Long id;
    private String nameBook;
    private BigDecimal fine;
    private Integer daysOverdue;
    private String status;

}
