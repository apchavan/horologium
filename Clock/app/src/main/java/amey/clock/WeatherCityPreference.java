package amey.clock;

import android.app.Activity;
import android.content.SharedPreferences;

class WeatherCityPreference {

    private SharedPreferences sharedPreferences;
    //private Activity activity;

    WeatherCityPreference(Activity activity) {
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        //this.activity = activity;
    }
    /*
    public String getCity() {
        LocationForCurrentCity locationForCurrentCity = new LocationForCurrentCity(activity);
         // return sharedPreferences.getString("city", "Sydney, AU");
        return sharedPreferences.getString("city", locationForCurrentCity.getCurrentCityAndCountry());
    }
    */

    void setCity(String city) {
        /*
         * Here we avoid to use 'commit()' since it write changes to the persistent storage immediately.
         * So instead we use 'apply()' method which saves the changes but doesn't write them as quickly.
         * 'apply()' can handle it in the background.
         */

        // sharedPreferences.edit().putString("city", city).commit();
        sharedPreferences.edit().putString("city", city).apply();
    }
}
/*

class LocationForCurrentCity implements LocationListener {

    Location location;
    LocationManager locationManager;
    Activity activity;
    private static String currentCityName, currentCountryCode;

    public LocationForCurrentCity(Activity activity) {
        this.activity = activity;
    }

    private void beginLocationFetchProcess() {
        try {
            if (activity != null) {
                locationManager = (LocationManager) Objects.requireNonNull(activity).getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    onLocationChanged(location);
                } else {
                    Toast.makeText(
                            activity.getApplicationContext(),
                            R.string.someProblemToGetCurrentLocationMakeSureYourGPSIsOn_str,
                            Toast.LENGTH_LONG).show();
                }
            }   // 'if(activity != null)' closed.
        }catch (SecurityException | NullPointerException ex){
            ex.printStackTrace();
        }
    }   // 'beginLocationFetchProcess()' closed.

    private String getCurrentCityName(){
        return currentCityName;
    }

    private static String getCurrentCountryCode() {
        return currentCountryCode;
    }

    String getCurrentCityAndCountry(){
        beginLocationFetchProcess();
        return (getCurrentCityName()+","+getCurrentCountryCode());
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

            currentCityName = addresses.get(0).getLocality();
            currentCountryCode = addresses.get(0).getCountryCode();
        }catch (SecurityException | IOException ex){
            ex.printStackTrace();
        }
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
*/
