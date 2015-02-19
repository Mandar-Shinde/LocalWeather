package com.mandar.localweather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class Dashboard extends Fragment {

    // Activity Parent
    WeatherMainActivity parent;

    // View Fields
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    TextView news;

    Typeface weatherFont;
    Handler handler;

    JSONObject json;

    Context con;
    double temprature;

    public Dashboard()
    {
        handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
        updateWeatherData("");
    }

    private void updateWeatherData(final String city){

        new Thread(){
            public void run(){
                json =null;
                 json = Reporter.getJSON(getActivity(), city);
                for(int i=0;i<5 && json == null ;i++)
                {
                    json = Reporter.getJSON(getActivity(), city);
                    try {
                        Thread.sleep(1000);} catch(Exception e){}
                }

                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                           // Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                            news.setText(R.string.info_internet);


                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });

                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {


            cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));


            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            temprature = main.getDouble("temp");
            temprature =  ((9*temprature)/5) + 32;//((temprature - 32)*5)/9;;//f->c
            currentTemperatureField.setText( String.format("%.2f", main.getDouble("temp"))+ " ℃  |  "  + String.format("%.2f", temprature)+ " °F" );



//
//            if(temprature>40)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_40);
//            else if(temprature>35)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_35);
//            else if(temprature>30)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_30);
//            else if(temprature>25)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_25);
//            else if(temprature>20)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_20);
//            else if(temprature>15)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_15);
//            else if(temprature>10)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_10);
//            else if(temprature>5)
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_05);
//            else
//                parent.frameContainer.setBackgroundResource(R.drawable.gradient_00);

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

           setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        boolean isnight=false;

        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
                parent.setBGvid("ship");
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
                isnight=true;
                parent.setBGvid("night");
            }

        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    if(isnight != true)
                        parent.setBGvid("rain");
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    if(isnight != true)
                        parent.setBGvid("hay");
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    if(isnight != true)
                        parent.setBGvid("hay");
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    if(isnight != true)
                        parent.setBGvid("night");
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    if(isnight != true)
                        parent.setBGvid("rain");
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    if(isnight != true)
                        parent.setBGvid("rain");
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);
        news = (TextView)rootView.findViewById(R.id.news);
        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set Background unitill dashboard is loaded
        parent.setBGvid("upsea");

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
        updateWeatherData("");
    }

    @Override
    public void onAttach(Activity activity) {
         super.onAttach(activity);

        // Setting to home activity
         parent = (WeatherMainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
