package com.leontheprofessional.test.whorepresentsyou.helper;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;
import com.leontheprofessional.test.whorepresentsyou.provider.MemberContract;

import java.util.ArrayList;

/**
 * Created by Leon on 10/12/2015.
 */
public class GeneralHelper {

    private static final String LOG_TAG = GeneralHelper.class.getSimpleName();

    public static boolean isTelephonyAvailable(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isTablet(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        return (Build.VERSION.SDK_INT >= 11 &&
                (((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) ||
                        ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)));
    }

    public static boolean isZipCode(String zipcode) {
        if (zipcode != null && zipcode.length() == 5) {
            if (zipcode.matches("[0-9]+"))
                return true;
        }
        return false;
    }

    public static void markAsFavorite(Context context, MemberModel member) {
        String key = member.getPhoneNumber().replace("-", "");
        Log.v(LOG_TAG, "key, markAsFavorite: " + key);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, GeneralConstant.FAVORITE_STATUS_TRUE_STATUS_CODE).commit();

        ContentValues values = new ContentValues();
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_NAME, member.getName());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_PARTY, member.getParty());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_STATE, member.getState());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT, member.getDistrict());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_PHONE, member.getPhoneNumber());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE, member.getOfficeAddress());
        values.put(MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE, member.getLinkUrl());
        Uri insertedId = context.getContentResolver().insert(MemberContract.MemberEntry.CONTENT_URI, values);
        Log.v(LOG_TAG, "insertedId: " + insertedId.toString());
    }

    public static void cancelFavoriteStatus(Context context, MemberModel member) {
        String key = member.getPhoneNumber().replace("-", "");
        Log.v(LOG_TAG, "key, cancelFavoriteStatus: " + key);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, GeneralConstant.FAVORITE_STATUS_FALSE_STATUS_CODE).commit();

        Uri deletedUri = Uri.parse(MemberContract.MemberEntry.CONTENT_URI + "/" + member.getPhoneNumber());
        Log.v(LOG_TAG, "deletedUri: " + deletedUri.toString());
        int deletedCounts = context.getContentResolver().delete(deletedUri, null, null);
        if (deletedCounts > 1) {
            Log.w(LOG_TAG, context.getString(R.string.more_than_one_rows_deleted));
        }
        Log.v(LOG_TAG, "deletedCounts: " + deletedCounts);
    }

    public static int getFavoriteStatus(Context context, MemberModel member) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int favoriteStatusCode = sharedPreferences.getInt(member.getPhoneNumber().replace("-", ""),
                GeneralConstant.FAVORITE_STATUS_DEFAULT_STATUS_CODE);
        Log.v(LOG_TAG, "favoriteStatusCode: " + favoriteStatusCode);
        return favoriteStatusCode;
    }

    public static ArrayList<MemberModel> getAllFavoriteMembers(Context context) {
        Cursor cursor = context.getContentResolver().query(MemberContract.MemberEntry.CONTENT_URI, null, null, null, null);

        ArrayList<MemberModel> allFavoriteMembers = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_NAME));
                String party = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_PARTY));
                String state = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_STATE));
                String district = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_PHONE));
                String officeAddress = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE));
                String website = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE));

                MemberModel member = new MemberModel(name, party, state, district, phoneNumber, officeAddress, website);
                allFavoriteMembers.add(member);
                cursor.moveToNext();
            }
        }

        return allFavoriteMembers;
    }

    public static void saveMember(final Context context, MemberModel member) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_NAME, member.getName());
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_STATE, member.getState());
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT, member.getDistrict());
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_PHONE, member.getPhoneNumber());
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE, member.getOfficeAddress());
        contentValues.put(MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE, member.getLinkUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver().insert(MemberContract.MemberEntry.CONTENT_URI, contentValues);
            }
        }
        ).start();
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
}
