package com.ramalho.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksUpdateQuantDTO {

    @NotNull
    @PositiveOrZero
    private Integer quantity;
}
