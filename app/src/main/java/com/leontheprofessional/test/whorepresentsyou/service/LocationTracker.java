package com.leontheprofessional.test.whorepresentsyou.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralConstant;

/**
 * Created by Leon on 10/13/2015.
 */
public class LocationTracker extends Service implements LocationListener {

    private static final String LOG_TAG = LocationTracker.class.getSimpleName();

    private static final float MIN_DISTANCE_CHANGE_FOR_LOCATION_UPDATES = 10f;
    private static final long MIN_TIME_DURATION_FOR_LOCATION_UPDATES = 1000;

    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    private View mLayout;

    Location location;
    double latitude;
    double longitude;

    protected LocationManager locationManager;

    public LocationTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    /**
     * As a default constructor, it has to be kept.
     */
    public LocationTracker() {
        this.mContext = getApplicationContext();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v(LOG_TAG, "isGPSEnabled: " + isGPSEnabled);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v(LOG_TAG, "isNetworkEnabled: " + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.e(LOG_TAG, "No network providers");
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.v(LOG_TAG, "ACCESS_COARSE_LOCATION Permission has been checked");
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            Log.v(LOG_TAG, "Displaying ACCESS_COARSE_LOCATION permission 01");
                            Snackbar.make(mLayout, R.string.request_permission_coarse_location, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions((Activity) mContext,
                                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                                    GeneralConstant.MY_PERMISSION_REQUST_ACCESS_COARSE_LOCATION);
                                        }
                                    })
                                    .show();
                        } else {
                            Log.v(LOG_TAG, "Displaying ACCESS_COARSE_LOCATION permission 02");
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    GeneralConstant.MY_PERMISSION_REQUST_ACCESS_COARSE_LOCATION);
                        }
                    } else {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_DURATION_FOR_LOCATION_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_LOCATION_UPDATES, this);
                        Log.v(LOG_TAG, "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                Log.v(LOG_TAG, "latitude: " + latitude);
                                longitude = location.getLongitude();
                                Log.v(LOG_TAG, "longitude: " + longitude);
                            }
                        }
                    }
                } else if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {

                            Log.v(LOG_TAG, "Displaying ACCESS_FINE_LOCATION permission");
                            Snackbar.make(mLayout, R.string.request_permission_fine_location, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions((Activity) mContext,
                                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                    GeneralConstant.MY_PERMISSION_REQUST_ACCESS_FINE_LOCATION);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    GeneralConstant.MY_PERMISSION_REQUST_ACCESS_FINE_LOCATION);
                        }
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_DURATION_FOR_LOCATION_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_LOCATION_UPDATES, this);
                            Log.d(LOG_TAG, "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    Log.v(LOG_TAG, "latitude: " + latitude);
                                    longitude = location.getLongitude();
                                    Log.v(LOG_TAG, "longitude: " + longitude);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.gps_is_setting));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.gps_setting_confirmation));

        // On pressing Settings button
        alertDialog.setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
