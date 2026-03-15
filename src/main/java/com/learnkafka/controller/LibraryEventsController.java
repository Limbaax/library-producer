package com.learnkafka.controller;

import com.learnkafka.domain.EventType;
import com.learnkafka.domain.LibraryEventDTO;
import com.learnkafka.producer.LiveEventsProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.IllegalFormatCodePointException;

@RestController
@Slf4j
public class LibraryEventsController {

    private final LiveEventsProducer liveEventsProducer;

    public LibraryEventsController(LiveEventsProducer liveEventsProducer) {
        this.liveEventsProducer = liveEventsProducer;
    }


    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEventDTO> postLibraryEvent(@RequestBody @Valid LibraryEventDTO libraryEventDTO) {
        liveEventsProducer.sendLibraryEvent_asynchApproach2(libraryEventDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEventDTO);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEventDTO libraryEventDTO) {

        //Initial request validation
        ResponseEntity<String> badRequest = validateLibraryEvent(libraryEventDTO);

        if (badRequest != null) return  badRequest;

        liveEventsProducer.sendLibraryEvent_asynchApproach2(libraryEventDTO);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEventDTO);
    }

    public ResponseEntity<String> validateLibraryEvent(LibraryEventDTO libraryEventDTO) {

        if (libraryEventDTO.libraryEventId() == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the library event ID");
        }

        if (!libraryEventDTO.libraryEventType().equals(EventType.UPDATE)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE Event Type is supported");
        }
        return null;
    }



}
