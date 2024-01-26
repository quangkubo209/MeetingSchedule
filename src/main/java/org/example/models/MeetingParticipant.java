package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingParticipant {
    private long id;
    private long meetingId;
    private long studentId;

    private Meeting meeting;
    private User user;

    // Getter and Setter methods
}

