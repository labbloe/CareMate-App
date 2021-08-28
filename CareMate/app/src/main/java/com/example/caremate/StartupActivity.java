package com.example.caremate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        TextView logo = (TextView) findViewById(R.id.startupLogo);

    }

    public void onClick(View v){
        Intent mainActivity = new Intent(StartupActivity.this, MainActivity.class);
        //mainActivity.putExtra("key", value); //pass parameters
        StartupActivity.this.startActivity(mainActivity);
    }
}
