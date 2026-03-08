package com.learnkafka.library_producer.domain;

public record LibraryEventDTO(Integer libraryEventId, EventType libraryEventType, BookDTO bookDTO) {

}
