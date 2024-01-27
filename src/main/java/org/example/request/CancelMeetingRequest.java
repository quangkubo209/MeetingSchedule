package org.example.request;

import lombok.extern.java.Log;

// CancelMeeting Request
public class CancelMeetingRequest {
    private String action;
    private Long meetingId;

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
// Getters and setters
}
