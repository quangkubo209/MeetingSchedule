package org.example.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMeetingRequest {
    private String action;

    private Long meetingId;
    private Long studentId;

    // Getters and setters
}