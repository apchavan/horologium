package amey.clock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import amey.clock.cities.CitiesDatabaseHelper;

/*
 Useful links :->
 https://www.androstock.com/tutorials/getting-current-location-latitude-longitude-country-android-android-studio.html
 https://github.com/martykan/forecastie
 https://www.faultinmycode.com/2018/05/open-weather-map-api-example.html
 https://codecanyon.net/category/mobile/android?_ga=2.241347524.73015043.1532934339-584464573.1532934339&referrer=search&term=weather&utf8=%E2%9C%93&view=list
 https://openweathermap.org/weather-conditions
 http://erikflowers.github.io/weather-icons/
 https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 https://stackoverflow.com/questions/22323974/how-to-get-city-name-by-latitude-longitude-in-android

 Including volley: https://inducesmile.com/android/create-a-beautiful-android-weather-app-using-openweathermap-api-and-volley-library/

 https://developer.android.com/reference/android/preference/PreferenceManager
 https://stackoverflow.com/questions/12074156/android-storing-retrieving-strings-with-shared-preferences
*/


public class WeatherFragment extends Fragment implements LocationListener {

    private final static String STORED_CITY_FILE = "storedCity";

    private static String currentCityName;
    private static String currentCountryCode;
    //    Typeface weatherFont;
    TextView cityField, updatedField, weatherIcon, detailsField, currentTemperatureField;

    FloatingActionButton changeCity_FloatingActionButton, currentCityWeather_FloatingActionButton;
    Handler handler;
    LocationManager locationManager;


    public WeatherFragment() {
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.weather_fragment, container, false);
        View view = inflater.inflate(R.layout.weather_fragment, container, false);

        cityField = view.findViewById(R.id.city_field);
        updatedField = view.findViewById(R.id.updated_field);
        detailsField = view.findViewById(R.id.details_field);
        currentTemperatureField = view.findViewById(R.id.current_temperature_field);
        weatherIcon = view.findViewById(R.id.weather_icon);

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        locationManager.getBestProvider(criteria, true);

        changeCity_FloatingActionButton = view.findViewById(R.id.change_city_floatingActionBtn);
        changeCity_FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        currentCityWeather_FloatingActionButton = view.findViewById(R.id.current_city_floatingActionBtn);
        currentCityWeather_FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        onLocationChanged(location);
                    } else {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                R.string.someProblemToGetCurrentLocationMakeSureYourGPSIsOn_str,
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (SecurityException ex) {
                    ex.printStackTrace();
                }

                changeCity(currentCityName + "," + currentCountryCode);
            }
        });

        if (
                updatedField.getVisibility() == View.VISIBLE ||
                        detailsField.getVisibility() == View.VISIBLE ||
                        currentTemperatureField.getVisibility() == View.VISIBLE ||
                        weatherIcon.getVisibility() == View.VISIBLE
                ) {
            cityField.setText(getResources().getText(R.string.cityNotAddedPleaseAddOneToShowWeatherStatus_str));
            updatedField.setVisibility(View.GONE);
            detailsField.setVisibility(View.GONE);
            currentTemperatureField.setVisibility(View.GONE);
            weatherIcon.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //updateWeatherData(new WeatherCityPreference(Objects.requireNonNull(getActivity())).getCity());

        // Check for whether any previous city was saved.

        String storedCity = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(STORED_CITY_FILE, null);
        if (storedCity != null)
            updateWeatherData(storedCity);
    }

    private void updateWeatherData(final String city) {

        new Thread() {
            @Override
            public void run() {
                //super.run();
                try {
                    final JSONObject jsonObject = WeatherRemoteFetch.getJSON(Objects.requireNonNull(getActivity()), city);
                    if (jsonObject == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), R.string.place_not_found, Toast.LENGTH_LONG).show();
                            }
                        });
                    }   // 'if' closed.
                    else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(jsonObject);

                                // Save the city using PreferenceManager
                                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(STORED_CITY_FILE, city).apply();
                            }
                        });
                    }   // 'else' closed.
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }   // Outer 'run()' closed.
        }.start();
    }   // 'updateWeatherData()' closed.

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void renderWeather(JSONObject json) {

        if (
                updatedField.getVisibility() != View.VISIBLE ||
                        detailsField.getVisibility() != View.VISIBLE ||
                        currentTemperatureField.getVisibility() != View.VISIBLE ||
                        weatherIcon.getVisibility() != View.VISIBLE
                ) {
            updatedField.setVisibility(View.VISIBLE);
            detailsField.setVisibility(View.VISIBLE);
            currentTemperatureField.setVisibility(View.VISIBLE);
            weatherIcon.setVisibility(View.VISIBLE);
        }   // 'if()' closed.

        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            detailsField.setText(details.getString("description").toUpperCase(Locale.US) + "\n" + "Humidity: " + main.getString("humidity") + "%" + "\n" + "Pressure: " + main.getString("pressure") + "hPa");
            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " ℃");

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            String updatedOn = dateFormat.format(new Date(json.getLong("dt") * 1000));

            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);

            String cityName = json.getString("name").toUpperCase(Locale.US),
                    countryName = json.getJSONObject("sys").getString("country");
            verifyAndAddCityToDB(cityName, countryName);
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            e.printStackTrace();
        }
    }

    private void verifyAndAddCityToDB(String cityName, String countryName){
        new CitiesDatabaseHelper(getContext()).insertCity(cityName,countryName);
    }

    private void setWeatherIcon(int actualID, long sunrise, long sunset) {
        int id = actualID / 100;
        String icon = "";
        if (actualID == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_sunny);
            } else {
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_clear_night);
            }
        }   // 'if (actualID == 800)' closed.
        else {
            switch (id) {
                case 2:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_drizzle);
                    break;
                case 5:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_cloudy);
                    break;

            }   // 'switch(id)' closed.
        }   // 'else' closed.
        weatherIcon.setText(icon);
    }   // 'setWeatherIcon()' closed.

    public void changeCity(String city) {
        updateWeatherData(city);
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.enter_city_name_str);
        final EditText editTextInput = new EditText(this.getContext());
        editTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editTextInput);
        builder.setPositiveButton(R.string.change_city_str_OK_btn_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeCity(editTextInput.getText().toString());

                new WeatherCityPreference(Objects.requireNonNull(getActivity())).setCity(editTextInput.getText().toString());
            }
        });
        builder.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            Geocoder geocoder = new Geocoder(Objects.requireNonNull(getActivity()).getBaseContext(), Locale.getDefault());

            List<Address> addresses =
                    geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);

            currentCityName = addresses.get(0).getLocality();
            currentCountryCode = addresses.get(0).getCountryCode();
        } catch (SecurityException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Provider enabled :)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Provider disabled :(", Toast.LENGTH_LONG).show();
    }
}
