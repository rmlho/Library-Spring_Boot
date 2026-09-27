package com.ramalho.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorEntityDTO {

    @NotBlank
    private String name;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @NotNull
    private LocalDate birthday;
}
