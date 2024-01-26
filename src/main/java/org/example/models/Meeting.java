package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Meeting {
    private Long id;
    private Long teacherId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String slotType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int slotAvailable;
    private LocalDateTime date;

    private User teacher;
    private List<MeetingParticipant> meetingParticipants;
    private MeetingMinutes meetingMinutes;

    // Getter and Setter methods
}
