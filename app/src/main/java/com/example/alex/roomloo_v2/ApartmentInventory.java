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
    public ApartmentInventory (Context context) { //changed to public to try to fix non-static method / static context in ApartmentFragment class within onCreateView method / GoogleMaps code

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
// for example here it’s “json”) + getJSONObject (or Array) then .getString or .getInt etc:
    public List<Apartment> getApartmentList(JSONArray jsonArray) { //added the parameter, placeholder for "apartments" array which contains all apts in our database
        //gutted
            // return mApartmentList;

        //reminder: ArrayLists collect objects of the same type
        //you can modify arraylists with things like .set, .add, .remove
//for the below, recall that this is just diamond notation and equivalent to saying mApartmentList = new ArrayList<Apartment>();
        List<Apartment> apartmentList = new ArrayList<>();

        for(int i=0; i<jsonArray.length();i++){

            //calling the appropriate info from the Ruby API

            JSONObject json = null;

            try {
                json = jsonArray.getJSONObject(i); //get the JSONObject within the array and b/c its in a for loop it gets all of them one at a time;  global method in JSONArray class; getJSONObject(int index)

                //if you want to show text alongside your database pull the query looks like
                    //jsonString = jsonString +
                    //"Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+

                Apartment apartment = new Apartment(UUID.fromString(json.getString("id") ) ); //UUID from String already converts our JSON string result into a UUID
                apartment.setApartmentText(json.getInt("price") + " " + json.getInt("bedrooms") + " " + json.getInt("bathrooms") );
                apartment.setApartmentLatitude(json.getJSONObject("building").getDouble("latitude"));
                apartment.setApartmentLongitude(json.getJSONObject("building").getDouble("longitude"));

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


//added jsonarray as a second parameter post seeing Ruby API structure. May not be correct way to go about this
    //strayed very far from the book / Criminal Intent here. Doesn't seem necessary to call to the Ruby API again? See pg 270 if this doesn't work
        //here you're going to be looking for a specific apartment that's clicked on (so by its respective id)
        // and you need to return all the results for that one apartment
//the other way seems to involve adding jsonarray as a second parameter. however it doesnt seem to work in ApartmentFragment as onCreate can't throw JSONExceptions / ioExceptions for some reason

    public Apartment getApartment(UUID id) { //id is a placeholder here and gets a real value in ApartmentFragment when we call this method
        //gutted
            // for (Apartment apartment: mApartmentList) {
            //if(apartment.getId().equals(id) ) {
            //    return apartment;
            //            }
            //        }

//trying to call Ruby API without using the jsonArray parameter / variable from getApartmentList
//NOTE the try / catch is done to avoid having to throw JSONException in the method header, see http://developer.android.com/reference/org/json/JSONException.html
//because that's a problem in ApartmentFragment. onCreate method can't do throws it seems
     try {
         JSONArray placeholderJsonAray = new JSONArray(); //does this make sense? creating a placeholderJsonArray to then call getJSONArray on and get the Array at position 0 , i.e. the first one
         JSONArray realJsonArrayValue = placeholderJsonAray.getJSONArray(0);
         List<Apartment> apartments = getApartmentList(realJsonArrayValue);

         //reminder the colon is just a shorthand way of saying for each apartment in the List of Apartments
         for (Apartment apartment : apartments) {
             if (apartment.getId().equals(id)) {
                 return apartment;
                    }
         }//end of for loop
         return null; //i think this is essentially your else statement, if no apartment matches return null
     } // end of try

     catch (JSONException jsonExceptionGetApartmentMethod) {
         throw new RuntimeException(jsonExceptionGetApartmentMethod);
            }

    }//end of getApartment method


    //added this method to get location from database for Google Maps
    //passing in the id so that the location it brings me is for one specific apartment
    public double getApartmentLatitude (UUID id) {//id here is a placeholder and gets a real value in ApartmentFragment

        Double jsonString  = null;

        //fix this to call the appropriate info from Ruby app

        JSONObject json = null;
        try {
            jsonString = json.getDouble(jsonString + "latitude: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

            Apartment apartment= new Apartment(id);
            apartment.setApartmentLatitude(jsonString);
            return jsonString;


    }//end of getApartmentLatitude method


    //added this method to get location from database for Google Maps
    //passing in the id so that the location it brings me is for one specific apartment
    public double getApartmentLongitude (UUID id) { //id here is a placeholder and gets a real value in ApartmentFragment

        Double jsonString  = null;

        //fix this to call the appropriate info from Ruby app

        JSONObject json = null;
        try {
            jsonString = json.getDouble(jsonString + "latitude: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Apartment apartment= new Apartment(id);
        apartment.setApartmentLatitude(jsonString);
        return jsonString;


    }//end of getApartmentLongitude method


}
