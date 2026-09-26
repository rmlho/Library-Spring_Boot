package com.ramalho.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherEntityDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String location;
}
