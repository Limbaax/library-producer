package com.learnkafka.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEventDTO(
        Integer libraryEventId,
        EventType libraryEventType,
        @NotNull
        @Valid
        BookDTO bookDTO) {

}
