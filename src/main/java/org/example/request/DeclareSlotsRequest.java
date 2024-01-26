package org.example.request;

public class DeclareSlotsRequest {
    private String action;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MeetingInformation getMeetingInformation() {
        return meetingInformation;
    }

    public void setMeetingInformation(MeetingInformation meetingInformation) {
        this.meetingInformation = meetingInformation;
    }

    private MeetingInformation meetingInformation;


    // Getters and setters
}