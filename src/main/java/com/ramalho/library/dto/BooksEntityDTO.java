package com.ramalho.library.dto;

import com.ramalho.library.database.model.AuthorEntity;
import com.ramalho.library.database.model.PublisherEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private AuthorEntity author;

    @NotBlank
    private String edition;

    @NotNull
    private PublisherEntity publisher;

}
