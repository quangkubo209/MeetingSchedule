package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingMinutes {
    private long id;
    private long meetingId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Meeting meeting;

    // Getter and Setter methods
}
