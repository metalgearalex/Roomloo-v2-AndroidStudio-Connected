package com.example.alex.roomloo_v2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Alex on 4/15/2016.
 */

public class HorizontalImageScrollerFragment extends Fragment {
    private Apartment mApartment;
    private ArrayList<String> mImageURLArraylist;
    private static final String TAG = "mImageURLArrayList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.images_to_show, container, false);
        mImageURLArraylist = getImageURLArrayList(mApartment);

        for (int z = 0; z< mImageURLArraylist.size(); z++) { //formerly used int z = 0 etc etc. recall that mImageURLArraylist = mApartment.getApartmentImageArrayList();

            ApartmentFragment apartmentFragment = new ApartmentFragment();
            container.addView(apartmentFragment.insertPhoto("http:" + mImageURLArraylist.get(z))); //container.addView(insertPhoto("http:" + mImageURLArraylist.get(z)));
            String debuggerChecker="3";
                }//end of for loop
        Log.d(TAG, "onCreateView() returned " + rootView);
        return rootView;
            } // end of onCreate View

    public ArrayList<String> getImageURLArrayList(Apartment apartment) {
        apartment = mApartment;
        return apartment.getApartmentImageArrayList();
            }

}

