package com.wong.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luanch);

    }
    public void onMyLayoutClick(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
    public void onMySecondLayoutClick(View view){
        startActivity(new Intent(this,MainMySecondLayoutActivity.class));

    }
}
