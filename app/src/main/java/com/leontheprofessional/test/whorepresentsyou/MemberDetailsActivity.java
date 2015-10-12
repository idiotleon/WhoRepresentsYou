package com.leontheprofessional.test.whorepresentsyou;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

public class MemberDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MemberDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (MainActivity.CUSTOM_SEARCH_INTENT_FILTER.equals(intent.getAction())) {
            MemberModel member = getIntent()
                    .getBundleExtra(getString(R.string.bundle_identifier))
                    .getParcelable(getString(R.string.parcelable_identifier));

            TextView textViewName = (TextView) findViewById(R.id.textview_detail_activity_name);
            TextView textViewParty = (TextView) findViewById(R.id.textview_detail_activity_party);
            TextView textViewState = (TextView) findViewById(R.id.textview_detail_activity_state);
            TextView textViewDistrict = (TextView) findViewById(R.id.textview_detail_activity_district);
            TextView textViewPhone = (TextView) findViewById(R.id.textview_detail_activity_phone);
            TextView textViewOffice = (TextView) findViewById(R.id.textview_detail_activity_office);
            TextView textViewLink = (TextView) findViewById(R.id.textview_detail_activity_link);

            textViewName.setText(member.getName());
            textViewParty.setText(member.getParty());
            textViewState.setText(member.getState());
            textViewDistrict.setText(member.getDistrict());
            textViewPhone.setText(member.getPhoneNumber());
            textViewOffice.setText(member.getOfficeAddress());
            textViewLink.setText(member.getLinkUrl());
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String zipcode = intent.getStringExtra(SearchManager.QUERY);
            Log.v(LOG_TAG, "zipcode: " + zipcode);
            Intent mainActivityIntent = new Intent(MemberDetailsActivity.this, MainActivity.class);
            mainActivityIntent.setAction(MainActivity.CUSTOM_SEARCH_INTENT_FILTER);
            mainActivityIntent.putExtra(getString(R.string.search_keyword_identifier), zipcode);
            startActivity(mainActivityIntent);
        }
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
}
