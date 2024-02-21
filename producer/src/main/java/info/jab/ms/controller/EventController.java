package info.jab.ms.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.jab.ms.controller.to.EventSubmission;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Value("${kafka.topicName}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(
        value = "/topics/{topic_name}/records", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEvent2(@PathVariable String topic_name) {
        return ResponseEntity.ok().body("Hello World");
    }

    @PostMapping(
        value = "/event", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEvent(@RequestBody(required = true) @Valid EventSubmission submission) 
        throws JsonProcessingException, ExecutionException, InterruptedException {
        
        logger.info("Sending event");
        Event event = new Event(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            submission.getName(),
            submission.getDescription(),
            "anonymous");
        var future = kafkaTemplate.send(topicName, objectMapper.writeValueAsString(event));
        SendResult<String, String> result = future.get();

        logger.info("Event sent");
        return ResponseEntity.ok().body(result.toString());
    }
}
