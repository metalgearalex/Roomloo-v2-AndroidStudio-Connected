//the Fragment for your ApartmentActivity file, i.e. a specific Apartment Page

package com.example.alex.roomloo_v2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Alex on 9/18/2015.
 */
public class ApartmentFragment extends Fragment {
    //going somewhat rouge here and not following a specific part of the book. if things don't work it's because of the below
    private Apartment mApartment;
    private static final String ARG_APARTMENT_ID = "apartment_id";
    private ImageView mApartmentImageView;
    private TextView mApartmentTextView;
    private LoginButton mFbLoginButton;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private ProfileTracker mProfileTracker;
    private ImageView mFbProfilePicture;

    //old attempts at trying to compress photos. Not used, delete once pulling and compressing from database
    // private File mPhotoFile; //to store / point to the photo's location? used to convert into bitmap?
    //Bitmap mOriginalImage; //to try and compress the photo?
    //Bitmap mCompressedImage; //to try and compress the photo?
    //ByteArrayOutputStream mOutputStream; //to try and compress the photo?

    //to retrieve an extra (i.e. which apartment is this that we're showing?)
    //basically stashing the data(apartment's id) in its arguments bundle
    //To attach the arguments bundle to a fragment you call Fragment.setArguments(Bundle)
    public static ApartmentFragment newInstance (UUID apartmentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_APARTMENT_ID, apartmentId);

        ApartmentFragment fragment = new ApartmentFragment();
        fragment.setArguments(args);
        return fragment;
            }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID apartmentId = (UUID) getArguments().getSerializable(ARG_APARTMENT_ID);
        mApartment = ApartmentInventory.get(getActivity() ).getApartment(apartmentId);//former code that worked to pull up the view of one un-specific apartment --> mApartment = new Apartment();
        mCallbackManager = CallbackManager.Factory.create();

//originally was getContext() but that didn't work here. getActivity returns the activity associated with a fragment and the Activity is a context since Activity extends Context
        //remember, Activity is a context since Activity extends Context
        FacebookSdk.sdkInitialize(getActivity() );

        mCallbackManager = CallbackManager.Factory.create();
        //AccessTokenTracker is meant to track if your app has a new acess token for that user
        //seems like we can and probably should move all of this to the fragment's onCreate method tho?
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }

        };
        //ProfileTracker track's if user's changed their profile picture etc
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        //to actually use the methods above
        mAccessTokenTracker.startTracking();
        mProfileTracker.startTracking();

    }


        //this might be another possibility but setContentView requires us to extend AppCompatActivity >> setContentView(R.layout.apartment_details_page);

        //to use our PictureCompression class
        //private void updatePhotoView() {
            //if (mApartmentImageView == null) {
                //mApartmentImageView.setImageDrawable(null);
                    //}
            //else {
                //Bitmap bitmap = PictureCompression.getScaledBitmap(getResources(R.drawable.livingroom) );
                //mApartmentImageView.setImageBitmap(bitmap);
                //another option?BitmapFactory.decodeResource(Context.getResources(), R.drawable.livingroom);
                    //}
        //}


//to explicitly inflate the fragment's view. basically just calling LayoutInflater.inflate(....) and passing in the layout resource ID
//second parameter is your view's parent, usually needed to configure widgets properly
//third parameter tells the layout inflater whether to add the inflated view to the view's parent.
//We're passing in false because we're adding the view in the activity's code for more flexibility
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.apartment_details_page, container, false);
        mApartmentTextView = (TextView) v.findViewById(R.id.details_page_apartment_text);
        mApartmentTextView.setText(mApartment.getApartmentText()); //is this right?

        //trying to get a compressed image to show up
        mApartmentImageView = (ImageView) v.findViewById(R.id.details_page_apartment_picture);
        mApartmentImageView.setImageBitmap(PictureCompression.decodeSampledBitmapFromResource(getResources(), R.drawable.livingroom, 100, 100));

        //FB log-in button
        mFbLoginButton = (LoginButton) v.findViewById(R.id.login_button);
        mFbLoginButton.setReadPermissions("user_friends"); //look into this more and what this does and what else we can pull
        mFbLoginButton.setFragment(this); //this is a reference to your current fragment

        // Other app specific specialization
        mFbProfilePicture = (ImageView) v.findViewById(R.id.fbuserImage);

        // Callback registration
        //note callbackManager is defined in onCreate
        //The CallbackManager manages the callbacks into the FacebookSdk from an Activity's or Fragment's onActivityResult() method.
        //registerCallback is a public method of FB's LoginButton and registers a login callback to the given callback manager.

        mFbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            ////Using a FB SDK FacebookCallback class, the onSuccess, etc etc is pre-generated for you when you write the first line
            //LoginResult is a FB interface that shows the results of a log in operation
            //see LoginResult documentation for the methods you can use to get acesstoken, permissions, etc
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                AccessToken accessToken = loginResult.getAccessToken(); //getAccessToken is an instance method that returns the new access Token
                Profile profile = Profile.getCurrentProfile(); //see the Profile documentation


                //trying to get FB profile picture to show up
                //Reminder: Try = some code that could throw an exception
                // Reminder: Catch = the code in here is called if that exception occurs


                try {
                    URL img_value = null;
                    String fbProfileId = profile.getId();
                    img_value = new URL("http://graph.facebook.com/"+fbProfileId+"/picture?type=large");
                    Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
                    mFbProfilePicture.setImageBitmap(mIcon1);
                    //other attempts:
                    //doesn't give an error tho overall app is crashing, not sure why >> mFbProfilePicture.setProfileId(fbProfileId);
                    //one stackoverflow solution but gives me an error: mFbProfilePicture.setImageBitmap(mIcon1);
                            }
//catches here because otherwise URL line and Bitmap line produce errors
                catch (MalformedURLException e){
                    System.out.println("Error: " + e.getMessage() );
                    e.printStackTrace();
                            }
                catch (IOException ioe){
                    System.out.println("Error: " + ioe.getMessage() );
                    ioe.printStackTrace();
                            }
                    }//end of onSuccess method

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"Please log-in so you can schedule a viewing or shoot us an email",Toast.LENGTH_SHORT).show();
                    }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(),"Sorry, something seems to have gone wrong with your log-in attempt", Toast.LENGTH_SHORT).show();
                    }
        });//end of register callback method

        //one approach for compression, no compile errors so code seems right but still resulted in an "OutOfMemoryError"
            //mOriginalImage = BitmapFactory.decodeResource(getResources(), R.drawable.livingroom);
            //mOutputStream = new ByteArrayOutputStream();
            //mOriginalImage.compress(Bitmap.CompressFormat.JPEG, 0, mOutputStream); //png may be lossless tho?
            //mApartmentImageView = (ImageView) v.findViewById(R.id.details_page_apartment_picture);
            //mApartmentImageView.setImageBitmap(mOriginalImage);


        //other attempts
        // mPhotoFile = mPhotoFile.getPath(getResources().getDrawable(R.drawable.livingroom).toString());
        //updatePhotoView((Bitmap) getResources().getDrawable(R.drawable.livingroom));
          //setImageDrawable(getResources().getDrawable(R.drawable.test_image1));

        //former code to get Image to show up in list-view
        //mApartmentImageView = (ImageView) v.findViewById(R.id.details_page_apartment_picture);
        //mApartmentImageView.setImageDrawable(getResources().getDrawable(R.drawable.test_image1) );

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
            }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }


}
