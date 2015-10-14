package com.leontheprofessional.test.whorepresentsyou.helper;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;
import com.leontheprofessional.test.whorepresentsyou.provider.MemberContract;

/**
 * Created by Leon on 10/12/2015.
 */
public class GeneralHelper {

    private static final String LOG_TAG = GeneralHelper.class.getSimpleName();

    public static boolean isTelephonyAvailable(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager!=null && telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY;
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
        String key = member.getName();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, GeneralConstant.FAVORITE_STATUS_TRUE_STATUS_CODE).commit();

        changeFavoriteStatusCode(context, member, GeneralConstant.FAVORITE_STATUS_TRUE_STATUS_CODE);
    }


    public static void cancelFavoriteStatus(Context context, MemberModel member) {
        String key = member.getName();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, GeneralConstant.FAVORITE_STATUS_FALSE_STATUS_CODE).commit();

        changeFavoriteStatusCode(context, member, GeneralConstant.FAVORITE_STATUS_FALSE_STATUS_CODE);
    }

    public static void changeFavoriteStatusCode(Context context, MemberModel member, int statusCode) {
        String memberName = member.getName();
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues updatedValue = new ContentValues();
        // The only row that has to be updated
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_FAVORITE_STATUS_CODE, statusCode);
        // The other rows remain the same
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_NAME, member.getName());
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_STATE, member.getState());
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT, member.getDistrict());
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_PHONE, member.getPhoneNumber());
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE, member.getOfficeAddress());
        updatedValue.put(MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE, member.getLinkUrl());
/*        int updateCount = contentResolver.update(MovieInfoProviderContract.GeneralMovieInfoEntry.CONTENT_URI,
                updatedValue, MovieInfoProviderContract.GeneralMovieInfoEntry.MOVIE_COLUMN_ID + " = ?",
                new String[]{movieId});*/
        Uri updateUri = Uri.parse(MemberContract.MemberEntry.TABLE_NAME + "/" + memberName);
        int updateCount = contentResolver.update(updateUri,
                updatedValue, null, null);
        Log.v(LOG_TAG, "updateCount, changeFavoriteStatusCode(Context context, String key, int statusCode): " + updateCount);
        Log.v(LOG_TAG, "key, changeFavoriteStatusCode(Context context, String key, int statusCode): " + memberName);
    }

    public static int getFavoriteStatus(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void saveMembers(final Context context, MemberModel member) {
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
