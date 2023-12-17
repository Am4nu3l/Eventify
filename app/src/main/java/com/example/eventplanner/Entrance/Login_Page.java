package com.example.eventplanner.Entrance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.Admin.AdminMain;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.R;
import com.example.eventplanner.ResetPassword;
import com.example.eventplanner.User.User_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Login_Page extends AppCompatActivity {
  private FirebaseAuth mAuth;
  private FirebaseFirestore database;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_page);
//    if(Build.VERSION.SDK_INT>=23){
//      View decore=Login_Page.this.getWindow().getDecorView();
//      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//      }
//      else{
//        decore.setSystemUiVisibility(0);
//      }
//    }
    database=FirebaseFirestore.getInstance();
    mAuth=FirebaseAuth.getInstance();
    EditText email=findViewById(R.id.email);
    EditText passWord=findViewById(R.id.password);
    TextView registration=findViewById(R.id.goToRegister);
   Button signIn=findViewById(R.id.signIn);
    ProgressBar progressBar=findViewById(R.id.spin_kit);
      TextView forgotPassword=findViewById(R.id.forgotPassword);
    forgotPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(Login_Page.this, ResetPassword.class);
            startActivity(intent);
      }
    });
    signIn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (email.getText().toString().equals("") || passWord.getText().toString().equals("")) {
              Toast.makeText(Login_Page.this, "email or password field is empty", Toast.LENGTH_LONG)
                  .show();
            } else {
              progressBar.setVisibility(View.VISIBLE);
              mAuth
                  .signInWithEmailAndPassword(
                      email.getText().toString(), passWord.getText().toString())
                  .addOnCompleteListener(
                      new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> tasks) {
                          if (tasks.isSuccessful()) {
                            database
                                .collection("User")
                                .get()
                                .addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        SharedPreferences sharedPreferences =
                                            getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
                                        if (task.isSuccessful()) {
                                          for (DocumentSnapshot snapshot : task.getResult()) {
                                            if (snapshot.exists()) {
                                              if (snapshot
                                                  .get("u_email")
                                                  .equals(tasks.getResult().getUser().getEmail())) {
                                                progressBar.setVisibility(View.GONE);
                                                SharedPreferences.Editor editor =
                                                    sharedPreferences.edit();
                                                editor.putString("user_id", snapshot.getId());
                                                editor.putString(
                                                    "usrFName", snapshot.get("u_fname").toString());
                                                editor.putString(
                                                        "email", snapshot.get("u_email").toString());
                                                editor.putString(
                                                    "usrMName", snapshot.get("u_mname").toString());
                                                editor.putString(
                                                    "usrPhone",
                                                    snapshot.get("u_phone_number").toString());
                                                editor.putString("user_type", "USER");
                                                editor.commit();
                                                Intent intent =
                                                    new Intent(Login_Page.this, User_Main.class);
                                                startActivity(intent);
                                                finish();
                                              }
//                                              else {
//                                                progressBar.setVisibility(View.GONE);
//                                                Toast.makeText(
//                                                                Login_Page.this,
//                                                                "There Is No Account With This Email And Password",
//                                                                Toast.LENGTH_LONG)
//                                                        .show();
//                                              }
                                            }
                                          }
                                        }
                                      }
                                    });
                            database
                                .collection("Company")
                                .get()
                                .addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Gson gson = new Gson();
                                        SharedPreferences sharedPreferences =
                                            getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
                                        if (task.isSuccessful()) {
                                          for (DocumentSnapshot snapshot : task.getResult()) {
                                            if (snapshot.exists()) {
                                            if (snapshot
                                                .get("c_email")
                                                .equals(tasks.getResult().getUser().getEmail())) {
                                              progressBar.setVisibility(View.GONE);
                                              SharedPreferences.Editor editor =
                                                  sharedPreferences.edit();
                                              editor.putString("c_id", snapshot.getId());
                                              editor.putString(
                                                  "c_name", snapshot.get("c_name").toString());
                                              editor.putString(
                                                  "c_pn1", snapshot.get("c_pn1").toString());
                                              editor.putString(
                                                  "c_pn2", snapshot.get("c_pn2").toString());
                                              editor.putString("user_type", "COMPANY");
                                              // editor.putString("services",
                                              // snapshot.get("c_pn2").toString());
                                              ArrayList<String> eTypes =
                                                  (ArrayList<String>) snapshot.get("services");
                                              Log.d("SIZE", "onComplete: " + eTypes.size());
                                              ArrayList<String> eventTypes = new ArrayList<>();
                                              if (eTypes != null) {
                                                for (int i = 0; i < eTypes.size(); i++) {
                                                  eventTypes.add(eTypes.get(i));
                                                  Log.d("DATA", "onComplete: " + eventTypes.get(i));
                                                }
                                              }
                                              String json = gson.toJson(eventTypes);
                                              editor.putString("services", json);
                                              editor.commit();
                                              Intent intent =
                                                  new Intent(Login_Page.this, Company_Main.class);
                                              startActivity(intent);
                                              finish();
                                            }
//                                            else {
//                                              progressBar.setVisibility(View.GONE);
//                                              Toast.makeText(
//                                                              Login_Page.this,
//                                                              "There Is No Account With This Email And Password",
//                                                              Toast.LENGTH_LONG)
//                                                      .show();
//                                            }
                                            }
                                          }
                                        }
                                      }
                                    });
                            database
                                .collection("Admin")
                                .get()
                                .addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        SharedPreferences sharedPreferences =
                                            getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
                                        for (DocumentSnapshot snapshot : task.getResult()) {
                                          if (snapshot
                                              .get("email")
                                              .equals(tasks.getResult().getUser().getEmail())) {
                                            progressBar.setVisibility(View.GONE);
                                            SharedPreferences.Editor editor =
                                                sharedPreferences.edit();
                                            editor.putString("user_type", "Admin");
                                            editor.commit();
                                            Intent intent =
                                                new Intent(Login_Page.this, AdminMain.class);
                                            startActivity(intent);
                                            finish();
                                          } else {
                                            progressBar.setVisibility(View.GONE);
                                          }
                                        }
                                      }
                                    });
                          } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(
                                    Login_Page.this,
                                    tasks.getException().getMessage(),
                                    Toast.LENGTH_SHORT)
                                .show();
                          }
                        }
                      });
            }
          }
        });
    registration.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Register();
      }
    });
  }
  public void Register () {
    Intent intent = new Intent(Login_Page.this, Registration_Page.class);
    startActivity(intent);
    finish();
  }
}
