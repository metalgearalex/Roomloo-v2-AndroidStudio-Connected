package com.example.alex.roomloo_v2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 9/17/2015.
 */
public class ListingsFragment extends Fragment {
    private RecyclerView mListingsRecyclerView;
    private ListingsAdapter mListingsAdapter; //used to set your Adapter

//not sure if I still need this - probably not? -- seems like no not in the final criminalintent listfragment
    //public static ListingsFragment newInstance() {
        //return new ListingsFragment();
            //}

    //creating the toolbar with the Neighborhood and Filter Buttons (i.e. menu)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //As soon as you create your RecyclerView you give it another object called LayoutManager.
    // This is required for it to work otherwise your app crashes.
    // LayoutManager handles the actual positioning of items and also defines the scrolling behavior.
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false); //fragment_listings just contains your RecyclerView

        mListingsRecyclerView = (RecyclerView) view.findViewById(R.id.listings_recycler_view);
        mListingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //there are other types of LayoutManagers available too like a GridLayoutManager

        updateUI();

        return view;
            }



    private void updateUI() {
        ApartmentInventory apartmentInventory = ApartmentInventory.get(getActivity() );
        List <Apartment> apartments = apartmentInventory.getApartmentList();

        mListingsAdapter = new ListingsAdapter(apartments);
        mListingsRecyclerView.setAdapter(mListingsAdapter);
            }


    //creating a ViewHolder, which holds onto the view
    //implements part is for making our RecyclerView / listings clickable
    public class ListingsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mListingsThumbnail;
        public Apartment mApartment; //an instance variable for that specific apartment in the list of apartment listings
        public TextView mListingsTextView;


//a method to make it cleaner to set our Text, etc to our adapter in onBindViewHolder
// (where we pass in the viewholder and its position and the adapter looks up the model data for that position
// and binds it to that ViewHolder's view)
//note most of the methods we call here don't just exist they're the methods we defined in our individual Apartment model Layer (Apartment.java)
        public void bindApartment(Apartment apartment) {
            mApartment = apartment;

            mListingsTextView.setText(mApartment.getApartmentText() );
            //doing the same thing here as above but trying to account for the ImageView / Drawable issue
            Drawable placeholderDrawable = getResources().getDrawable(R.drawable.test_image1); //see pg 431
            mListingsThumbnail.setImageDrawable(placeholderDrawable);//
                }


        public ListingsHolder (View view) {
            super(view);
            mListingsThumbnail = (ImageView) view.findViewById(R.id.list_item_apartment_picture);
            mListingsTextView = (TextView) view.findViewById(R.id.list_item_apartment_text);
            view.setOnClickListener(this);
                }

        //to make our listings clickable and bring up that apartment's detailed page
        //reminder getActivity is a method defined in the Android Activity class (same with onCreate etc etc)
        //here we're starting an activity from a fragment using an explicit intent and then calling Fragment.startActivity(intent)
        //specifically we're calling the right apartment to show by calling the newIntent method we defined in ApartmentActivity and getting Id from our Apartment model layer as well
        @Override
        public void onClick(View v) {
            Intent intent = ApartmentActivity.newIntent(getActivity(), mApartment.getId());
            startActivity(intent);
                }
    }

    //Creating an Adapter -- essentially your glue that allows you to bind underlying data to user interface elements
    //acts as middleman between data source and AdapterView Layout
    //When used with a RecyclerView the adapter is responsible for:
        // creating the necessary ViewHolders
        // Binding ViewHolders to data from the model layer
    private class ListingsAdapter extends RecyclerView.Adapter<ListingsHolder> {
        private List<Apartment> mApartments; //need to define Apartment somewhere - Apartment is your model class
        public ListingsAdapter(List<Apartment> apartments) {
            mApartments = apartments;
                }


        //SECOND step in setting up RecyclerView Adapter- create a ViewHolder (along with the ViewHolder’s view to display)
        //called by RecyclerView when it needs a new View to display an item
        //basically creating the View and wrapping it in a ViewHolder here
        @Override
        public ListingsHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity() );
            View view = layoutInflater.inflate(R.layout.fragment_listings_layout, parent, false);
            return new ListingsHolder(view);
                }

        //THIRD step in setting up RecyclerView Adapter- RecyclerView passes in a ViewHolder into this method along with the position
        // The adapter will look up the model data for that position and bind it to the ViewHolder’s view

        //binds a ViewHolder's view to your model object
        //receives the ViewHolder and a position in your data set, which is used to find the right model data
        //then update the View to reflect that model data
        @Override
        public void onBindViewHolder(ListingsHolder holder, int position) {
            Apartment apartmentPosition = mApartments.get(position);
            holder.bindApartment(apartmentPosition);
                }

        //FIRST step in setting up RecyclerView Adapter - it asks how many objects are in the list
        @Override
        public int getItemCount() {
            return mApartments.size();
                }
    }

}
