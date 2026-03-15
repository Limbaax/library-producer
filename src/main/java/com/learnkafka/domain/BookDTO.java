package com.learnkafka.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDTO(
        @NotNull(message = "Book ID cannot be null")
        Integer bookId,
        @NotBlank(message = "Book name cannot be blank")
        String bookName,
        @NotBlank(message = "Book author cannot be blank")
        String bookAuthor) {

}
