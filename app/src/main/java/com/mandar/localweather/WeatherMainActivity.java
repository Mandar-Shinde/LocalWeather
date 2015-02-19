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

        // Set Background unitill dashboard is loaded
        setBGvid("upsea");

        // Load Dashboard
        dashboard = new Dashboard();
        getSupportFragmentManager().beginTransaction().add(R.id.container, dashboard).commit();

    }

    // Dashboard Background Setter
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

}
