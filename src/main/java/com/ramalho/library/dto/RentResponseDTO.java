package com.ramalho.library.dto;

import com.ramalho.library.database.model.EnumStatusRent;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentResponseDTO {

    private String nameUser;

    private String nameBook;

    private String statusRent;
}
