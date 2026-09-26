package com.ramalho.library.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErroResponse {
    private String message;
    private Integer status;
}
