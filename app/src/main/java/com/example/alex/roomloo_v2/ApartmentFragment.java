//the Fragment for your ApartmentActivity file, i.e. a specific Apartment Page

package com.example.alex.roomloo_v2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

        //this might be another possibility but setContentView requires us to extend AppCompatActivity >> setContentView(R.layout.apartment_details_page);
            }

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

        // Callback registration
        //note callbackManager is defined in onCreate
        //The CallbackManager manages the callbacks into the FacebookSdk from an Activity's or Fragment's onActivityResult() method.
        //registerCallback is a public method of FB's LoginButton and registers a login callback to the given callback manager.
        mFbLoginButton.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"fail",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
            }
        });

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


}
