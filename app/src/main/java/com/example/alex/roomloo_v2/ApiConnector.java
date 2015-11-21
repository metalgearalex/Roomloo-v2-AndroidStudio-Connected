//basically this class uses a HTTP Client, which enables the Android app to send and receive data
//see https://www.youtube.com/watch?v=4Soj22OMc98&feature=iv&src_vid=38HDyEUEpCw&annotation_id=annotation_2056605115


package com.example.alex.roomloo_v2;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 10/24/2015.
 */
public class ApiConnector {

    public JSONArray GetAllCustomers() {
        // URL where your API is located
        String url = "http://www.roomloo.com/api/apartments"; //Ruby API location

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;
//HttpClient supports out of the box all HTTP methods defined in the HTTP/1.1 specification:
// GET, HEAD, POST, PUT, DELETE, TRACE and OPTIONS. There is a specific class for each method type.:
// HttpGet, HttpHead, HttpPost, HttpPut, HttpDelete, HttpTrace, and HttpOptions.
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default Apache HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
//HTTP messages can carry a content entity associated with the request or response.
            httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here

        } catch (IOException e) {
            e.printStackTrace();
                }

        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;

    } //end of public JSONArray GetAllCustomers() method


    //SEEMS THE JSONArray/jsonArray in this whole tab is really just a placeholder the whole time and it
//gets a real value in ListingsFragment when we call getApartmentList()

    //your call to the Ruby API, get apartment essentially, basically the entire row across of data for that apartment
//for CriminalIntent we got the UUID, TITLE, DATE, SOLVED, SUSPECT, etc
//Really it all boils down to something along the lines of JSONObject or JSONArray (usually as a parameter variable
// for example here it’s “json”) + getJSONObject (or Array) then .getString or .getInt etc:
    public List<Apartment> getApartmentList(JSONArray jsonArray) { //parameter is the same JSONArray we get from our httprequest
        //gutted
        // return mApartmentList;

        //reminder: ArrayLists collect objects of the same type
        //you can modify arraylists with things like .set, .add, .remove
//for the below, recall that this is just diamond notation and equivalent to saying mApartmentList = new ArrayList<Apartment>();
        List<Apartment> apartmentList = new ArrayList<>();

        for(int i=0; i<jsonArray.length();i++){ //top nullpointer error

            //calling the appropriate info from the Ruby API

            JSONObject json = null;

            try {
                json = jsonArray.getJSONObject(i); //get the JSONObject within the array and b/c its in a for loop it gets all of them one at a time;  global method in JSONArray class; getJSONObject(int index)

                //if you want to show text alongside your database pull the query looks like
                //jsonString = jsonString +
                //"Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+

                Apartment apartment = new Apartment(UUID.fromString(json.getString("id")) ); //UUID from String already converts our JSON string result into a UUID
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


//added jsonarray as a second parameter post seeing Ruby API structure. May not be correct way to go about this
    //strayed very far from the book / Criminal Intent here. Doesn't seem necessary to call to the Ruby API again? See pg 270 if this doesn't work
    //here you're going to be looking for a specific apartment that's clicked on (so by its respective id)
    // and you need to return all the results for that one apartment
//the other way seems to involve adding jsonarray as a second parameter. however it doesnt seem to work in ApartmentFragment as onCreate can't throw JSONExceptions / ioExceptions for some reason

    public Apartment getApartment(UUID id, JSONArray jsonArray) { //id is a placeholder here and gets a real value in ApartmentFragment when we call this method

//trying to call Ruby API without using the jsonArray parameter / variable from getApartmentList
//NOTE the try / catch is done to avoid having to throw JSONException in the method header, see http://developer.android.com/reference/org/json/JSONException.html
//because that's a problem in ApartmentFragment. onCreate method can't do throws it seems

            List<Apartment> apartments = getApartmentList(jsonArray);

            //reminder the colon is just a shorthand way of saying for each apartment in the List of Apartments
            for (Apartment apartment : apartments) {
                if (apartment.getId().equals(id)) {
                    return apartment;
                }
            }//end of for loop
            return null; //i think this is essentially your else statement, if no apartment matches return null

    }//end of getApartment method






}//end of apiconnector class
