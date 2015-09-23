package com.example.alex.roomloo_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;
import java.util.UUID;

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


    //actually attaching arguments bundle to a fragment
    //here we're stashing the crime ID someplace and that someplace is in its bundle
    //in what is called its arguments (the key-value pair)
    public static ScheduleViewingFragment newInstance(UUID apartmentId) {
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
        UUID apartmentId = (UUID) getArguments().getSerializable(ARG_APARTMENT_ID_SCHEDULER);
        mApartment = ApartmentInventory.get(getActivity() ).getApartment(apartmentId);
            }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduling_page, container, false);

        //wire up your buttons here
        //setTargetFragment(Fragment fragment, int requestCode) opens up a connection between the fragments so that you can return data back from your dialogFragments
        // to this Fragment by setting this fragment as its Target
        mDateButton = (Button) v.findViewById(R.id.date_button);
        //commenting out causes error and we already have text for it's onCreate (in the XML), i.e. What Day? and set Text as chosen by user in onActivityResult >> mDateButton.setText(mApartment.getDate().toString() ); //to display the date chosen
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mApartment.getDate()); //former code DatePickerFragment dialog = new DatePickerFragment(); however new code allows us to pass date to DatePickerFragment
                dialog.setTargetFragment(ScheduleViewingFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE); //displays the Dialog, /parameters = FragmentManager, String TAG
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.time_button);
        //commenting out causes error and we already have text for it's onCreate (in the XML), i.e. What Day? and set Text as chosen by user in onActivityResult>>  mTimeButton.setText(mApartment.getDate().toString() ); //to display the time chosen
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mApartment.getDate() ); //former code DatePickerFragment dialog = new DatePickerFragment(); however new code allows us to pass date to DatePickerFragment
                dialog.setTargetFragment(ScheduleViewingFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME); //displays the Dialog, /parameters = FragmentManager, String TAG
            }
        });

        return v;
            } //end of onCreateView

    //to retrieve the Date the user input in your DatePicker dialog
    //reminder requestCode is used to identify which fragment is reporting back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
