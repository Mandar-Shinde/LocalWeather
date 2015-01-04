package com.mandar.localweather;

/**
 * Created by mandar on 01-Jan-15.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Reporter implements LocationListener {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?";//"http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    static boolean isGPSEnabled = false;
    static boolean isNetworkEnabled = false;
    static Location location = null;
    static float latitude;
    static float longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected LocationManager locationManager;
    private Location m_Location;

    public static JSONObject getJSON(Context context, String city){
        try {

            //Declaring a Location Manager
            LocationManager locationManager;

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            StringBuilder useurl = new StringBuilder();

            if (!isGPSEnabled && !isNetworkEnabled)
            {
                useurl.append(OPEN_WEATHER_MAP_API).append("q=").append(city).append("&units=metric");
            }
            else
            {

                if (isNetworkEnabled) {

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = (float)  location.getLatitude();
                            longitude= (float)location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {

                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude =(float)  location.getLatitude();
                                longitude=(float) location.getLongitude();
                            }
                        }
                    }
                }
                useurl.append(OPEN_WEATHER_MAP_API).append("lat=").append(String.valueOf(latitude)).append("&lon=").append(String.valueOf(longitude)).append("&units=metric");
            }



            URL url = new URL(useurl.toString());
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();


            connection.addRequestProperty("x-api-key","e0bb0993b225c240f2ce1380e93bb19c");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
