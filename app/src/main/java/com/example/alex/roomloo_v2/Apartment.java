//your dummy Model-layer-class for the specifics of one apartment

package com.example.alex.roomloo_v2;

import java.util.Date;

/**
 * Created by Alex on 9/17/2015.
 */
public class Apartment {
    private int mId;
    private String mApartmentText;
    private Date mDate;
    private double mlatitude; //added for database -> Google Map
    private double mlongitude; //added for database -> Google Map
    //perhaps we need a photoTitle to get the right picture?
    // in CriminalIntent we did:
        // public String getPhotoFilename() {
            //return "IMG_" + getId().toString() + ".jpg";
                //}

//gutted for now.
//    public Apartment() {
        //Generate unique identifier
//        this(UUID.randomUUID() );

        //something along these lines may work if we ungut it
            //Random generator = new Random();
            //int randomIntegerId = generator.nextInt(2000000000); //hardcode. gives you an integer between 0 and n-1. this is more or less the biggest number it lets you input
//            }



    public Apartment(int id) {
        mId = id;
            }

    public Integer getId() {
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
    public Double getApartmentLatitude () {
        return mlatitude;
            }

    public void setApartmentLatitude (Double latitude) {
        mlatitude = latitude;
            }

    public Double getApartmentLongitude () {
        return mlongitude;
            }

    public void setApartmentLongitude (Double longitude) {
        mlongitude = longitude;
            }


}

