package com.example.eventplanner.Admin.Screens;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.eventplanner.Adapters.CompanyAdapter;
import com.example.eventplanner.Adapters.EventAdapter;
import com.example.eventplanner.Adapters.UserAdapter;
import com.example.eventplanner.Objects.Company;
import com.example.eventplanner.Objects.User;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPerson extends Fragment {


    private UserAdapter adapter;
private ArrayList<User>user=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view=inflater.inflate(R.layout.fragment_user_person, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView userRv = view.findViewById(R.id.eventRv);
        ProgressBar progressBar = view.findViewById(R.id.spin_kit);
        Drawable drawable=getContext().getDrawable(R.drawable.line);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(userRv.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
        userRv.setLayoutManager(layoutManager);
        userRv.addItemDecoration(dividerItemDecoration);
        userRv.setNestedScrollingEnabled(false);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
if (task.isSuccessful()){
    for (DocumentSnapshot snapshot:task.getResult()){
     User singleUser=new User(snapshot.get("u_fname").toString(),snapshot.get("u_mname").toString(),snapshot.getId(),
             snapshot.get("u_phone_number").toString(),snapshot.get("u_email").toString());
user.add(singleUser);
    }
    adapter = new UserAdapter(user);
    userRv.setAdapter(adapter);
    adapter.notifyItemInserted(user.size() - 1);
    adapter.notifyDataSetChanged();
}
else {

}
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter = new UserAdapter(user);
                userRv.setAdapter(adapter);
                adapter.notifyItemInserted(user.size() - 1);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        ImageButton search=view.findViewById(R.id.btSsearch);
        EditText searchInp=view.findViewById(R.id.inpSearch);
    search.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            adapter = new UserAdapter(searchByUserPhoneNumber(searchInp.getText().toString()));
            userRv.setAdapter(adapter);
            adapter.notifyItemInserted(user.size() - 1);
            adapter.notifyDataSetChanged();
          }
        });
        return view;
    }
    public ArrayList<User> searchByUserPhoneNumber(String searchString)  {
        ArrayList<User>searchResult=new ArrayList<>();
        Pattern pattern=Pattern.compile(searchString,Pattern.CASE_INSENSITIVE);
        for (int i=0; i<user.size(); i++){
            Matcher matcher=pattern.matcher(user.get(i).getPhoneNumber());
            boolean matchFound=matcher.find();
            if(matchFound){
                searchResult.add(user.get(i));
            }

        }
        return searchResult;
    }
}