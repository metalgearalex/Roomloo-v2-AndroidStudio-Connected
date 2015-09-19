package com.example.alex.roomloo_v2;

import android.support.v4.app.Fragment;

public class ListingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListingsFragment(); //maybe return ListingsFragment.newInstance() ?
    }
}
