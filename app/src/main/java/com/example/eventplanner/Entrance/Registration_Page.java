package com.example.eventplanner.Entrance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.eventplanner.Company.Available_Events;
import com.example.eventplanner.Company.Booked_Event;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.Entrance.Registration.Company_Registration;
import com.example.eventplanner.Entrance.Registration.User_Registration;
import com.example.eventplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Registration_Page extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration_page);
    if(Build.VERSION.SDK_INT>=23){
      View decore= Registration_Page.this.getWindow().getDecorView();
      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      }
      else{
        decore.setSystemUiVisibility(0);
      }
    }
    User_Registration user_registration = new User_Registration();
    Company_Registration company_registration = new Company_Registration();
    BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setBackground(null);
    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,user_registration).commit();
    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
          case R.id.user:
            getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,user_registration).commit();
            return true;
          case R.id.company:
            getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,company_registration).commit();
            return true;
        }
        return true;
      }
    });
  }




  }

