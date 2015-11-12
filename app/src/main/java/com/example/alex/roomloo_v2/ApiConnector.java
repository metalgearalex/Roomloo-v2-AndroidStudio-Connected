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
import java.io.IOException;

/**
 * Created by Alex on 10/24/2015.
 */
public class ApiConnector {

    public JSONArray GetAllCustomers() {
        // URL where your API is located
        String url = "http://192.168.0.7/~tahseen0amin/Tutorial/getAllCustomers.php"; //change this to Ruby API location

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

}//end of apiconnector class
