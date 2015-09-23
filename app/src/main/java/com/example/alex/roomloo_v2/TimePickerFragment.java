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
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Alex on 9/22/2015.
 */
public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME = "com.roomloo.android.time"; //does the actual name here matter?

    //to enable you to pass the Date from your ScheduleViewingFragment to your DatePickerFragment
    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //overriding a method from the DialogFragment class
    //builds an AlertDialog with a title and one OK button
    //see pg 219
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        //creating a Calendar since Date is more of a timestamp
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        //inflating the actual dialog when you click the schedule Date button in your Schedule Viewing Page
        View v= LayoutInflater.from(getActivity() ).inflate(R.layout.dialog_time, null);

        //using our new calendar
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        //no way to initialize the state like we do with DatePicker? mTimePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity() )
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ////TimePicker only sets the time. The date remains the same
                                GregorianCalendar cal = new GregorianCalendar();

                                int year = cal.get(Calendar.YEAR);
                                int month = cal.get(Calendar.MONTH);
                                int day = cal.get(Calendar.DAY_OF_MONTH);

                                int hour = mTimePicker.getCurrentHour();
                                int minute = mTimePicker.getCurrentMinute();

                                //possibility? public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
                                Date date= new GregorianCalendar(year, month, day, hour, minute).getTime();

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
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


}
