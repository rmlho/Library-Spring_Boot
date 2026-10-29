package com.ramalho.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequestDTO {

    @NotBlank
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Invalid minimum size.")
    private String password;
}
