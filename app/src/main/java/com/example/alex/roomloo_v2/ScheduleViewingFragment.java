package com.example.alex.roomloo_v2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex on 9/22/2015.
 */
public class ScheduleViewingFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate"; //for DatePicker

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduling_page, container, false);

        //wire up your buttons here


        return v;
            }

}
