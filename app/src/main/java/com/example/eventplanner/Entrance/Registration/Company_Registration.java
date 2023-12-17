package com.example.eventplanner.Entrance.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.Objects.Company;
import com.example.eventplanner.R;
import com.example.eventplanner.TermsAndConditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Company_Registration extends Fragment {
CheckBox agreeToTermsAndConditions;
TextView readTerms;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_company__registration, container, false);
        EditText companyName =view.findViewById(R.id.companyName);
        EditText email=view.findViewById(R.id.email);
       ProgressBar progressBar=view.findViewById(R.id.spin_kit);
       Button signUp=view.findViewById(R.id.signUp);
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        EditText phoneNumberOne=view.findViewById(R.id.phoneNumberOne);
        EditText phoneNumberTwo=view.findViewById(R.id.phoneNumberTwo);
        EditText createPassWord=view.findViewById(R.id.createPassword);
        CheckBox checkHotel=view.findViewById(R.id.checkHotel);
        CheckBox checkPark=view.findViewById(R.id.checkPark);
        CheckBox checkHall=view.findViewById(R.id.checkHall);
        agreeToTermsAndConditions= (CheckBox) view.findViewById(R.id.termsAndCondition);
        readTerms=view.findViewById(R.id.readTerms);
readTerms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
Intent intent=new Intent(getActivity(), TermsAndConditions.class);
startActivity(intent);
    }
});
        EditText confirmPassword=view.findViewById(R.id.confirmPassword);
    signUp.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              ArrayList<String>services=new ArrayList<>();
              if (checkHotel.isChecked()) {
                  services.add(checkHotel.getText().toString());
              }
             else if (checkPark.isChecked()) {
                  services.add(checkPark.getText().toString());
              }
              else if (checkHall.isChecked()) {
                  services.add(checkHall.getText().toString());
              }
              else{
                  Toast.makeText(getActivity(), "Please Select At least one service Type!!", Toast.LENGTH_SHORT).show();
              }
              if (companyName.getText().toString().isEmpty()||email.getText().toString().isEmpty()||
                      phoneNumberOne.getText().toString().isEmpty()||phoneNumberTwo.getText().toString().isEmpty()||
                      confirmPassword.getText().toString().isEmpty()){
                  Toast.makeText(getActivity(), "Please Fill All The Fields!!", Toast.LENGTH_SHORT).show();
              }
              else if(!createPassWord.getText().toString().equals(confirmPassword.getText().toString())){
                  Toast.makeText(getActivity(), "Password Mismatch!!", Toast.LENGTH_SHORT).show();
              }
              else {
                  progressBar.setVisibility(View.VISIBLE);
              HashMap<String,Object>company=new HashMap<>();
            Company user =
                new Company(
                    companyName.getText().toString(),
                        UUID.randomUUID().toString(),
                    email.getText().toString(),
                    phoneNumberOne.getText().toString(),
                    phoneNumberTwo.getText().toString(),
                    confirmPassword.getText().toString(),
                    services);
company.put("c_name",user.getCompanyName());
company.put("c_email",user.getEmail());
company.put("c_pn1",user.getPhoneNumberOne());
              company.put("c_pn2",user.getGetPhoneNumberTwo());
              company.put("password",user.getConfirmedPassWord());
              company.put("services",user.getServices());
            mAuth
                .createUserWithEmailAndPassword(
                    email.getText().toString(), confirmPassword.getText().toString())
                .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          database
                              .collection("Company")
                              .add(company)
                              .addOnCompleteListener(
                                  new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
Toast.makeText(getActivity(), "Registration completed Successfully", Toast.LENGTH_SHORT).show();
progressBar.setVisibility(View.GONE);
                                        Intent intent=new Intent(getActivity(), Login_Page.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                  });
                        } else {
                          progressBar.setVisibility(View.GONE);
                          Toast.makeText(getActivity(), "Registration Interrupted"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                      }
                    });}
          }
        });
       return view;
    }
}