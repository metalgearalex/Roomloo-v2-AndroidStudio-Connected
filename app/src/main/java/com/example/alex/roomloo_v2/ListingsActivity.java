package com.example.alex.roomloo_v2;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

public class ListingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        //to try to create toolbar that i can actually format / change colors of
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //^^ to try to create toolbar that i can actually format / change colors of

        return new ListingsFragment(); //maybe return ListingsFragment.newInstance() ?
            }

}
