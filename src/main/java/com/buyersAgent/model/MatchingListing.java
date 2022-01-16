package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class MatchingListing {
    private String listingId;
    private String description;
    private String headline;
    private List<AppointmentTime> appointmentTimeList;

    public MatchingListing(){
        appointmentTimeList = new ArrayList<>();
    }
    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public List<AppointmentTime> getAppointmentTimeList() {
        return appointmentTimeList;
    }

    public void setAppointmentTimeList(List<AppointmentTime> appointmentTimeList) {
        this.appointmentTimeList = appointmentTimeList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
