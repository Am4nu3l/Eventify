package com.example.eventplanner.Entrance.Registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class User_Registration extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user__registration, container, false);
        EditText firstName=view.findViewById(R.id.firstName);
        EditText middleName=view.findViewById(R.id.middleName);
        EditText email=view.findViewById(R.id.email);
        EditText phoneNumber=view.findViewById(R.id.phoneNumber);
        EditText confirmPassword=view.findViewById(R.id.confirmPassword);
        EditText createPassword=view.findViewById(R.id.createPassword);
        Button signUp=view.findViewById(R.id.signUp);
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        ProgressBar progressBar=view.findViewById(R.id.spin_kit);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().isEmpty()||middleName.getText().toString().isEmpty()||
                        email.getText().toString().isEmpty()||phoneNumber.getText().toString().isEmpty()||
                        confirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please Fill All The Fields!!", Toast.LENGTH_SHORT).show();
                }
                else if(!createPassword.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(getActivity(), "Password Mismatch!!", Toast.LENGTH_SHORT).show();

                }
                else{
                progressBar.setVisibility(View.VISIBLE);
                HashMap<String,String>user=new HashMap<>();
                user.put("u_fname",firstName.getText().toString());
                user.put("u_mname",middleName.getText().toString());
                user.put("u_email",email.getText().toString());
                user.put("u_phone_number",phoneNumber.getText().toString());
                user.put("c_password",confirmPassword.getText().toString());
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),confirmPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
if(task.isSuccessful()){
database.collection("User").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
    @Override
    public void onComplete(@NonNull Task<DocumentReference> task) {
if (task.isSuccessful()){
    Toast.makeText(getActivity(), "Registration completed Successfully", Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(View.GONE);
    Intent intent=new Intent(getActivity(), Login_Page.class);
    startActivity(intent);
    getActivity().finish();
}
else {
    Toast.makeText(getActivity(), "Registration interrupted"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
    progressBar.setVisibility(View.GONE);
}
    }
});
}
else {
    Toast.makeText(getActivity(), "Registration interrupted"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
    progressBar.setVisibility(View.GONE);
}
                    }
                });
}

            }
        });
        return view;
    }
}