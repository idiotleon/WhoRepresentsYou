package com.leontheprofessional.test.whorepresentsyou.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Leon on 10/12/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private Context context;
    public static final String DATABASE_NAME = "wry_database";
    private static final int VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String createMemberTableQuery = "CREATE TABLE " + MemberContract.MemberEntry.TABLE_NAME + " (" +
                MemberContract.MemberEntry.COLUMN_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_NAME + " TEXT NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_STATE + " TEXT NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT + " INTEGER NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_PHONE + " TEXT NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE + " TEXT NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE + " TEXT NOT NULL, " +
                MemberContract.MemberEntry.COLUMN_MEMBER_FAVORITE_STATUS_CODE + " DEFAULT 0)";
        Log.v(LOG_TAG, "createMemberTableQuery: " + createMemberTableQuery);

        db.execSQL(createMemberTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String updateTableQuery = "DROP TABLE IF EXISTS " + MemberContract.MemberEntry.TABLE_NAME;
        db.execSQL(updateTableQuery);
    }
}
