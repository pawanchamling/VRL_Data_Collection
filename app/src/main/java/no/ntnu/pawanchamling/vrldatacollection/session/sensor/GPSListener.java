package no.ntnu.pawanchamling.vrldatacollection.session.sensor;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Pawan Chamling on 13/04/15.
 */
public class GPSListener implements LocationListener {

    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("@@@GPSListener", "GPS Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("@@@GPSListener", "GPS Disabled");
    }
}
