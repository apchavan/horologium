package amey.clock;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class WeatherRemoteFetch {

    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    static JSONObject getJSON(Context context, String city) throws IOException, JSONException {
        URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // NOTE: 'open_weather_maps_app_id' key registered on 'https://openweathermap.org/' for your email is required below.
        connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder json = new StringBuilder(1024);
        String tmp;

        while ((tmp = reader.readLine()) != null)
            json.append(tmp).append("\n");
        reader.close();

        JSONObject data = new JSONObject(json.toString());

        if (data.getInt("cod") != 200)
            return null;

        return data;
    }
}
