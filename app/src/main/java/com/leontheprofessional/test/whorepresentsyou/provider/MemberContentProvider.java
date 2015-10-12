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

/**
 * Created by Leon on 10/12/2015.
 */
public class MemberContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static final int MEMBER = 100;
    private static final int MEMBERS = 101;

    private static final UriMatcher uriMathcer;

    static {
        uriMathcer = new UriMatcher(UriMatcher.NO_MATCH);
        uriMathcer.addURI(MemberContract.CONTENT_AUTHORITY,
                MemberContract.MemberEntry.TABLE_NAME, MEMBER);
        uriMathcer.addURI(MemberContract.CONTENT_AUTHORITY,
                MemberContract.MemberEntry.TABLE_NAME + "/#", MEMBERS);
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

        return insertedId;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        database = databaseHelper.getWritableDatabase();

        String name;

        int deletedCount = 0;

        switch (uriMathcer.match(uri)) {
            case MEMBER:
                name = uri.getPathSegments().get(1);
                selection = MemberContract.MemberEntry.COLUMN_MEMBER_NAME + " = ?";
                selectionArgs = new String[]{name};
                deletedCount = database.delete(MemberContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MEMBERS:
                deletedCount = database.delete(MemberContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }

        return deletedCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String name;
        int updatedRowsCount = 0;
        switch (uriMathcer.match(uri)) {
            case MEMBERS:
                updatedRowsCount = database.update(MemberContract.MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMBER:
                name = uri.getPathSegments().get(1);
                selection = MemberContract.MemberEntry.COLUMN_MEMBER_NAME + " = ?";
                selectionArgs = new String[]{name};
                updatedRowsCount = database.update(MemberContract.MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRowsCount;
    }
}
