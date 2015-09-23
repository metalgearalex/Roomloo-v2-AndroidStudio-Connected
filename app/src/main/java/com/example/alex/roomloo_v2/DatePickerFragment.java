package com.example.alex.roomloo_v2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Alex on 9/22/2015.
 */
public class DatePickerFragment extends DialogFragment {


    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    public static final String EXTRA_DATE = "com.roomloo.android.date"; //does the actual name here matter?

    //to enable you to pass the Date from your ScheduleViewingFragment to your DatePickerFragment
    //may be able to delete this?
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
            }

    //overriding a method from the DialogFragment class
    //builds an AlertDialog with a title and one OK button
    //here you're just initializing the DatePicker. the book is set up so that it gets the Date from CrimeFragment. Here this is not going to happen so just set it to today's date
    //see pg 219, 226
@Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        //creating a Calendar since Date is more of a timestamp
        Calendar calendar=Calendar.getInstance(); //returns a new Gregorian Calendar.
        //NOTE: a new Gregorian Calendar constructor with no parameters is automatically initialized to the current date and time with the default Locale and TimeZone

        //commenting out prior code since no other fragment knows anything about a date so there's nothing to pass to here
        // Date date = (Date) getArguments().getSerializable(ARG_DATE);

        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        //int year = calendar.get(Calendar.YEAR);
        //int month = calendar.get(Calendar.MONTH);
        //int day = calendar.get(Calendar.DAY_OF_MONTH);

        //inflating the actual dialog when you click the schedule Date button in your Schedule Viewing Page
        View v= LayoutInflater.from(getActivity() ).inflate(R.layout.dialog_date, null);

        //using our new calendar
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        //commenting out again per the above >> mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity() )
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int year = mDatePicker.getYear();
                            int month = mDatePicker.getMonth();
                            int day = mDatePicker.getDayOfMonth();
                            Date date = new GregorianCalendar(year, month, day).getTime();
                            sendResult(Activity.RESULT_OK, date);
                                }
                    })
            .create();
    }//end of onCreateDialog


    //putting the Date on an Intent as an extra
    //this method is used in your AlertDialog Builder
    //essentially calls ScheduleViewingFragment.onActivityResult
    private void sendResult (int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
                }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
            }


}
