package com.example.eventplanner.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.eventplanner.About;
import com.example.eventplanner.Company.Available_Events;
import com.example.eventplanner.Company.Booked_Event;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.Objects.User;
import com.example.eventplanner.R;
import com.example.eventplanner.User.User_Landing.Booked_Event_U;
import com.example.eventplanner.User.User_Landing.Home;
import com.example.eventplanner.User.User_Landing.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class User_Main extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_main);
//    if(Build.VERSION.SDK_INT>=23){
//      View decore= User_Main.this.getWindow().getDecorView();
//      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//      }
//      else{
//        decore.setSystemUiVisibility(0);
//      }
//    }
    BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setBackground(null);
    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, new Home()).commit();
    //the bottom navigation bar for the user starts here and ends on line 75
    bottomNavigationView.setOnItemSelectedListener(
        new NavigationBarView.OnItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.home:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeContainer, new Home())
                    .commit();
                return true;
              case R.id.booked:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeContainer, new Booked_Event_U())
                    .commit();
                return true;
              case R.id.profile:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeContainer, new Profile())
                    .commit();
                return true;
            }
            return true;
          }
        });
    //the top right corner the three dots menu are implemented here
    Button menu_drop = findViewById(R.id.more);
    menu_drop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(User_Main.this,view);
        popupMenu.inflate(R.menu.topdote);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            if(menuItem.getTitle().toString().equals("Log Out")){
              Intent intent=new Intent(User_Main.this, Login_Page.class);
              mAuth.signOut();
              startActivity(intent);
              finish();
            }
            else if(menuItem.getTitle().toString().equals("About")){
              Intent intent=new Intent(User_Main.this, About.class);
              mAuth.signOut();
              intent.putExtra("filter","USER");
              startActivity(intent);
              finish();            }
            return true;
          }
        });
      }
    });
  }



}
