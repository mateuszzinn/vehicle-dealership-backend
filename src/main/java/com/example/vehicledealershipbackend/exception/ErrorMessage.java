package com.example.vehicledealershipbackend.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private int status;
    private String error;
    private Map<String, String> messages;
    private LocalDateTime timestamp;

    public ErrorMessage(int status, String error, Map<String, String> messages) {
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.timestamp = LocalDateTime.now();
    }
}
