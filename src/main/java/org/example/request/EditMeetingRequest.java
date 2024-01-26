package org.example.request;

public class EditMeetingRequest {
    private String action;
    private Long userId;

    private Long meetingId;

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    private MeetingInformation meetingInformation;

    public MeetingInformation getMeetingInformation() {
        return meetingInformation;
    }

    public void setMeetingInformation(MeetingInformation meetingInformation) {
        this.meetingInformation = meetingInformation;
    }

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
// Getters and setters
}