package com.example.alex.roomloo_v2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Alex on 9/22/2015.
 */
public class ScheduleViewingActivity extends SingleFragmentActivity {

    //telling your Fragment(i.e. ApartmentFragment) which Apartment to display
// by passing the Apartment's ID as an extra when this Activitiy is started
//reminder: extra's are structured as key-value pairs
//creating an explicit intent and calling putExtra on it
//NOTE: the apartmentId actually gets a real value in our ListingsFragment class where we call this intent and the method .getId() in onClick...
//Also believe the key definition here doesn't actually matter it's just that an activity can start from different places
// so you generally use your package name to prevent name collisions with extras in other apps
    private static final String EXTRA_APARTMENT_ID ="com.example.alex.roomloo_v2.Apartment.Apartment_mId"; //if something wrong it's because this isn't the right mapping. see pg 195. also pg 199 they changed it to private


    public static Intent newIntent(Context packageContext, UUID apartmentId) { //Reminder: Context argument specifies which application package the activity class can be found in
        Intent intent = new Intent(packageContext, ScheduleViewingActivity.class);
        intent.putExtra(EXTRA_APARTMENT_ID, apartmentId); //first value of Intent.putExtra is always a String;
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return new ScheduleViewingFragment();
    }


}
