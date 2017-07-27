package com.leontheprofessional.test.whorepresentsyou.appwidget.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/19/2015.
 */
public class StackWidgetService extends RemoteViewsService {

    public static final String INTENT_EXTRA_POSITION_IDENTIFIER = "intent_extras_position";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final String LOG_TAG = StackRemoteViewsFactory.class.getSimpleName();

        private ArrayList<MemberModel> membersArrayList;

        private Context context;
        private int appWidgetId;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.i(LOG_TAG, "appWidgetId: " + appWidgetId);
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            membersArrayList.clear();
        }

        @Override
        public int getCount() {
            return membersArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stack_view_page);

            if (position < getCount()) {
                MemberModel member = membersArrayList.get(position);

                String name = member.getName();
                Log.v(LOG_TAG, "Member Name: " + name);
                remoteViews.setTextViewText(R.id.stackwidget_member_name, name);
                String state = member.getState();
                remoteViews.setTextViewText(R.id.stackwidget_member_state, state);
                String district = member.getDistrict();
                remoteViews.setTextViewText(R.id.stackwidget_member_district, district);
                String party = member.getParty();
                remoteViews.setTextViewText(R.id.stackwidget_member_party, party);
                String phone = member.getPhoneNumber();
                remoteViews.setTextViewText(R.id.stackwidget_member_phone, phone);
                String officeAddress = member.getOfficeAddress();
                remoteViews.setTextViewText(R.id.stackwidget_office_address, officeAddress);
                String webPageLink = member.getLinkUrl();
                remoteViews.setTextViewText(R.id.stackwidget_member_webpage_link, webPageLink);

                Bundle extras = new Bundle();
                extras.putInt(INTENT_EXTRA_POSITION_IDENTIFIER, position);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteViews.setOnClickFillInIntent(R.id.stackwidget_view, fillInIntent);
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void initData() {
            membersArrayList = new ArrayList<>();
            membersArrayList.clear();
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        membersArrayList = GeneralHelper.getAllFavoriteMembers(context);
                    }
                }).start();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
