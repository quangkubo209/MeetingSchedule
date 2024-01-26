package org.example.response;

import java.util.List;

// Response
public class ViewAvailableTimeSlotsResponse {
    private String code;
    private List<TimeSlot> lists;
    private Metadata metadata;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

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

    // Getters and setters
}

