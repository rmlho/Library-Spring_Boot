package com.ramalho.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksEntityDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    private Long authorid;

    @NotBlank
    private String edition;

    @NotNull
    private Long publisherid;

}
