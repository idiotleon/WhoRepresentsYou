package com.leontheprofessional.test.whorepresentsyou;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.support.v7.widget.ShareActionProvider;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.leontheprofessional.test.whorepresentsyou.helper.GeneralConstant;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.lang.reflect.Member;

public class MemberDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MemberDetailsActivity.class.getSimpleName();

    private ShareActionProvider shareActionProvider;

    private MemberModel member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        member = new MemberModel();
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

        MenuItem shareMenuItem = menu.findItem(R.id.action_share_detail_activity);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        String contactInfo = "Name: " + member.getName() + "\n" +
                "State: " + member.getState() + " District: " + member.getDistrict() + "\n" +
                "Phone: " + member.getPhoneNumber() + "\n" +
                "Office Address: " + "\n" + member.getOfficeAddress() + "\n" +
                "Website: " + member.getLinkUrl();

        shareIntent.putExtra(Intent.EXTRA_TEXT, contactInfo);

        shareActionProvider.setShareIntent(shareIntent);

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
            member = getIntent()
                    .getBundleExtra(getString(R.string.bundle_identifier))
                    .getParcelable(getString(R.string.parcelable_identifier));

            TextView textViewName = (TextView) findViewById(R.id.textview_detail_activity_name);
            TextView textViewParty = (TextView) findViewById(R.id.textview_detail_activity_party);
            TextView textViewState = (TextView) findViewById(R.id.textview_detail_activity_state);
            TextView textViewDistrict = (TextView) findViewById(R.id.textview_detail_activity_district);
            TextView textViewPhone = (TextView) findViewById(R.id.textview_detail_activity_phone);
            TextView textViewOffice = (TextView) findViewById(R.id.textview_detail_activity_office);
            TextView textViewLink = (TextView) findViewById(R.id.textview_detail_activity_link);

            CheckBox favoriteCheckBox = (CheckBox) findViewById(R.id.checkbox_favorite_star_button);

            if (GeneralConstant.FAVORITE_STATUS_TRUE_STATUS_CODE == GeneralHelper.getFavoriteStatus(MemberDetailsActivity.this, member.getName(), 0)) {
                favoriteCheckBox.setChecked(true);
            } else {
                favoriteCheckBox.setChecked(false);
            }

            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        GeneralHelper.markAsFavorite(MemberDetailsActivity.this, member);
                        Toast.makeText(MemberDetailsActivity.this, "Marked as Favorite.", Toast.LENGTH_SHORT).show();
                    } else {
                        GeneralHelper.cancelFavoriteStatus(MemberDetailsActivity.this, member);
                        Toast.makeText(MemberDetailsActivity.this, "Favorite canceled", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            textViewName.setText(member.getName());
            textViewParty.setText(member.getParty());
            textViewState.setText(member.getState());
            textViewDistrict.setText(member.getDistrict());
            final String phoneNumber = member.getPhoneNumber();
            textViewPhone.setText(phoneNumber);
            textViewPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            textViewOffice.setText(member.getOfficeAddress());
            final String linkUrl = member.getLinkUrl();
            textViewLink.setText(linkUrl);
            textViewLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(linkUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction()))

        {
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
