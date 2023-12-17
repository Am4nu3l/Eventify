package com.example.eventplanner.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.eventplanner.About;
import com.example.eventplanner.Adapters.AvailableEventAdapter;
import com.example.eventplanner.Admin.AdminBookedEvents;
import com.example.eventplanner.Admin.AdminHome;
import com.example.eventplanner.Admin.AdminMain;
import com.example.eventplanner.Admin.Users;
import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Company_Main extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_company_main);
    if(Build.VERSION.SDK_INT>=23){
      View decore=Company_Main.this.getWindow().getDecorView();
      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      }
      else{
        decore.setSystemUiVisibility(0);
      }
    }
    BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setBackground(null);
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.homeContainer, new Available_Events())
        .commit();
    bottomNavigationView.setOnItemSelectedListener(
        new NavigationBarView.OnItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.availableEvents:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeContainer, new Available_Events())
                    .commit();
                return true;
              case R.id.bookedEvents:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeContainer, new Booked_Event())
                    .commit();
                return true;
            }
            return true;
          }
        });
    Button menu_drop = findViewById(R.id.more);
    menu_drop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(Company_Main.this,view);
        popupMenu.inflate(R.menu.topdote);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            if(menuItem.getTitle().toString().equals("Log Out")){
              Intent intent=new Intent(Company_Main.this, Login_Page.class);
              mAuth.signOut();
              startActivity(intent);
              finish();
            }
            else if(menuItem.getTitle().toString().equals("About")){
              Intent intent=new Intent(Company_Main.this, About.class);
              mAuth.signOut();
              intent.putExtra("filter","COMPANY");
              startActivity(intent);
              finish();            }
            return true;
          }
        });
      }
    });
  }



}
