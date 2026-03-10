package com.learnkafka.library_producer.producer;

import com.learnkafka.library_producer.domain.LibraryEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class LiveEventsProducer {


    /** Confirm this approach
     *  @Autowired
     *   private KafkaTemplate<String, LibraryEventRequest> template;
     *
     *   public CompletableFuture send(LibraryEventRequest dto) {  // ← Record direto!
     *     return template.send("library-events", dto.libraryEventId().toString(), dto)
     *       .whenComplete((result, ex) -> { /* log */

    private final KafkaTemplate<Integer,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    public String topic;

    public LiveEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEventDTO libraryEventDTO){

        var key= libraryEventDTO.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEventDTO);


        var completableFuture = kafkaTemplate.send(topic, key , value);

        return completableFuture.whenComplete((sendResult,throwable) ->{
            if(throwable != null) {
                handleFailure(key,value,throwable);
            } else {
                handleSuccess(key,value, sendResult);
            }
        });
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message sent Successfully for the key: {} and the value: {} , partition is {} ", key,value,sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable throwable) {
        log.error("Error sending the mesage and the exception is {} ", throwable.getMessage(), throwable);
    }
}
