package com.ramalho.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentRequestEntityDTO {

    @NotBlank
    private String nameBook;
}
