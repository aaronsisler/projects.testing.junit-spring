package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.model.Client;
import com.ebsolutions.spring.junit.shared.exception.DataProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody List<@Valid Client> clients) {
        try {
            return ResponseEntity.ok(clientService.create(clients));
        } catch (DataProcessingException dpe) {
            return ResponseEntity.internalServerError().body(dpe.getMessage());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        try {
            List<Client> clients = clientService.readAll();

            return !clients.isEmpty() ? ResponseEntity.ok(clients) : (ResponseEntity<?>) ResponseEntity.noContent();
        } catch (DataProcessingException dpe) {
            return ResponseEntity.internalServerError().body(dpe.getMessage());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage;
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occurred.";
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
