package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate date;
    private MeetingMinutes meetingMinutes;
    private List<MeetingParticipant> meetingParticipants;
    // Getters và Setters
}
