package com.leontheprofessional.test.whorepresentsyou.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralConstant;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

public class MemberDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MemberDetailsActivity.class.getSimpleName();

    private ShareActionProvider shareActionProvider;

    private MemberModel member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        member = new MemberModel();

        if (MainActivity.CUSTOM_SEARCH_INTENT_FILTER.equals(getIntent().getAction())) {
            member = getIntent()
                    .getBundleExtra(getString(R.string.bundle_identifier))
                    .getParcelable(getString(R.string.parcelable_identifier));
        } else if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String zipcode = getIntent().getStringExtra(SearchManager.QUERY);
            Log.v(LOG_TAG, "zipcode: " + zipcode);
            Intent mainActivityIntent = new Intent(MemberDetailsActivity.this, MainActivity.class);
            mainActivityIntent.setAction(MainActivity.CUSTOM_SEARCH_INTENT_FILTER);
            mainActivityIntent.putExtra(getString(R.string.search_keyword_identifier), zipcode);
            startActivity(mainActivityIntent);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.save_instance_state_details_activity))) {
            member = savedInstanceState.getParcelable(getString(R.string.save_instance_state_details_activity));
        }

        refreshPage();
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(getString(R.string.save_instance_state_details_activity), member);
    }

/*    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }*/

    private void refreshPage() {

        TextView textViewName = (TextView) findViewById(R.id.textview_detail_name);
        TextView textViewParty = (TextView) findViewById(R.id.textview_detail_party);
        TextView textViewState = (TextView) findViewById(R.id.textview_detail_state);
        TextView textViewDistrict = (TextView) findViewById(R.id.textview_detail_district);
        TextView textViewPhone = (TextView) findViewById(R.id.textview_detail_phone);
        TextView textViewOffice = (TextView) findViewById(R.id.textview_detail_office);
        TextView textViewLink = (TextView) findViewById(R.id.textview_detail_link);

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
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        Log.e(LOG_TAG, getString(R.string.call_permission_rejected_by_user));
                        return;
                    }
                }
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
