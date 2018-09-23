package com.projects.lab4;

/**
 * Created by saba7 on 3/26/2018.
 */

public class EventList {
    String eventName;
    String eventDate;
    String eventTime;

    public EventList(String eventName, String eventDate, String eventTime) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }
}
