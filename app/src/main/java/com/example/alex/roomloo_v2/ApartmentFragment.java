//the Fragment for your ApartmentActivity file, i.e. a specific Apartment Page

package com.example.alex.roomloo_v2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private Button mScheduleButton;
    private AccessToken mAccessToken;

    private static View view;
    private static GoogleMap mMap;
    private static Double latitude;
    private static Double longitude;
    private SupportMapFragment mSupportMapFragment;

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


        //FB related code here
//originally was getContext() but that didn't work here. getActivity returns the activity associated with a fragment and the Activity is a context since Activity extends Context
        //remember, Activity is a context since Activity extends Context
        FacebookSdk.sdkInitialize(getActivity() );

        //The CallbackManager manages the callbacks into the FacebookSdk
        mCallbackManager = CallbackManager.Factory.create();

//AccessTokenTracker is meant to track if your app has a new acesss token for that user
//Your app can only have one person at a time logged in and LoginManager sets the current AccessToken
// and Profile for that person
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateToken(AccessToken.getCurrentAccessToken() ); //doesn't seem to matter if you just pass in currentAccesstToken

            } // end of onCurrentAccessTokenChanged method

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


    //to check if the user is logged-in
    //Your app can only have one person at a time logged in and LoginManager sets the currentAccessToken
    // and Profile for that person. So despite what the documentation would lead you to believe access tokens are for users
    public boolean isLoggedIn() {
        mAccessToken = AccessToken.getCurrentAccessToken(); //this is the FB method for seeing if someone is logged-in
        return mAccessToken != null;
    }

    //method for if you're logged in, let the schedule button work, if not don't
    private void updateToken (AccessToken currentAccessToken) {
        //trying to account for edge case where log-in status changes while they're still on the apartment page
        //edge solved when you log-in but still lets you log-out then schedule a viewing if you don't leave the page

        if (isLoggedIn() ) {
            mScheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ScheduleViewingActivity.newIntent(getActivity(), mApartment.getId());
                    startActivity(intent);
                }
            });
        }
        else {
            mScheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "We need you to log-in before you can schedule a viewing", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    //to explicitly inflate the fragment's view. basically just calling LayoutInflater.inflate(....) and passing in the layout resource ID
//second parameter is your view's parent, usually needed to configure widgets properly
//third parameter tells the layout inflater whether to add the inflated view to the view's parent.
//We're passing in false because we're adding the view in the activity's code for more flexibility
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.apartment_details_page, container, false);

        //This fragment is the simplest way to place a map in an application.
        // It's a wrapper around a view of a map to automatically handle the necessary life cycle needs
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.location_map);

        if (mSupportMapFragment != null) {
//A GoogleMap must be acquired using getMapAsync(OnMapReadyCallback). This class automatically initializes the maps system and the view
// The callback method provides you with a GoogleMap instance guaranteed to be non-null and ready to be used.
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {

                //override onMapReady is auto-filled for you by Android Studio
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true); //not necessary allows all gestures (moving the camera, rotating, etc)

                        // Passing harcoded values for latitude & longitude. Need to make dynamic post integration with database
                        latitude = 40.733460;
                        longitude = -73.986400;


                        UUID apartmentId = (UUID) getArguments().getSerializable(ARG_APARTMENT_ID);
                        LatLng marker_latlng = new LatLng(ApartmentInventory.getApartmentLatitude(apartmentId), ApartmentInventory.getApartmentLongitude(apartmentId) );

                        //target = the location the camera is pointing at
                        //trying to replace with just position >> CameraPosition cameraPosition = new CameraPosition.Builder().target(marker_latlng).zoom(15.0f).build();

                        // For dropping a marker and positioning the camera (i.e. map) at a point on the Map
                        //title is what shows up when you click on the marker
                        googleMap.addMarker(new MarkerOptions().position(marker_latlng).title("Apartment").snippet("Address"));
                        //to actually auto-zoom-in on that marker
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(marker_latlng, 17); //5 is the zoom level
                        googleMap.animateCamera(yourLocation);
//commenting out some stackoverflow code because don't think we need it and seems to be messing things up
                        //
                        //googleMap.moveCamera(cameraUpdate);


                        //mMap.setMyLocationEnabled(true);
                        // For dropping a marker at a point on the Map
                        //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
                        // For zooming automatically to the Dropped PIN Location
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));

                    }

                }
            });
        }




        //setUpMapIfNeeded(); // For setting up the MapFragment
        //end of Google Maps Code in onCreateView at least

        mApartmentTextView = (TextView) v.findViewById(R.id.details_page_apartment_text);
        mApartmentTextView.setText(mApartment.getApartmentText()); //yes this works and is necessary to show the apartment text on the specific-apartment-view's page. Oddly enough, sometimes Android gets a nullpointerexception with this line, if you get it just temporarily comment out this line so you  can see what the real error is

        //trying to get a compressed image to show up
        mApartmentImageView = (ImageView) v.findViewById(R.id.details_page_apartment_picture);
        mApartmentImageView.setImageBitmap(PictureCompression.decodeSampledBitmapFromResource(getResources(), R.drawable.livingroom, 100, 100));

        //Getting a schedule Button to show up and hooked-up
        mScheduleButton = (Button) v.findViewById(R.id.schedule_button);

        //pulling up our scheduleview page upon a click of the schedule button
        //if statement added to check if a user is logged in
        //reminder getActivity is a method defined in the Android Activity class (same with onCreate etc etc)
        //here we're starting an activity from a fragment using an explicit intent and then calling Fragment.startActivity(intent)
        //specifically we're calling the right apartment to show by calling the newIntent method we defined in ScheduleViewingActivity and getting Id from our Apartment model layer as well
        updateToken(AccessToken.getCurrentAccessToken() );



        //FB log-in button
        mFbLoginButton = (LoginButton) v.findViewById(R.id.login_button);
        mFbLoginButton.setReadPermissions("user_friends"); //look into this more and what this does and what else we can pull
        mFbLoginButton.setFragment(this); //this is a reference to your current fragment

        // Other app specific specialization


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

            }//end of onSuccess method

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Please log-in so you can schedule a viewing or shoot us an email",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(),"Sorry, something seems to have gone wrong with your log-in attempt", Toast.LENGTH_SHORT).show();
            }
        });//end of register callback method

        return v;
    } //end of onCreateView


    /***** Sets up the map if it is possible to do so *****/
    //public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        //if (mSupportMapFragment == null) {
            // Try to obtain the map from the SupportMapFragment.
           // mSupportMapFragment = ((SupportMapFragment) ApartmentActivity.fragmentManager
             //       .findFragmentById(R.id.location_map)).getMapAsync(new OnMapReadyCallback() { //callback for Google maps
      //          @Override
        //        public void onMapReady(mMap) { //once set onMapReady(GoogleMap) method is triggered when the map is ready to be used and provides a non-null instance of GoogleMap
          //          setUpMap();
           //     }
      //      });

        //    if (mMap != null)

//        }
  //  }


    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
//    private static void setUpMap() {
//        // For showing a move to my loction button
//        mMap.setMyLocationEnabled(true);
//        // For dropping a marker at a point on the Map
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
////        // For zooming automatically to the Dropped PIN Location
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//                longitude), 12.0f));
//    }


//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        if (mMap != null)
//            setUpMap();


//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) MapsActivityTest.fragmentManager
//                    .findFragmentById(R.id.location_map)).getMap(); // getMap is deprecated
//            // Check if we were successful in obtaining the map.
//            if (mMap != null)
//                setUpMap();
//        }
//    }




//put this back in
    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mMap != null) {
//            ApartmentActivity.fragmentManager.beginTransaction()
//                    .remove(ApartmentActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
//            mMap = null;
//                }
//    }

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

//old attempts at trying to compress photos. Not used, delete once pulling and compressing from database
// private File mPhotoFile; //to store / point to the photo's location? used to convert into bitmap?
//Bitmap mOriginalImage; //to try and compress the photo?
//Bitmap mCompressedImage; //to try and compress the photo?
//ByteArrayOutputStream mOutputStream; //to try and compress the photo?

