//basically this class uses a HTTP Client, which enables the Android app to send and receive data
//see https://www.youtube.com/watch?v=4Soj22OMc98&feature=iv&src_vid=38HDyEUEpCw&annotation_id=annotation_2056605115


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
import java.util.UUID;

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
        return apartmentList; //debugger says size = 0
    }


    //pulls out information for each Apartment.
// makes an Apartment for each and adds it to a List
//the method is called above in getApartmentList()
    // Flickr's JSONObject is called "photos" and it contains a JSONArray called "photo" each with an "id" and representing a single photo

    //per debugger, jsonObject here is actually the real JSON as is apartmentsJsonArray which is everything inside of the brackets in the apartments array
    private void parseApartmentList(List<Apartment> apartmentList, JSONArray jsonArray) throws IOException, JSONException {

        for (int i=0; i<=jsonArray.length(); i++) { //added an equals sign because debugger says size of my apartmentlist array is 0

            try {
                //the problem is apartmentsJsonArray and apartmentJsonObject get you the same thing so when you look for your UUID its not unique, each # shows up twice

                JSONObject apartmentJsonObject = jsonArray.getJSONObject(i); //get the JSONObject within the array and b/c its in a for loop it gets all of them one at a time;  global method in JSONArray class; getJSONObject(int index)

                //if you want to show text alongside your database pull the query looks like
                //jsonString = jsonString +
                //"Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+

                //original code to get apt id>> Apartment apartment = new Apartment(UUID.fromString(apartmentJsonObject.getString("id")) ); //UUID from String already converts our JSON string result into a UUID
                    //problem is it's not a string in the actual JSON / API it's an int
                        //you can't convert directly from Int to UUID
                        //But you can convert Int to String with String.valueof(your int value)
                //THE PROBLEM is apartmentsJsonArray and apartmentJsonObject get you the same thing so when you look for your UUID its not unique, each # shows up twice
                Integer idInt = apartmentJsonObject.getInt("id");
                String idString = String.valueOf(idInt);
                Apartment apartment = new Apartment(UUID.fromString(idString) ); //UUID from String already converts our JSON string result into a UUID

                apartment.setApartmentText(apartmentJsonObject.getInt("price") + " " + apartmentJsonObject.getInt("bedrooms") + " " + apartmentJsonObject.getInt("bathrooms") );
                apartment.setApartmentLatitude(apartmentJsonObject.getJSONObject("building").getDouble("latitude"));
                apartment.setApartmentLongitude(apartmentJsonObject.getJSONObject("building").getDouble("longitude"));

                apartmentList.add(apartment);
//note we may have to move the setApartmentText stuff above outside the for loop
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//end of for loop


    } // end of parseApartmentList


    public Apartment getApartment(UUID id) { //id is a placeholder here and gets a real value in ApartmentFragment when we call this method

//trying to call Ruby API without using the jsonArray parameter / variable from getApartmentList
//NOTE the try / catch is done to avoid having to throw JSONException in the method header, see http://developer.android.com/reference/org/json/JSONException.html
//because that's a problem in ApartmentFragment. onCreate method can't do throws it seems

            List<Apartment> apartments = getApartmentList();

            //reminder the colon is just a shorthand way of saying for each apartment in the List of Apartments
            for (Apartment apartment : apartments) {
                if (apartment.getId().equals(id)) {
                    return apartment;
                }
            }//end of for loop
            return null; //i think this is essentially your else statement, if no apartment matches return null

    }//end of getApartment method






}//end of apiconnector class
