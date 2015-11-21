//your model class for the list of apartments we have (Apartment is the model layer for just one apartment
// as opposed to the list of apartments)
//Really only getApartmentList and the get methods are used in other classes from ApartmentInventory
//from our web Service the method we want (at least thus far) is => getAnswerSql ()
package com.example.alex.roomloo_v2;

import android.content.Context;

import java.util.List;

/**
 * Created by Alex on 9/17/2015.
 */
public class ApartmentInventory {

    private static ApartmentInventory sApartmentInventory; //a singleton, i.e. a class that allows only one instance of itself to be created
    private Context mContext;
    private List<Apartment> mApartmentList; //added for new method to try and get ApartmentInventory method to work properly / avoid nullpointer errors

    //to create a singleton you create a class with a private constructor and a get method,
    // if the instance already exists then get() simply returns the instance
    //see page 169
    public static ApartmentInventory get(Context context) {
        if(sApartmentInventory == null) {
            sApartmentInventory = new ApartmentInventory(context);
                }
        return sApartmentInventory;
            }

//getting our apartment inventory
//context here refers to ListingsActivity
    public ApartmentInventory (Context context) { //changed to public to try to fix non-static method / static context in ApartmentFragment class within onCreateView method / GoogleMaps code
        mContext = context.getApplicationContext();

        ApiConnector apiConnector = new ApiConnector();
        apiConnector.getApartmentList();


//CriminalIntent used an Android helper class called SQLiteOpenHelper to open the database here. See page 259
        //so open json here?

        //this didn't work either same nullpointer error >> ApiConnector api = new ApiConnector();

        //the below 2 lines resulted in a NetworkOnMainThread error
            //ApiConnector getAPI = new ApiConnector();
            //getAPI.GetAllCustomers();

        //prior attempt, probably wrong: List<Apartment> apartmentList = new ArrayList<>();

        //something like this instead? ApartmentInventory apartmentInventory = ApartmentInventory.get(getActivity());

        //this is what CriminalIntent replaced the gutted stuff with:
        //seems unnecessary? doesnt seem like this method is called anywhere
            //mContext = context.getApplicationContext();
            //mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //gutted
        //creating a dummy set of 20 apartments as our mock inventory
            //mApartmentList = new ArrayList<>();
            //for (int i=0; i<20; i++) {
            //    Apartment apartment = new Apartment();
            //    apartment.setApartmentText("Apartment #" + i);
            //    mApartmentList.add(apartment);
            //      }
            }






}
