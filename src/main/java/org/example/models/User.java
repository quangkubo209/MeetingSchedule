package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class User {
    private long id;
    private String username;
    private String password;
    private String fullname;
    private String role;
    private LocalDateTime createdAt;

    private List<Meeting> meetings;
    private List<MeetingParticipant> meetingParticipants;
    private List<UserSessions> userSessions;

    // Getter and Setter methods
}

