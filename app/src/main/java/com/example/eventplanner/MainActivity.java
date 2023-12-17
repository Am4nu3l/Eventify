package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.eventplanner.Admin.AdminMain;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.User.User_Main;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if(Build.VERSION.SDK_INT>=23){
//            View decore=MainActivity.this.getWindow().getDecorView();
//            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//            else{
//                decore.setSystemUiVisibility(0);
//            }
//        }
        mAuth=FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, Login_Page.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
     if(!sharedPreferences.getAll().isEmpty()){
            if(sharedPreferences.getString("user_type",null).equals("COMPANY")){
                Intent intent = new Intent(MainActivity.this, Company_Main.class);
                startActivity(intent);
                finish();
            }
            else if(sharedPreferences.getString("user_type",null).equals("USER")){
                Intent intent = new Intent(MainActivity.this, User_Main.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(MainActivity.this, AdminMain.class);
                startActivity(intent);
                finish();
            }
        }




    }
}
