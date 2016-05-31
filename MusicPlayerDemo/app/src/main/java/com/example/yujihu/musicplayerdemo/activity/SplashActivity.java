package com.example.yujihu.musicplayerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.yujihu.musicplayerdemo.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                startActivity(intent);
//                SplashActivity.this.finish();
//            }
//        },2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },1500);
    }
}
