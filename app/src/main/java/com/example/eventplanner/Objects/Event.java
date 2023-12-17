package com.example.eventplanner.Objects;

import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;

public class Event {
private ArrayList<String> eventType;
private String eventProviderName;
    private String eventProviderPhoneNumber;
    private String eventProviderId;
private String eventDescription;
private String eventAvailableAt;
private ArrayList<String>serviceProvided;
    private String eventHourOfTheDay;
private Uri imageUrl;
private String eventId;


    private String eventSizeInRange;
    private ArrayList<String>foods;
    private ArrayList<String>drinks;
    private ArrayList<String>equipments;
public Event(ArrayList<String>foods,ArrayList<String>drinks,ArrayList<String>equipments){
    this.foods=foods;
    this.drinks=drinks;
    this.equipments=equipments;
}
    public Event(ArrayList<String> eventType, String eventProviderName,String eventProviderPhoneNumber, String eventDescription,
                 String eventAvailableAt, String eventHourOfTheDay, Uri imageUrl, String eventSizeInRange, String eventId, String eventProviderId,ArrayList<String>serviceProvided) {
        this.eventType = eventType;
        this.eventProviderName = eventProviderName;
        this.eventDescription = eventDescription;
        this.eventAvailableAt = eventAvailableAt;
        this.imageUrl = imageUrl;
        this.eventProviderPhoneNumber=eventProviderPhoneNumber;
        this. eventHourOfTheDay=eventHourOfTheDay;
        this.eventSizeInRange = eventSizeInRange;
        this.eventId=eventId;
        this.serviceProvided=serviceProvided;
                        this.eventProviderId=eventProviderId;
    }

    public Event() {

    }

    public String getEventProviderPhoneNumber() {
        return eventProviderPhoneNumber;
    }

    public ArrayList<String> getFoods() {
        return foods;
    }

    public ArrayList<String> getDrinks() {
        return drinks;
    }

    public ArrayList<String> getEquipments() {
        return equipments;
    }

    public ArrayList<String> getServiceProvided() {
        return serviceProvided;
    }

    public String getEventProviderId() {
        return eventProviderId;
    }

    public ArrayList<String> getEventType() {
        return eventType;
    }

    public String getEventProviderName() {
        return eventProviderName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventAvailableAt() {
        return eventAvailableAt;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public String getEventSizeInRange() {
        return eventSizeInRange;
    }
    public String getEventHourOfTheDay() {
        return eventHourOfTheDay;
    }

    public String getEventId() {
        return eventId;
    }
}
