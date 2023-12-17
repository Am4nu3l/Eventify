package com.example.eventplanner.User.User_HHP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.eventplanner.About;
import com.example.eventplanner.Adapters.HomeFragmentAdapter;
import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.R;
import com.example.eventplanner.User.User_Main;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserHHP extends AppCompatActivity {
  private TabLayout tabLayout;
  private ViewPager2 slideHome;
  FirebaseFirestore database;
  private FirebaseAuth mAuth;
  private HomeFragmentAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_hhp);
//    if(Build.VERSION.SDK_INT>=23){
//      View decore=UserHHP.this.getWindow().getDecorView();
//      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//      }
//      else{
//        decore.setSystemUiVisibility(0);
//      }
//    }
    Button back=findViewById(R.id.back);
    tabLayout = findViewById(R.id.tabLayout);
    slideHome = findViewById(R.id.viewPager);
    mAuth=FirebaseAuth.getInstance();
    database=FirebaseFirestore.getInstance();
    TextView title=findViewById(R.id.barTitle);
    String barTitle=getIntent().getStringExtra("title");
    title.setText(barTitle);
    FragmentManager fragmentManager = getSupportFragmentManager();
    adapter = new HomeFragmentAdapter(fragmentManager, getLifecycle());
    slideHome.setAdapter(adapter);
    tabLayout.addTab(tabLayout.newTab().setText("Hotel"));
    tabLayout.addTab(tabLayout.newTab().setText("Hall"));
    tabLayout.addTab(tabLayout.newTab().setText("Park"));
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        slideHome.setCurrentItem(tab.getPosition());
      }
      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }
      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
    slideHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        tabLayout.selectTab(tabLayout.getTabAt(position));
      }
    });
back.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View view) {
Intent intent=new Intent(UserHHP.this, User_Main.class);
startActivity(intent);
finish();
  }
});
    Button menu_drop = findViewById(R.id.more);
    menu_drop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(UserHHP.this,view);
        popupMenu.inflate(R.menu.topdote);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            if(menuItem.getTitle().toString().equals("Log Out")){
              Intent intent=new Intent(UserHHP.this, Login_Page.class);
              mAuth.signOut();
              startActivity(intent);
              finish();
            }
            else if(menuItem.getTitle().toString().equals("About")){
              Intent intent=new Intent(UserHHP.this, About.class);
              mAuth.signOut();
              intent.putExtra("filter","USER");
              startActivity(intent);
              finish();
            }
            return true;
          }
        });
      }
    });
  }

//  @Override
//  protected void onStart() {
//    super.onStart();
//    FirebaseUser currentUser = mAuth.getCurrentUser();
//    if(currentUser == null){
//      Intent intent = new Intent(MainActivity.this, WalkThroughPages.class);
//      startActivity(intent);
//      finish();
//    }
//  }
}

