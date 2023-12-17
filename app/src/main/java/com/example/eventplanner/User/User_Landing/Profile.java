package com.example.eventplanner.User.User_Landing;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.R;
import com.google.firebase.auth.FirebaseAuth;


public class Profile extends Fragment {
    TextView name,email,phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences =getActivity().
                getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
       name=view.findViewById(R.id.textName);
       email=view.findViewById(R.id.textEmail);
       phoneNumber=view.findViewById(R.id.textPhoneNumber);
       name.setText(sharedPreferences.getString("usrFName",null)+"\t"+sharedPreferences.getString("usrMName",null));
        email.setText(sharedPreferences.getString("email",null));
        phoneNumber.setText(sharedPreferences.getString("usrPhone",null));
      Button singOut=view.findViewById(R.id.btnSignOut);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
singOut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
mAuth.signOut();
        Intent intent=new Intent(getActivity(), Login_Page.class);
        startActivity(intent);
        getActivity().finish();
    }
});

        return view;
    }


}