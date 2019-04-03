package amey.clock;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TimeZone implements LocationListener {

    private static String currentTimeZone;
    private Activity activity;

    private Location location;
    private LocationManager locationManager;

    public TimeZone(Activity activity){
        this.activity = activity;
    }

    private void beginTimezoneFetchProcess(){
        try {
            if (activity != null) {
                locationManager = (LocationManager) Objects.requireNonNull(activity).getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null)
                    onLocationChanged(location);

            }   // 'if(activity != null)' closed.
        }catch (SecurityException | NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            Geocoder geocoder = new Geocoder(Objects.requireNonNull(activity).getBaseContext(), Locale.getDefault());
            List<Address> addresses =
                    geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);

            // String currentCityName = addresses.get(0).getLocality();
            String currentCountryName = addresses.get(0).getCountryName();

            currentTimeZone = currentCountryName.toUpperCase(Locale.getDefault());
        }catch (SecurityException | IOException ex){
            ex.printStackTrace();
        }
    }

    public String getCurrentTimeZone(){
        beginTimezoneFetchProcess();
        return currentTimeZone;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
