package com.mandar.localweather;



import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.IOException;


public class WeatherMainActivity extends FragmentActivity {

    Dashboard dashboard;
    RelativeLayout frameContainer;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_main);
        frameContainer = (RelativeLayout) findViewById(R.id.container);
        videoView = (VideoView) findViewById(R.id.videoView);

//        String uriPath = "android.resource://mov/raw/Best3gp";
//        Uri uri = Uri.parse(uriPath);
//        videoView.setVideoPath(uriPath);

       // setBGvid("upsea");
//
//
//        dashboard = new Dashboard();
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, dashboard)
//                    .commit();
//        }


        dashboard = new Dashboard();
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, dashboard).commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setBGvid("upsea");

    }


    public void setBGvid(String s)
    {
        String uriPath = "android.resource://" + getPackageName() + "/raw/"+s;
        Uri uri = Uri.parse(uriPath);

        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.setLooping(true);

            }
        });
        videoView.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
