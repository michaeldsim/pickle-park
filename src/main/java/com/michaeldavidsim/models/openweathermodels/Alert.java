package com.michaeldavidsim.models.openweathermodels;

import java.util.List;

public class Alert {
    private String sender_name;
    private String event;
    private long start;
    private long end;
    private String description;
    private List<String> tags;
    
    public String getSender_name() {
        return sender_name;
    }
    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }
    public long getEnd() {
        return end;
    }
    public void setEnd(long end) {
        this.end = end;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
