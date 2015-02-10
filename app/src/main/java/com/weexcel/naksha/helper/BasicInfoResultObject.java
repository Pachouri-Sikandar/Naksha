package com.weexcel.naksha.helper;

/**
 * Created by Ankit on 30-Jan-15.
 */
public class BasicInfoResultObject {

    Double latitude;
    Double longitude;
    String name;
    String image;
    String openNow;
    String placeID;
    int priceLevel;


    public BasicInfoResultObject(Double latitude, Double longitude, String name, String image, String openNow, String placeID, int priceLevel) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.image = image;
        this.openNow = openNow;
        this.placeID = placeID;
        this.priceLevel = priceLevel;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }
}
