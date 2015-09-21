//this is your view for a specific Apartment's page

package com.example.alex.roomloo_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.util.UUID;

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
   private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;


    public static Intent newIntent(Context packageContext, UUID apartmentId) { //Reminder: Context argument specifies which application package the activity class can be found in
        Intent intent = new Intent(packageContext, ApartmentActivity.class);
        intent.putExtra(EXTRA_APARTMENT_ID, apartmentId); //first value of Intent.putExtra is always a String;
        return intent;
            }

//creating your view by showing your fragment
//could just be return new ApartmentFragment();
// but to call the exact Apartment the user clicked on we're calling ApartmentFragment.newInstance(UUID) (i.e. the newInstance method defined in ApartmentFragment
// the UUID we pass in is the one we retrieve from its extra
// getSerializableExtra returns the value from putExtra in our newIntent method above (so apartmentId)

    @Override
    protected Fragment createFragment() {
        UUID apartmentId = (UUID) getIntent().getSerializableExtra(EXTRA_APARTMENT_ID);
        return ApartmentFragment.newInstance(apartmentId);
            }

    //for Facebook log-in integration
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        //strayed from FB documentation here
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    }
        };
    }
//Finally you should call callbackManager.onActivityResult to pass the login results to the LoginManager via callbackManager.???

}
