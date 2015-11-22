//this is your view for a specific Apartment's page

package com.example.alex.roomloo_v2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Alex on 9/18/2015.
 */
public class ApartmentActivity extends SingleFragmentActivity {

//telling your Fragment(i.e. ApartmentFragment) which Apartment to display
// by passing the Apartment's ID as an extra when this Activitiy is started
//reminder: extra's are structured as key-value pairs
//creating an explicit intent and calling putExtra on it
//NOTE: the apartmentId actually gets a real value in our ListingsFragment class where we call this intent and the method .getId() in onClick...
//Also believe the key definition here doesn't actually matter it's just that an activity can start from different places
// so you generally use your package name to prevent name collisions with extras in other apps
   private static final String EXTRA_APARTMENT_ID ="com.example.alex.roomloo_v2.Apartment.Apartment_mId"; //if something wrong it's because this isn't the right mapping. see pg 195. also pg 199 they changed it to private
   private static final int REQUEST_ERROR = 0; //for Google Maps error check

    public static Intent newIntent(Context packageContext, int apartmentId) { //Reminder: Context argument specifies which application package the activity class can be found in
        Intent intent = new Intent(packageContext, ApartmentActivity.class);
        intent.putExtra(EXTRA_APARTMENT_ID, apartmentId); //first value of Intent.putExtra is always a String;
        return intent;
            }


//creating your view by showing your fragment
//could just be return new ApartmentFragment();
// but to call the exact Apartment the user clicked on we're calling ApartmentFragment.newInstance(UUID) (i.e. the newInstance method defined in ApartmentFragment
// the UUID we pass in is the one we retrieve from its extra
// getSerializableExtra returns the value from putExtra in our newIntent method above (so apartmentId)
//Also creating a FragmentManager so we can setup a Google MapFragment (used in ApartmentFragment)
    public static FragmentManager fragmentManager;
    @Override
    protected Fragment createFragment() {
        int apartmentId = (int) getIntent().getSerializableExtra(EXTRA_APARTMENT_ID);
        return ApartmentFragment.newInstance(apartmentId);
            }



//verifying that Google Play Services is available and
// an up-to-date version of the Play Store is there so we can use the Google Maps API

    @Override
    protected void onResume() {
        super.onResume();
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil
                    .getErrorDialog(errorCode, this, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                    @Override
                                        public void onCancel(DialogInterface dialog) {
                                        //Leave if services are unavailable.
                                        finish();
                                                }
                            });
            errorDialog.show();
        } //end of if statement
    } //end of onResume

}
