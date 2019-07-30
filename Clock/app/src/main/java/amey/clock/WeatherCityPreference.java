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
