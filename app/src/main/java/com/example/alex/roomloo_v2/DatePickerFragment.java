package com.example.alex.roomloo_v2;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Alex on 9/22/2015.
 */
public class DatePickerFragment extends DialogFragment {

    //overriding a method from the DialogFragment class
    //builds an AlertDialog with a title and one OK button
    //see pg 219
@Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
    return new AlertDialog.Builder(getActivity() )
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok, null)
            .create();
                }

}
