//this is your view for a specific Apartment's page

package com.example.alex.roomloo_v2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * generates key hash for facebbok
     */
    private void GetKeyHash()
    {

        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("keyhash", "keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
            }
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }

    }

}
