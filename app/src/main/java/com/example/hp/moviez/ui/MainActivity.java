package com.example.hp.moviez.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.moviez.R;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.movie_detail_container) != null) {
            // This implies that the phone is in the two pane mode hence add a detail fragment
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

}
