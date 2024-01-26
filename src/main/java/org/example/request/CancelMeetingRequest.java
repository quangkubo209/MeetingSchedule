package org.example.request;

// CancelMeeting Request
public class CancelMeetingRequest {
    private String action;
    private String meetingId;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
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
