package com.leontheprofessional.test.whorepresentsyou.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.activity.fragment.DisplayFragment;
import com.leontheprofessional.test.whorepresentsyou.activity.fragment.DisplayListFragment;
import com.leontheprofessional.test.whorepresentsyou.activity.fragment.adapter.CustomListFragmentAdapter;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;
import com.leontheprofessional.test.whorepresentsyou.jsonparsing.WhoRepresentsYouApi;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;
import com.leontheprofessional.test.whorepresentsyou.service.LocationTracker;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper.isZipCode;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String CUSTOM_SEARCH_INTENT_FILTER = "com.leontheprofessional.test.CustomSearchIntentFilter";

    private int ADDRESS_MAX_RESULT_NUMBER = 1;
    private int MIN_DISTANCE_CHANGE_FOR_LOCATION_UPDATES = 10;
    private int MIN_TIME_DURATION_FOR_LOCATION_UPDATES = 1000;

    private ArrayList<MemberModel> members;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private String zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        members = new ArrayList<>();

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.save_instance_state_main_activity))) {
            members = savedInstanceState.getParcelableArrayList(getString(R.string.save_instance_state_main_activity));
            refreshListFragment(members);
        } else if (GeneralHelper.isNetworkConnectionAvailable(MainActivity.this)) {
            refreshPage(getIntent());
            refreshListFragment(members);
        } else {
            showFavorite();
        }

        if (GeneralHelper.isTablet(MainActivity.this)) {
            setContentView(R.layout.fragment_main);
            Log.v(LOG_TAG, "This is a tablet");
        } else {
            setContentView(R.layout.activity_main);
            Log.v(LOG_TAG, "This is a phone");
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_textview);
            String[] states = getResources().getStringArray(R.array.state_code);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, states);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                    Bundle bundle = new Bundle();
                    bundle.put
                    DisplayListFragment displayListFragment = new DisplayListFragment();
*/
                }
            });
            refreshListFragment(members);

            Button representativeSearchButton = (Button) findViewById(R.id.btn_search_representative);
            representativeSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String keyword = autoCompleteTextView.getText().toString();
                    GeneralHelper.hideSoftKeyBoard(MainActivity.this, v);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            WhoRepresentsYouApi whoRepresentsYouApi = new WhoRepresentsYouApi();
                            try {
                                members.clear();
                                members = whoRepresentsYouApi.searchRepresentatives(MainActivity.this, keyword);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            refreshListFragment(members);
                        }
                    }.execute();
                }
            });

            Button senatorSearchButton = (Button) findViewById(R.id.btn_search_senator);
            senatorSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String keyword = autoCompleteTextView.getText().toString();
                    if (keyword == null && keyword.length() == 0) {

                    } else {
                        GeneralHelper.hideSoftKeyBoard(MainActivity.this, v);
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                WhoRepresentsYouApi whoRepresentsYouApi = new WhoRepresentsYouApi();
                                try {
                                    members.clear();
                                    members = whoRepresentsYouApi.searchSenators(MainActivity.this, keyword);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException ex) {
                                    ex.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                refreshListFragment(members);
                            }
                        }.execute();
                    }
                }
            });
        }

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "FAB clicked");
                if (GeneralHelper.isNetworkConnectionAvailable(MainActivity.this)) {

                    LocationTracker locationTracker = new LocationTracker(MainActivity.this);
                    if (locationTracker.canGetLocation()) {
                        latitude = locationTracker.getLatitude();
                        Log.v(LOG_TAG, "latitude:  " + latitude);
                        longitude = locationTracker.getLongitude();
                        Log.v(LOG_TAG, "longitude:  " + longitude);
                    } else {
                        locationTracker.showSettingsAlert();
                    }
                    locationTracker.stopUsingGPS();

                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, ADDRESS_MAX_RESULT_NUMBER);
                        if (addresses != null && addresses.size() > 0) {
                            zipcode = addresses.get(0).getPostalCode();
                            Log.v(LOG_TAG, "zipcode: " + zipcode);
                        } else {
                            Log.e(LOG_TAG, "addresses is null");
                        }

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {

                                WhoRepresentsYouApi whoRepresentsYouApi = new WhoRepresentsYouApi();
                                try {
                                    members.clear();
                                    members = whoRepresentsYouApi.getAllMemberByZipCode(MainActivity.this, zipcode);
                                    Log.v(LOG_TAG, "members: " + members.get(1).getName());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                refreshListFragment(members);
                            }
                        }.execute();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshListFragment(ArrayList<MemberModel> members) {
        DisplayListFragment displayListFragment = new DisplayListFragment();
        CustomListFragmentAdapter customListFragmentAdapter = new CustomListFragmentAdapter(MainActivity.this, members);
//        if (members != null && members.size() > 0) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.fragment_argument_identifier3), members);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main_activity, displayListFragment);
        displayListFragment.setArguments(bundle);
        displayListFragment.setListAdapter(customListFragmentAdapter);
        fragmentTransaction.commit();
/*        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            displayListFragment.setListAdapter(customListFragmentAdapter);
            fragmentTransaction.remove(displayListFragment);
            fragmentTransaction.commit();
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(getString(R.string.save_instance_state_main_activity), members);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    private void refreshPage(Intent intent) {
        Log.v(LOG_TAG, "handleIntent() executed");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            zipcode = intent.getStringExtra(SearchManager.QUERY);
        } else if (CUSTOM_SEARCH_INTENT_FILTER.equals(intent.getAction())) {
            zipcode = intent.getStringExtra(getString(R.string.search_keyword_identifier));
            Log.v(LOG_TAG, "zipcode: " + zipcode);
        }

        if (GeneralHelper.isNetworkConnectionAvailable(MainActivity.this)) {
            if (isZipCode(zipcode)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        WhoRepresentsYouApi whoRepresentsYouApi = new WhoRepresentsYouApi();
                        try {
                            members.clear();
                            members = whoRepresentsYouApi.getAllMemberByZipCode(MainActivity.this, zipcode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (GeneralHelper.isTablet(MainActivity.this)) {
                            Bundle arguments = new Bundle();
                            arguments.putParcelableArrayList(getString(R.string.fragment_argument_identifier), members);
                            DisplayFragment displayFragment = new DisplayFragment();
                            displayFragment.setArguments(arguments);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container1, displayFragment);
                            fragmentTransaction.addToBackStack(null);

                            fragmentTransaction.commit();
                        } else {
                            refreshListFragment(members);
                        }
                    }
                }.execute();
            } else {
                Log.e(LOG_TAG, "Zipcode is incorrect.");
            }
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void showFavorite() {

    }
}
