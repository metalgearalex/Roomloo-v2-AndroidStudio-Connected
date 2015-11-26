package com.example.alex.roomloo_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.util.Date;

/**
 * Created by Alex on 9/22/2015.
 */
public class ScheduleViewingFragment extends Fragment {
    private Apartment mApartment; //to enable passing data to your Dialog Fragments
    private static final String ARG_APARTMENT_ID_SCHEDULER = "apartment_id_scheduler"; //to enable passing data to DialogFragment
    private Button mDateButton;
    private Button mTimeButton;
    private static final String DIALOG_DATE = "DialogDate"; //for DatePicker
    private static final String DIALOG_TIME = "DialogTime"; //for TimePicker
    private static final int REQUEST_DATE = 53;
    private final int REQUEST_TIME=179;
    private CallbackManager mCallbackManager_Schedule;
    private AccessToken mAccessToken_Schedule;

    //actually attaching arguments bundle to a fragment
    //here we're stashing the crime ID someplace and that someplace is in its bundle
    //in what is called its arguments (the key-value pair)
    public static ScheduleViewingFragment newInstance(int apartmentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_APARTMENT_ID_SCHEDULER, apartmentId); //string key, serializable value

        ScheduleViewingFragment fragment = new ScheduleViewingFragment();
        fragment.setArguments(args);
        return fragment;
            }


//fragments use bundles to save and retrieve their state
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to enable passing data to your Dialog Fragments - see pg 199
        int apartmentId = (int) getArguments().getSerializable(ARG_APARTMENT_ID_SCHEDULER);

        //previous code
            // mApartment = ApartmentInventory.get(getActivity() ).getApartment(apartmentId);
        new GetApartmentTask().execute();
        //because this results in a newtork on mainthread error:
            // ApiConnector apiConnector = new ApiConnector();
            //mApartment = apiConnector.getApartment(apartmentId);


        //FB related code here
        mCallbackManager_Schedule = CallbackManager.Factory.create();

//originally was getContext() but that didn't work here. getActivity returns the activity associated with a fragment and the Activity is a context since Activity extends Context
        //remember, Activity is a context since Activity extends Context
        FacebookSdk.sdkInitialize(getActivity());

        //The CallbackManager manages the callbacks into the FacebookSdk
        mCallbackManager_Schedule = CallbackManager.Factory.create();

            } //end of onCreate


    //to avoid a networkonmainthreaderror
    private class GetApartmentTask extends AsyncTask<ApiConnector,Long,Apartment > //JSONArray here specifies the type of result you'll be sending back to the main thread
    {
        //the ... is a way to tell Android you have a variable number of parameters
        //params seems to be a common way to pass in a variable type or something along the lines
        //but note that params is still just a variable name. you can call it anything
        //U can then pass multiple items and just access like params[0].. etc..
        @Override
        protected Apartment doInBackground(ApiConnector... params) {
            int apartmentId = (int) getArguments().getSerializable(ARG_APARTMENT_ID_SCHEDULER); //this results in the correct apt id
            return new ApiConnector().getApartment(apartmentId); //in debugger mApartment shows up as null here even when its working
        }

        //reminder the ApiConnector class is really a giant JSONArray
        //also the api returns the result in a variable called jsonArray
        //and we get that in a JSONArray through the doInBackground method override within GetAllCustomerTask
        //HOWEVER, SEEMS THE JSONArray/jsonArray in this whole tab is really just a placeholder the whole time and it
        //gets a real value in ListingsFragment when we call getApartmentList()
        @Override
        protected void onPostExecute(Apartment apartment) { //accepts as input the value you just returned inside doInBackground, in this case an Apartment
            mApartment = apartment; //this is where you define mApartment is the apartment that comes from the API. This is then used throughout to get/set latitude, text, etc

//may have to eventually put in getapartmentdate type method calls here. see what you did in ApartmentFragment

        }

    } // end of GetApartmentTask method



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduling_page, container, false);

        //wire up your buttons here
        //setTargetFragment(Fragment fragment, int requestCode) opens up a connection between the fragments so that you can return data back from your dialogFragments
        // to this Fragment by setting this fragment as its Target
        mDateButton = (Button) v.findViewById(R.id.date_button);
        mTimeButton = (Button) v.findViewById(R.id.time_button);
        //commenting out causes error and we already have text for it's onCreate (in the XML), i.e. What Day? and set Text as chosen by user in onActivityResult >> mDateButton.setText(mApartment.getDate().toString() ); //to display the date chosen


//the isLoggedIn method is repeated here because I encountered an edge case where if you logged out on the Apartment-Details
//Page you could still click through to schedule a viewing. So basically here we're just repeating the check to make sure
//they really are logged in
                if (isLoggedIn() ) {

                    mDateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager manager = getFragmentManager();
                            DatePickerFragment dialog = DatePickerFragment.newInstance(mApartment.getDate()); //former code DatePickerFragment dialog = new DatePickerFragment(); however new code allows us to pass date to DatePickerFragment
                            dialog.setTargetFragment(ScheduleViewingFragment.this, REQUEST_DATE);
                            dialog.show(manager, DIALOG_DATE); //displays the Dialog, /parameters = FragmentManager, String TAG
                        }
                    });


                    //commenting out causes error and we already have text for it's onCreate (in the XML), i.e. What Day? and set Text as chosen by user in onActivityResult>>  mTimeButton.setText(mApartment.getDate().toString() ); //to display the time chosen
                    mTimeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager manager = getFragmentManager();
                            TimePickerFragment dialog = TimePickerFragment.newInstance(mApartment.getDate()); //former code DatePickerFragment dialog = new DatePickerFragment(); however new code allows us to pass date to DatePickerFragment
                            dialog.setTargetFragment(ScheduleViewingFragment.this, REQUEST_TIME);
                            dialog.show(manager, DIALOG_TIME); //displays the Dialog, /parameters = FragmentManager, String TAG
                        }
                    });
                } else {
                    mDateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Please go back and Log-In First", Toast.LENGTH_LONG).show();
                        }
                    });

                    mTimeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Please go back and Log-In First", Toast.LENGTH_LONG).show();
                        }
                    });

                }


        return v;
            } //end of onCreateView

    //to check if the user is logged-in
    //Your app can only have one person at a time logged in and LoginManager sets the currentAccessToken
    // and Profile for that person. So despite what the documentation would lead you to believe access tokens are for users
    public boolean isLoggedIn() {
        mAccessToken_Schedule = AccessToken.getCurrentAccessToken(); //this is the FB method for seeing if someone is logged-in
        return mAccessToken_Schedule != null;
            }


    //to retrieve the Date the user input in your DatePicker dialog
    //reminder requestCode is used to identify which fragment is reporting back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager_Schedule.onActivityResult(requestCode, resultCode, data); //no idea if this is still necessary, but prob

        if (resultCode != Activity.RESULT_OK) {
            return;
                }

        else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mApartment.setDate(date);
            mDateButton.setText(DateFormat.format("EEEE, MMMM d, yyyyy", mApartment.getDate())); //former code that works, just trying to reformat >> mDateButton.setText(mApartment.getDate().toString() );
                }

        else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mApartment.setDate(date);
            mTimeButton.setText(DateFormat.format("h:mm a", mApartment.getDate()) ); //works but just wanted to reformat what the button shows after you set the time of your viewing>>  mTimeButton.setText(mApartment.getDate().toString() );
        }

    }


}
