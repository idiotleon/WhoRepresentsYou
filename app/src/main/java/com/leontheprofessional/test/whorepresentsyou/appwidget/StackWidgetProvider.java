package com.leontheprofessional.test.whorepresentsyou.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.MainActivity;
import com.leontheprofessional.test.whorepresentsyou.activity.MemberDetailsActivity;
import com.leontheprofessional.test.whorepresentsyou.appwidget.service.StackWidgetService;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.lang.reflect.Member;
import java.util.ArrayList;

/**
 * Created by Leon on 10/19/2015.
 */
public class StackWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = StackWidgetProvider.class.getSimpleName();

    private static final String DISPLAY_ACTION = "com.leontheprofessional.test.whorepresentsyou.DISPLAY_ACTION";

    private ArrayList<MemberModel> membersArrayList;
    private MemberModel member;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction() == DISPLAY_ACTION) {

            int position = intent.getExtras().getInt(StackWidgetService.INTENT_EXTRA_POSITION_IDENTIFIER);
            Log.i(LOG_TAG, "position: " + position);

            membersArrayList = GeneralHelper.getAllFavoriteMembers(context);

            member = membersArrayList.get(position);
            Log.i(LOG_TAG, "member name: " + member.getName());
            Intent displayIntent = new Intent(context, MemberDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(context.getString(R.string.parcelable_identifier), member);
            displayIntent.putExtra(context.getString(R.string.bundle_identifier), bundle);
            displayIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            displayIntent.setAction(MainActivity.CUSTOM_SEARCH_INTENT_FILTER);
            context.startActivity(displayIntent);

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.i(LOG_TAG, "Contents of appWidgetIds: " + appWidgetIds[i]);
        }

        for (int i = 0; i < appWidgetIds.length; i++) {

            // set intent for widget service that will create the views
            Intent serviceIntent = new Intent(context, StackWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stack_widget_provider_layout);
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stackwidget_provider_view, serviceIntent);
            remoteViews.setEmptyView(R.id.stackwidget_provider_view, R.id.stack_widget_empty_view);

            // set intent for item click (opens main activity)
            Intent viewIntent = new Intent(context, StackWidgetProvider.class);
            viewIntent.setAction(StackWidgetProvider.DISPLAY_ACTION);
            viewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            Log.i(LOG_TAG, "appWidgetIds[i]: " + appWidgetIds[i]);
            viewIntent.setData(Uri.parse(viewIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent viewPendingIntent = PendingIntent.getBroadcast(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.stackwidget_provider_view, viewPendingIntent);

            // update widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, context.getString(R.string.widget_deleted_confirmation), Toast.LENGTH_SHORT).show();
    }
}
