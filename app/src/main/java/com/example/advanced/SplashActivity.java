package com.example.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageResource(R.mipmap.bigpicture);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
