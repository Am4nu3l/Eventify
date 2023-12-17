package com.example.eventplanner.Objects;

import android.net.Uri;

import java.util.ArrayList;

public class BookedEvent extends Event{
private ArrayList<String>foods;
private ArrayList<String>drinks;
private ArrayList<String>equipments;
    private String name;
    private String size;
    private String date;
    private String bookedDate;
    private Uri imageUrl;
    private String eventId;

    public BookedEvent(String name,String size,String date,String bookedDate,Uri imageUrl,String eventId) {
        super();
        this.name=name;
        this.size=size;
        this.date=date;
        this.bookedDate=bookedDate;
        this.imageUrl=imageUrl;
this.eventId=eventId;
    }
    public BookedEvent(ArrayList<String> foods, ArrayList<String> drinks, ArrayList<String> equipments) {
        super(foods, drinks, equipments);
    }
    @Override
    public String getEventId() {
        return eventId;
    }
    @Override
    public Uri getImageUrl() {
        return imageUrl;
    }

    public BookedEvent(ArrayList<String> eventType, String eventProviderName,String eventProviderPhoneNumber, String eventDescription, String eventAvailableAt, String eventHourOfTheDay, Uri imageUrl, String eventSizeInRange, String eventId, String eventProviderId, ArrayList<String> serviceProvided) {
        super(eventType, eventProviderName,eventProviderPhoneNumber, eventDescription, eventAvailableAt, eventHourOfTheDay, imageUrl, eventSizeInRange, eventId, eventProviderId, serviceProvided);
    }
    @Override
    public ArrayList<String> getFoods() {
        return foods;
    }

    @Override
    public ArrayList<String> getDrinks() {
        return drinks;
    }

    @Override
    public ArrayList<String> getEquipments() {
        return equipments;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getBookedDate() {
        return bookedDate;
    }
}
