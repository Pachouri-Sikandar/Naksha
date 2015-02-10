package com.weexcel.naksha.helper;

import java.io.Serializable;

/**
 * Created by Ankit on 02-Feb-15.
 */
public class ReviewInfoResultObject implements Serializable {
    String placeID;
    String website;
    int ratingIndividual;
    String authorIndividual;
    String authorURL;


    public ReviewInfoResultObject(String placeID, String website, int ratingIndividual, String authorIndividual, String authorURL) {
        this.placeID = placeID;
        this.website = website;
        this.ratingIndividual = ratingIndividual;
        this.authorIndividual = authorIndividual;
        this.authorURL = authorURL;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getRatingIndividual() {
        return ratingIndividual;
    }

    public void setRatingIndividual(int ratingIndividual) {
        this.ratingIndividual = ratingIndividual;
    }

    public String getAuthorIndividual() {
        return authorIndividual;
    }

    public void setAuthorIndividual(String authorIndividual) {
        this.authorIndividual = authorIndividual;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }
}
