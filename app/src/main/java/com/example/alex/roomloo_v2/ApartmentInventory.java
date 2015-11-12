//your model class for the list of apartments we have (Apartment is the model layer for just one apartment
// as opposed to the list of apartments)
//Really only getApartmentList and the get methods are used in other classes from ApartmentInventory
//from our web Service the method we want (at least thus far) is => getAnswerSql ()
package com.example.alex.roomloo_v2;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 9/17/2015.
 */
public class ApartmentInventory {

    private static ApartmentInventory sApartmentInventory; //a singleton, i.e. a class that allows only one instance of itself to be created
    //gutted:  private List<Apartment> mApartmentList;
    //commenting this out again, i guess i was trying to connect straight to the heroku database with ksoap?
        //added for code to connect to web service layer
        //Note: WSDL stands for Web Services Description Language
        //An WSDL document describes a web service. It specifies the location of the service, and the methods of the service
            //private static final String SOAP_ACTION = "http://ws.android.com/sayHello"; //Namespace/methodname
            //private static final String METHOD_NAME = "sayHello"; //a WSDL operation?
            //private static final String NAMESPACE = "http://ws.android.com/"; //URL of WSDL file
            //private static final String URL = "http://175.157.229.119:8080/AndroidWSTest/services/PrintMsg?wsdl"; //ip of server : port number /


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
    private ApartmentInventory (Context context) {

        new GetAllCustomerTask().execute(new ApiConnector()); //maybe move this to one of your oncreate methods in your mainactivity?

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


//SEEMS THE JSONArray/jsonArray in this whole tab is really just a placeholder the whole time and it
//gets a real value in ListingsFragment when we call getApartmentList()

//your call to the Ruby API, get apartment essentially, basically the entire row across of data for that apartment
//for CriminalIntent we got the UUID, TITLE, DATE, SOLVED, SUSPECT, etc
//Really it all boils down to something along the lines of JSONObject or JSONArray (usually as a parameter variable
// for example here it’s “json”) + getJSONObject (or Array) then .getString:
    public List<Apartment> getApartmentList(JSONArray jsonArray) { //added the parameter
        //gutted
            // return mApartmentList;

        //reminder: ArrayLists collect objects of the same type
        //you can modify arraylists with things like .set, .add, .remove
//for the below, recall that this is just diamond notation and equivalent to saying mApartmentList = new ArrayList<Apartment>();
        List<Apartment> apartmentList = new ArrayList<>();

        String jsonString  = "";
        for(int i=0; i<jsonArray.length();i++){

            //fix this to call the appropriate info from Ruby app

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i); //global method in JSONArray class; getJSONObject(int index)
                jsonString = jsonString +
                        "Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+
                        "Age : "+json.getInt("Age")+"\n"+
                        "Mobile Using : "+json.getString("Mobile")+"\n\n";
                Apartment apartment = new Apartment();
                apartment.setApartmentText(jsonString);
                apartmentList.add(apartment);
//note we may have to move the setApartmentText stuff above outside the for loop
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//end of for loop

        return apartmentList;

        //then set the Text based on those values -- Seems unnecessary since this is my model class vs. the Activity class in the tutorial?
            //this.responseTextView.setText(s);

            }//end of getApartmentList method


    private class GetAllCustomerTask extends AsyncTask<ApiConnector,Long,JSONArray> //JSONArray here specifies the type of result you'll be sending back to the main thread
    {
        //the ... is a way to tell Android you have a   variable number of parameters
        //params seems to be a common way to pass in a variable type or something along the lines
        //U can then pass multiple items and just access like params[0].. etc..
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].GetAllCustomers(); //essentially returns your JSONArray
        }


        //reminder the ApiConnector class is really a giant JSONArray
        //also the api returns the result in a variable called jsonArray
        //and we get that in a JSONArray through the doInBackground method override within GetAllCustomerTask
        //HOWEVER, SEEMS THE JSONArray/jsonArray in this whole tab is really just a placeholder the whole time and it
        //gets a real value in ListingsFragment when we call getApartmentList()
        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            getApartmentList(jsonArray);


        }
    }



    public Apartment getApartment(UUID id) {
        //gutted
            // for (Apartment apartment: mApartmentList) {
            //if(apartment.getId().equals(id) ) {
            //    return apartment;
            //            }
            //        }

//your call to the Ruby API; should be very similar to the getApartmentList method but here you only need to query for the UUID
//you don't need an ArrayList here

        String jsonString  = "";

            //fix this to call the appropriate info from Ruby app

            JSONObject json = null;
            try {
                jsonString = jsonString +
                        "Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n";
//note we may have to move the setApartmentText stuff above outside the for loop
            } catch (JSONException e) {
                e.printStackTrace();
            }

        if (jsonString ==null) {
            return null;
        }
        else {
            Apartment apartment = new Apartment();
        apartment.setApartmentText(jsonString);
        return apartment;
            }

    }//end of getApartment method


    //added this method to get location from database for Google Maps
    //passing in the id so that the location it brings me is for one specific apartment
    public Apartment getApartmentLatitude (UUID id) {

        Double jsonString  = null;

        //fix this to call the appropriate info from Ruby app

        JSONObject json = null;
        try {
            jsonString = json.getDouble(jsonString + "latitude: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonString ==null) {
            return null;
        }
        else {
            Apartment apartment = new Apartment();
            apartment.setApartmentLatitude(jsonString);
            return apartment;
        }

    }//end of getApartment method


    //added this method to get location from database for Google Maps
    //passing in the id so that the location it brings me is for one specific apartment
    public Apartment getApartmentLongitude (UUID id) {

        Double jsonString  = null;

        //fix this to call the appropriate info from Ruby app

        JSONObject json = null;
        try {
            jsonString = json.getDouble(jsonString + "longitude: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonString ==null) {
            return null;
        }
        else {
            Apartment apartment = new Apartment();
            apartment.setApartmentLongitude(jsonString);
            return apartment;
        }

    }//end of getApartment method


}
