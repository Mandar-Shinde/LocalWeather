package com.mandar.localweather;



import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.VideoView;

import java.io.IOException;


public class WeatherMainActivity extends FragmentActivity
{

    Dashboard dashboard;
    FrameLayout frameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        frameContainer = (FrameLayout)findViewById(R.id.container);


        dashboard= new Dashboard();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,dashboard)
                    .commit();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        getSupportFragmentManager().beginTransaction()
                .remove(dashboard)
                .commit();


        dashboard= new Dashboard();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,dashboard)
                    .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
 //       getMenuInflater().inflate(R.menu.menu_weather_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
