package com.leontheprofessional.test.whorepresentsyou.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralConstant;
import com.leontheprofessional.test.whorepresentsyou.provider.MemberContract;

/**
 * Created by Leon on 10/12/2015.
 */
public class GeneralHelper {

    private static final String LOG_TAG = GeneralHelper.class.getSimpleName();

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
}
