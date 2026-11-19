package com.ramalho.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ramalho.library.database.model.BooksEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksResponseDTO {

    private String name;
    private String gender;
    private Integer quantity;
    private AuthorResponse author;
    private String edition;
    private PublisherResponse publisher;

    public static BooksResponseDTO from(BooksEntity book) {
        return new BooksResponseDTO(
                book.getName(),
                book.getGender(),
                book.getQuantity(),
                new AuthorResponse(book.getAuthor().getName(), book.getAuthor().getBirthday()),
                book.getEdition(),
                new PublisherResponse(book.getPublisher().getName(), book.getPublisher().getLocation())
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthorResponse {

        private String name;

        @JsonFormat(pattern = "MM/dd/yyyy")
        private LocalDate birthday;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublisherResponse {

        private String name;
        private String location;
    }
}
