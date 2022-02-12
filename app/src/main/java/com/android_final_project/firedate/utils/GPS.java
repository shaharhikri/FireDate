package com.android_final_project.firedate.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class GPS {
    private static GPS me;
    Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;

    public static GPS getMe() {
        return me;
    }

    private GPS(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void initHelper(Context context) {
        if (me == null) {
            me = new GPS(context);
        }
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }


    public Location getLastKnownLocation(){
        if (checkPermission()) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    public void getLocation(CallBack_GPS cb){
        if (checkPermission()) {

            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnSuccessListener(location -> {
                cb.getLocation(location);
            });
        }
        else {
            cb.getLocation(null);
        }
    }

    public int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        int dis = (int) Math.floor(dist);
        if (dis < 1) {
            return 1;
        }

        return dis;
    }

    public int calculateDistance(Location l1, Location l2) {
        if (l1 == null){
            Log.d("pttt", "calculateDistance: l1 is null");
        }
        if (l2 == null){
            Log.d("pttt", "calculateDistance: l2 is null");
        }
        if (l1 == null || l2 == null){
            return 300;
        }
        return calculateDistance(l1.getLatitude(), l1.getLongitude(), l2.getLatitude(), l2.getLongitude());
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public interface CallBack_GPS {
        void getLocation(Location location);
    }
}
