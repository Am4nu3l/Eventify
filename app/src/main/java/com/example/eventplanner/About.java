package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.Admin.AdminMain;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.User.User_Main;

public class About extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    Button back=findViewById(R.id.back);
    TextView textView = findViewById(R.id.about);
    String welcomeText = getString(R.string.app_content_description);
    String filter=getIntent().getStringExtra("filter");
//    if(Build.VERSION.SDK_INT>=23){
//      View decor=About.this.getWindow().getDecorView();
//      if(decor.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//      }
//      else{
//        decor.setSystemUiVisibility(0);
//      }
//    }
    textView.setText(Html.fromHtml(welcomeText));
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(filter.equals("USER")){
          Intent intent=new Intent(About.this, User_Main.class);
          startActivity(intent);
          finish();
        }
       else if(filter.equals("COMPANY")){
          Intent intent=new Intent(About.this, Company_Main.class);
          startActivity(intent);
          finish();
        }
        else if(filter.equals("ADMIN")){
          Intent intent=new Intent(About.this, AdminMain.class);
          startActivity(intent);
          finish();
        }

      }
    });
  }
}
