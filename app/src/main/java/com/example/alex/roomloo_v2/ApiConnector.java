//this class enables the Android app to send and receive data from our Ruby API
// It is largely built off of the PhotoGallery app from bignerdranch android book. Don't try nad use HTTTP Client / Entity as those methods are deprecated and don't seem to work


package com.example.alex.roomloo_v2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10/24/2015.
 */
public class ApiConnector {

    private static final String TAG = "ApiConnector";

    //fetches raw data from a URL and returns it as an array of bytes
    public byte[] getUrlBytes (String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //represents the connection

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream(); //however, until you call getInputStream on the HttpURLConnection you won't actually connect. so this is where we actually connect.

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            //once you create your URL and open a connection you call read() repeatedly until your connection runs out of data
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }

        finally {
            connection.disconnect();
        }

    }

    //converts result from getUrlBytes(string) to a String
    //this gets used in getApartmentList below
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec) );
            }

    //request URL and fetches contents
    public List <Apartment> getApartmentList() {

        List<Apartment> apartmentList = new ArrayList<>();

        try {
            String url = "http://www.roomloo.com/api/apartments";

            String jsonString = getUrlString(url); //getUrlString method is used; this is where our placeholder URL (String urlSpec) from getUrlBytes and getUrlString finally gets a real meaning
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString); //this was formerly a JSONArray but got an error saying value of type JSONObject cannot be converted to JSONArray
            JSONArray jsonArray = jsonObject.getJSONArray("apartments"); //trying to convert my JSONObject into a JSONArray and then really only use this going forward to avoid double counting the arraylist items
            parseApartmentList(apartmentList, jsonArray); //calling the parseApartmentList method defined below
                    }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch apartments - IOException", ioe); //this is getting triggered, prob due to jsonobject vs. jsonarray issue
        }
        return apartmentList;
    }


    //pulls out information for each Apartment.
// makes an Apartment for each and adds it to a List
//the method is called above in getApartmentList()
        //per debugger, jsonObject here is actually the real JSON as is apartmentsJsonArray which is everything inside of the brackets in the apartments array
    private void parseApartmentList(List<Apartment> apartmentList, JSONArray jsonArray) throws IOException, JSONException {

        for (int i=0; i<=jsonArray.length(); i++) { //added an equals sign because debugger says size of my apartmentlist array is 0

            try {

                JSONObject apartmentJsonObject = jsonArray.getJSONObject(i); //get the JSONObject within the array and b/c its in a for loop it gets all of them one at a time;  global method in JSONArray class; getJSONObject(int index)

                //original code to get apt id>> Apartment apartment = new Apartment(UUID.fromString(apartmentJsonObject.getString("id")) ); //UUID from String already converts our JSON string result into a UUID

                Integer idInt = apartmentJsonObject.getInt("id");
                Apartment apartment = new Apartment(idInt);

                apartment.setApartmentText("Price: $" + apartmentJsonObject.getInt("price") + " " + "Bedrooms: " + apartmentJsonObject.getInt("bedrooms") + " " + "Bathrooms: " + apartmentJsonObject.getInt("bathrooms") );
                apartment.setApartmentLatitude(apartmentJsonObject.getJSONObject("building").getDouble("latitude"));
                apartment.setApartmentLongitude(apartmentJsonObject.getJSONObject("building").getDouble("longitude"));

                apartmentList.add(apartment);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//end of for loop


    } // end of parseApartmentList


//request URL and fetches contents
public Apartment getApartment(int idInt) {

    List<Apartment> apartmentList = new ArrayList<>();
    //commenting out to try for loop instead: Apartment apartment = new Apartment(idInt);

    try {
        String url = "http://www.roomloo.com/api/apartments";

        String jsonString = getUrlString(url); //getUrlString method is used; this is where our placeholder URL (String urlSpec) from getUrlBytes and getUrlString finally gets a real meaning
        Log.i(TAG, "Received JSON: " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString); //this was formerly a JSONArray but got an error saying value of type JSONObject cannot be converted to JSONArray
        JSONArray jsonArray = jsonObject.getJSONArray("apartments"); //trying to convert my JSONObject into a JSONArray and then really only use this going forward to avoid double counting the arraylist items
        parseApartment(apartmentList, jsonArray); //calling the parseApartment method defined below
    }
    catch (JSONException je) {
        Log.e(TAG, "Failed to parse JSON", je);
    }
    catch (IOException ioe) {
        Log.e(TAG, "Failed to fetch apartments - IOException", ioe);
    }

    for (Apartment apartment : apartmentList) {
        if (apartment.getId().equals(idInt)) {
            return apartment; //this has a value here per debugger
                }
    }//end of for loop
    Apartment apartment = new Apartment(idInt);
    return apartment; //mApartmentText etc has a value here. apartment is successfully returned
}


    //pulls out information for each Apartment.
// makes an Apartment for each and adds it to a List
//the method is called above in getApartment()
        //per debugger, jsonObject here is actually the real JSON as is apartmentsJsonArray which is everything inside of the brackets in the apartments array
    private void parseApartment(List<Apartment> apartmentList, JSONArray jsonArray) throws IOException, JSONException {

        for (int i=0; i<=jsonArray.length(); i++) { //added an equals sign because debugger says size of my apartmentlist array is 0

            try {

                JSONObject apartmentJsonObject = jsonArray.getJSONObject(i); //get the JSONObject within the array and b/c its in a for loop it gets all of them one at a time;  global method in JSONArray class; getJSONObject(int index)

                //original code to get apt id>> Apartment apartment = new Apartment(UUID.fromString(apartmentJsonObject.getString("id")) ); //UUID from String already converts our JSON string result into a UUID

                Integer idInt = apartmentJsonObject.getInt("id");
                Apartment apartment = new Apartment(idInt); //UUID from String already converts our JSON string result into a UUID

                apartment.setApartmentText(apartmentJsonObject.getInt("price") + " " + apartmentJsonObject.getInt("bedrooms") + " " + apartmentJsonObject.getInt("bathrooms") );
                apartment.setApartmentLatitude(apartmentJsonObject.getJSONObject("building").getDouble("latitude"));
                apartment.setApartmentLongitude(apartmentJsonObject.getJSONObject("building").getDouble("longitude"));

                apartmentList.add(apartment);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//end of for loop


    } // end of parseApartment



}//end of apiconnector class
