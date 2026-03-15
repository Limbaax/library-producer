package com.learnkafka.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEventDTO(
        Integer libraryEventId,
        EventType libraryEventType,
        @NotNull(message = "Book cannot be null")
        @Valid
        BookDTO bookDTO) {

}
