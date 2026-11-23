package model;

import java.sql.Date;

public class Event {

    private int eventId;
    private String title;
    private int categoryId;
    private String categoryName;
    private String location;
    private Date eventDate;
    private int slots;
    private String description;
    private int joined;   // number of approved volunteers

    public Event() {
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public int getSlots() { return slots; }
    public void setSlots(int slots) { this.slots = slots; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getJoined() { return joined; }
    public void setJoined(int joined) { this.joined = joined; }
}
