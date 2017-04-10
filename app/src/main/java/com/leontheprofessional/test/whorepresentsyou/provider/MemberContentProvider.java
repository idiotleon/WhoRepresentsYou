package com.leontheprofessional.test.whorepresentsyou.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Leon on 10/12/2015.
 */
public class MemberContentProvider extends ContentProvider {

    private static final String LOG_TAG = MemberContentProvider.class.getSimpleName();

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static final int MEMBERS = 100;
    private static final int MEMBER = 101;

    private static final UriMatcher uriMathcer;

    static {
        uriMathcer = new UriMatcher(UriMatcher.NO_MATCH);
        uriMathcer.addURI(MemberContract.CONTENT_AUTHORITY,
                MemberContract.MemberEntry.TABLE_NAME, MEMBERS);
        uriMathcer.addURI(MemberContract.CONTENT_AUTHORITY,
                MemberContract.MemberEntry.TABLE_NAME + "/*", MEMBER);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        database = databaseHelper.getReadableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMathcer.match(uri)) {
            case MEMBERS:
                queryBuilder.setTables(MemberContract.MemberEntry.TABLE_NAME);
                break;
            case MEMBER:
                String memberName = uri.getPathSegments().get(1);
                queryBuilder.setTables(MemberContract.MemberEntry.TABLE_NAME);
                queryBuilder.appendWhere(MemberContract.MemberEntry.COLUMN_MEMBER_NAME + "=" + memberName);
                break;
        }

        Log.v(LOG_TAG, "queryBuilder: " + queryBuilder.toString());
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, groupBy, having, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMathcer.match(uri)) {
            case MEMBERS:
                return MemberContract.MemberEntry.Content_TYPE;
            case MEMBER:
                return MemberContract.MemberEntry.Content_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        database = databaseHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id;
        Uri insertedId = null;
        switch (uriMathcer.match(uri)) {
            default:
                id = database.insert(MemberContract.MemberEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(MemberContract.MemberEntry.CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(insertedId, null);
                }
        }

        Log.v(LOG_TAG, "insertedId: " + insertedId.toString());
        return insertedId;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "delete() executed.");
        database = databaseHelper.getWritableDatabase();

        String telephoneNumber;

        int deletedCount = 0;

        switch (uriMathcer.match(uri)) {
            case MEMBER:
                Log.v(LOG_TAG, "uri, delete(): " + uri.toString());
                telephoneNumber = uri.getPathSegments().get(1);
                selection = MemberContract.MemberEntry.COLUMN_MEMBER_PHONE + " = ?";
                Log.v(LOG_TAG, "selection, delete(): " + selection);
                selectionArgs = new String[]{telephoneNumber};
                Log.v(LOG_TAG, "selectionArgs, delete(): " + selectionArgs[0]);
                deletedCount = database.delete(MemberContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MEMBERS:
                Log.v(LOG_TAG, "uri MEMBERS selected");
                deletedCount = database.delete(MemberContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }

        Log.v(LOG_TAG, "deletedCount: " + deletedCount);
        return deletedCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String telephoneNumber;
        int updatedRowsCount = 0;
        switch (uriMathcer.match(uri)) {
            case MEMBERS:
                updatedRowsCount = database.update(MemberContract.MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMBER:
                telephoneNumber = uri.getPathSegments().get(1);
                selection = MemberContract.MemberEntry.COLUMN_MEMBER_PHONE + " = ?";
                selectionArgs = new String[]{telephoneNumber};
                updatedRowsCount = database.update(MemberContract.MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRowsCount;
    }
}
