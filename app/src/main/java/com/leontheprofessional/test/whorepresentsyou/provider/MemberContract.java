package com.leontheprofessional.test.whorepresentsyou.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Leon on 10/12/2015.
 */
public class MemberContract {

    public static final String CONTENT_AUTHORITY = "com.leontheprofessional.test.whorepresentsyou.Provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members_table";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_MEMBER_ID = "_id";
        public static final String COLUMN_MEMBER_NAME = "member_name";
        public static final String COLUMN_MEMBER_STATE = "member_state";
        public static final String COLUMN_MEMBER_DISTRICT = "member_district";
        public static final String COLUMN_MEMBER_PHONE = "member_phone";
        public static final String COLUMN_MEMBER_PARTY = "member_party";
        public static final String COLUMN_MEMBER_OFFICE = "member_office";
        public static final String COLUMN_MEMBER_WEBSITE = "member_website";

        public static final String Content_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String Content_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
