package com.ramalho.library.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentResponseDTO {

    private Long id;

    private String nameUser;

    private String nameBook;

    private String statusRent;
}
