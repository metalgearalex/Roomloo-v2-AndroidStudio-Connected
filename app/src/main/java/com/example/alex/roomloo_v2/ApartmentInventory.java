//your dummy model class for the list of apartments we have (Apartment is the model layer for just one apartment
// as opposed to the list of apartments)

package com.example.alex.roomloo_v2;

import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 9/17/2015.
 */
public class ApartmentInventory extends AsyncTask<Void, Void, Void> {
//if openConnection has an issue, see this: http://alvinalexander.com/java/jdbc-connection-string-mysql-postgresql-sqlserver
    static String openConnection = "jdbc:postgresql://ec2-54-225-101-4.compute-1.amazonaws.com/dcqdqkgs3im02m" + "user=tqxazxojfiolmg&password=vaPEnRpTKdkuo8-em4U8ZroJJw"; //What is a BDD? >> "jdbc:postgresql://YOUR_HOST/YOUR_BDD?" + "user=postgres&password=YOURPASSWORD"
    private static String connectionAnswer; //seems like this should be elsewhere or is just a duplicate in general? static String connectionAnswer= "empty";
    private Context mContext; //just added post Database creation


    private static ApartmentInventory sApartmentInventory; //a singleton, i.e. a class that allows only one instance of itself to be created
    private List<Apartment> mApartmentList; //book guts this

    //to create a singleton you create a class with a private constructor and a get method,
    // if the instance already exists then get() simply returns the instance
    //see page 169
    public static ApartmentInventory get(Context context) {
        if(sApartmentInventory == null) {
            sApartmentInventory = new ApartmentInventory(context);
                }
        return sApartmentInventory;
            }
//creating our apartments listings / inventory
    private ApartmentInventory(Context context) {
        mApartmentList = new ArrayList<>();
        for (int i=0; i<4; i++) { //temporary code, need to find dynamic way to do this
            Apartment apartment = new Apartment();
            apartment.setApartmentText(getAnswerSql() );
            mApartmentList.add(apartment);
                }
        //GUTTED
        // mApartmentList = new ArrayList<>();
        //for (int i=0; i<20; i++) {
        //Apartment apartment = new Apartment();
        ///apartment.setApartmentText("Apartment #" + i);
        //mApartmentList.add(apartment);
        //        }
            }
    //for the below, recall that this is just diamond notation and equivalent to saying mApartmentList = new ArrayList<Apartment>();
    public List<Apartment> getApartmentList() {
        return mApartmentList;
            }

    public Apartment getApartment(UUID id) {
        //Gutted
        for (Apartment apartment: mApartmentList) {
            if(apartment.getId().equals(id) ) {
                return apartment;
                        }
                    }
        return null;
    }


    //code to connect to Heroku postgresql database that stores our apartment / user info

    public String getAnswerSql (){
        //execute(); >caused an error
        return connectionAnswer;
    }

    @Override
    public Void doInBackground(Void... params) {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            Class.forName("org.postgresql.Driver"); //Before you can connect to a database, you need to load the driver. This is one of the 2 standard ways
            connection = DriverManager.getConnection(openConnection); //used to get a Connection instance from JDBC; global JDBC method
            statement = connection.createStatement(); //Any time you want to issue SQL statements to the database, you require a Statement or PreparedStatement instance.
            String querySQL = "SELECT Price* FROM ApartmentInventory"; //"SELECT attribute * FROM tableName WHERE condition", Example: “SELECT price FROM ApartmentListDatabaseTable WHERE price < 4000”
            result = statement.executeQuery(querySQL); //result is an instance of ResultSet. once you issue your query it returns a ResultSet instance which contains the entire result
            //note that by default the Driver collects all the results for the query at once. You can make it fetch only a few rows however using cursors
            connectionAnswer = "";
            while (result.next()) { //before reading any values you must call next() This returns true if there is a result, but more importantly, it prepares the row for processing.
                int id = result.getInt("Price");
                connectionAnswer = connectionAnswer + id + " | " +  "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("Error: Cant connect!");
            connection = null;
        } finally {
            if (result != null) {
                try {
                    result.close(); //you must close a ResultSet (i.e. "result")
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }
        System.err.println("----- PostgreSQL query ends correctly!-----");
        return null;
    }
}

