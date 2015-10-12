package com.leontheprofessional.test.whorepresentsyou.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leontheprofessional.test.whorepresentsyou.R;

/**
 * Created by Leon on 10/12/2015.
 */
public class DetailActivity extends AppCompatActivity{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
    }
}
