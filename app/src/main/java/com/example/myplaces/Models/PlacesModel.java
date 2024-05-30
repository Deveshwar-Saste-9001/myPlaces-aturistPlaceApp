package com.example.myplaces.Models;

public class PlacesModel {
    private String placesId;
    private boolean verified;
    private String placeName;
    private String placeAddress;
    private String place_Image;
    private String place_description;
    private String place_state;
    private String place_country;
    private String place_pincode;
    private Double place_latitude;
    private Double place_longitude;
    private long road_condition;
    private long sefety_level;
    private long adventure_level;
    private long cost_level;
    private long tourist_level;
    private String places_to_stay;

    public PlacesModel(String placesid, boolean verified, String placeName, String placeAddress, String place_Image, String place_description, String place_state, String place_country, String place_pincode, Double place_latitude, Double place_longitude, long road_condition, long sefety_level, long adventure_level, long cost_level, long tourist_level, String places_to_stay) {
        this.placesId = placesid;
        this.verified = verified;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.place_Image = place_Image;
        this.place_description = place_description;
        this.place_state = place_state;
        this.place_country = place_country;
        this.place_pincode = place_pincode;
        this.place_latitude = place_latitude;
        this.place_longitude = place_longitude;
        this.road_condition = road_condition;
        this.sefety_level = sefety_level;
        this.adventure_level = adventure_level;
        this.cost_level = cost_level;
        this.tourist_level = tourist_level;
        this.places_to_stay = places_to_stay;
    }

    public String getPlacesId() {
        return placesId;
    }

    public void setPlacesId(String placesId) {
        this.placesId = placesId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlace_Image() {
        return place_Image;
    }

    public void setPlace_Image(String place_Image) {
        this.place_Image = place_Image;
    }

    public String getPlace_description() {
        return place_description;
    }

    public void setPlace_description(String place_description) {
        this.place_description = place_description;
    }

    public String getPlace_state() {
        return place_state;
    }

    public void setPlace_state(String place_state) {
        this.place_state = place_state;
    }

    public String getPlace_country() {
        return place_country;
    }

    public void setPlace_country(String place_country) {
        this.place_country = place_country;
    }

    public String getPlace_pincode() {
        return place_pincode;
    }

    public void setPlace_pincode(String place_pincode) {
        this.place_pincode = place_pincode;
    }

    public Double getPlace_latitude() {
        return place_latitude;
    }

    public void setPlace_latitude(Double place_latitude) {
        this.place_latitude = place_latitude;
    }

    public Double getPlace_longitude() {
        return place_longitude;
    }

    public void setPlace_longitude(Double place_longitude) {
        this.place_longitude = place_longitude;
    }

    public long getRoad_condition() {
        return road_condition;
    }

    public void setRoad_condition(long road_condition) {
        this.road_condition = road_condition;
    }

    public long getSefety_level() {
        return sefety_level;
    }

    public void setSefety_level(long sefety_level) {
        this.sefety_level = sefety_level;
    }

    public long getAdventure_level() {
        return adventure_level;
    }

    public void setAdventure_level(long adventure_level) {
        this.adventure_level = adventure_level;
    }

    public long getCost_level() {
        return cost_level;
    }

    public void setCost_level(long cost_level) {
        this.cost_level = cost_level;
    }

    public long getTourist_level() {
        return tourist_level;
    }

    public void setTourist_level(long tourist_level) {
        this.tourist_level = tourist_level;
    }

    public String getPlaces_to_stay() {
        return places_to_stay;
    }

    public void setPlaces_to_stay(String places_to_stay) {
        this.places_to_stay = places_to_stay;
    }
}
