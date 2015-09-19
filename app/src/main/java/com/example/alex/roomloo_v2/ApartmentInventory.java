//your dummy model class for the list of apartments we have (Apartment is the model layer for just one apartment
// as opposed to the list of apartments)

package com.example.alex.roomloo_v2;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 9/17/2015.
 */
public class ApartmentInventory {

    private static ApartmentInventory sApartmentInventory; //a singleton, i.e. a class that allows only one instance of itself to be created
    private List<Apartment> mApartmentList;

    //to create a singleton you create a calss with a private constructor and a get method,
    // if the instance already exists then get() simply returns the instance
    //see page 169
    public static ApartmentInventory get(Context context) {
        if(sApartmentInventory == null) {
            sApartmentInventory = new ApartmentInventory(context);
                }
        return sApartmentInventory;
            }
//creating a dummy set of 20 apartments as our mock inventory
    private ApartmentInventory(Context context) {
        mApartmentList = new ArrayList<>();
        for (int i=0; i<20; i++) {
            Apartment apartment = new Apartment();
            apartment.setApartmentText("Apartment #" + i);
            mApartmentList.add(apartment);
                }
            }
    //for the below, recall that this is just diamond notation and equivalent to saying mApartmentList = new ArrayList<Apartment>();
    public List<Apartment> getApartmentList() {
        return mApartmentList;
            }

    public Apartment getApartment(UUID id) {
        for (Apartment apartment: mApartmentList) {
            if(apartment.getId().equals(id) ) {
                return apartment;
                        }
                    }
        return null;
    }

}
