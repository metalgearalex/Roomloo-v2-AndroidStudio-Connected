package com.example.alex.roomloo_v2;

import android.support.v4.app.Fragment;

public class ListingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

//commenting out again
            //to try to create toolbar that i can actually format / change colors of
                //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                //setSupportActionBar(myToolbar);
            //^^ to try to create toolbar that i can actually format / change colors of

        return new ListingsFragment(); //maybe return ListingsFragment.newInstance() ?
            }

    //added for toolbar. otherwise delete this whole onCreate method
    //don't worry if savedInstanceState is null in debugger
        //onCreate is called when the activity is starting.
        // This is where most initialization should go: calling setContentView(int) to inflate the activity's UI,
        // using findViewById(int) to programmatically interact with widgets in the UI
//      @Override
//      protected void onCreate(Bundle savedInstanceState) {
//          super.onCreate(savedInstanceState);
//          Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//          setSupportActionBar(myToolbar);
//            }

}
