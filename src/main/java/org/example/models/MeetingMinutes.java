package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingMinutes {
    private Long id;
    private Long meetingId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter and Setter methods
}
