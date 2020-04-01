package com.example.advanced.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;

public class SharedActivity extends AppCompatActivity {

    /**
     * Activity 需要使用application的context
     *
     * @param name
     * @param mode
     * @return
     */
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return this.getApplicationContext().getSharedPreferences(name, mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
    }

    private void readMySharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        for (int i = 0; i < 100; i++) {
            String key = "test:" + i;
            Log.e("test", "key:" + key + ", value:" + sharedPreferences.getInt(key, -1));
        }
    }

    private void writeMySharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 100; i++) {
            editor.putInt("test:" + i, i);
            editor.apply();
        }
    }
}
