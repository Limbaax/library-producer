package com.learnkafka.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDTO(
        @NotNull
        Integer bookId,
        @NotBlank
        String bookName,
        @NotBlank
        String bookAuthor) {

}
