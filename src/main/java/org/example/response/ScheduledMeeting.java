package org.example.response;

public class ScheduledMeeting {
    private String meetingId;
    private String startTime;
    private String endTime;
    private String slotType;

    public int getRemainingSlot() {
        return remainingSlot;
    }

    public void setRemainingSlot(int remainingSlot) {
        this.remainingSlot = remainingSlot;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    private int remainingSlot;

    // Getters and setters
}
