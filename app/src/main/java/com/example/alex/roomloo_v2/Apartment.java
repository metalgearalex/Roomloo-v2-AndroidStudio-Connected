//your dummy Model-layer-class for the specifics of one apartment

package com.example.alex.roomloo_v2;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 9/17/2015.
 */
public class Apartment {
    private UUID mId;
    private String mApartmentText;
    private Date mDate;
    private double mlatitude; //added for database -> Google Map
    private double mlongitude; //added for database -> Google Map
    //perhaps we need a photoTitle to get the right picture?
    // in CriminalIntent we did:
        // public String getPhotoFilename() {
            //return "IMG_" + getId().toString() + ".jpg";
                //}

    public Apartment() {
        //Generate unique identifier
        this(UUID.randomUUID());
    }

    public Apartment(UUID id) {
        mId = id;
            }

    public UUID getId() {
        return mId;
    }


    public String getApartmentText() {
        return mApartmentText;
    }

    public void setApartmentText(String apartmentText) {
        mApartmentText = apartmentText;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    //added to store location of apartment from Database for Google maps
    public void setApartmentLatitude (Double latitude) {
        mlatitude = latitude;
            }

    public void setApartmentLongitude (Double longitude) {
        mlongitude = longitude;
            }


}

