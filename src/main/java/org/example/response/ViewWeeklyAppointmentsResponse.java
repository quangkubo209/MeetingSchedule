package org.example.response;

import java.util.List;

// Response
public class ViewWeeklyAppointmentsResponse {
    private String code;
    private List<TimeSlot> lists;

    public List<TimeSlot> getLists() {
        return lists;
    }

    public void setLists(List<TimeSlot> lists) {
        this.lists = lists;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    private Metadata metadata;

    // Getters and setters
}


