package com.leontheprofessional.test.whorepresentsyou.google_analytics;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.leontheprofessional.test.whorepresentsyou.R;

/**
 * Created by LeonthePro7 on 12/24/2016.
 */

public class GoogleTrackingApplication extends Application {
    public Tracker tracker;

    public ContainerHolder containerHolder;
    public TagManager tagManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public TagManager tagManager() {
        if (tagManager == null) {
            tagManager = TagManager.getInstance(this);
        }

        return tagManager;
    }

    public ContainerHolder setContainerHolder() {
        return containerHolder;
    }

    public void startTracking() {

        if (tracker == null) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);

            tracker = googleAnalytics.newTracker(R.xml.track_app);

            googleAnalytics.enableAutoActivityReports(this);

            googleAnalytics.getLogger().setLogLevel(com.google.android.gms.analytics.Logger.LogLevel.VERBOSE);
        }
    }
}
