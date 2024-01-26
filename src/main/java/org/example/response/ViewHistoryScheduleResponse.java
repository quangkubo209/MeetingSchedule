package org.example.response;

import java.util.List;

public class ViewHistoryScheduleResponse {
    private String code;
    private List<MeetingDetails> lists;
    private Metadata metadata;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<MeetingDetails> getLists() {
        return lists;
    }

    public void setLists(List<MeetingDetails> lists) {
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