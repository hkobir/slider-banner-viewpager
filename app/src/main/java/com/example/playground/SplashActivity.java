package com.example.playground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(getIntent()!=null && getIntent().getData()!=null){
            Log.d("splash", "onCreate: " + getIntent().getAction());
            Log.d("splash_data", "onCreate: " + getIntent().getData());
            String urlString = getIntent().getData().toString();
            if (urlString.startsWith("callback://auth_success")) {
                Uri uri = Uri.parse(urlString);
                Toast.makeText(this, "Refreshed data for callback: "+uri.getQueryParameter("token"), Toast.LENGTH_SHORT).show();
                //then based on this callback we can refresh user payload data with user name and pass from data string encrypted query param (token)
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}