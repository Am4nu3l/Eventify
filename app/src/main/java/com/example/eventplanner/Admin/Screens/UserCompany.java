package com.example.eventplanner.Admin.Screens;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.eventplanner.Adapters.CompanyAdapter;
import com.example.eventplanner.Adapters.EventAdapter;
import com.example.eventplanner.Objects.Company;
import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCompany extends Fragment {

    private CompanyAdapter adapter;
    private ArrayList<Company> company=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_user_company, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView companyRV = view.findViewById(R.id.eventRv);
        ProgressBar progressBar = view.findViewById(R.id.spin_kit);
        Drawable drawable=getContext().getDrawable(R.drawable.line);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(companyRV.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
        companyRV.setLayoutManager(layoutManager);
        companyRV.addItemDecoration(dividerItemDecoration);
        companyRV.setNestedScrollingEnabled(false);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
if(task.isSuccessful()){
    for(DocumentSnapshot snapshot:task.getResult()){
       Company singleCompany=new Company(snapshot.get("c_name").toString(),snapshot.getId(),snapshot.get("c_email").toString(),snapshot.get("c_pn1").toString(),
               snapshot.get("c_pn2").toString(),snapshot.get("password").toString(),(ArrayList<String>)snapshot.get("services"));
    company.add(singleCompany);
        Log.d("ID", "onComplete: "+snapshot.getId());
    }

    adapter = new CompanyAdapter(company);
    companyRV.setAdapter(adapter);
    adapter.notifyItemInserted(company.size() - 1);
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
                adapter = new CompanyAdapter(company);
                companyRV.setAdapter(adapter);
                adapter.notifyItemInserted(company.size() - 1);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        ImageButton search=view.findViewById(R.id.btSsearch);
        EditText searchInp=view.findViewById(R.id.inpSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new CompanyAdapter(searchByCompanyPhoneNumber(searchInp.getText().toString()));
                companyRV.setAdapter(adapter);
                adapter.notifyItemInserted(company.size() - 1);
                adapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    public ArrayList<Company> searchByCompanyPhoneNumber(String searchString)  {
        ArrayList<Company>searchResult=new ArrayList<>();
        Pattern pattern=Pattern.compile(searchString,Pattern.CASE_INSENSITIVE);
        for (int i=0; i<company.size(); i++){
            Matcher matcher=pattern.matcher(company.get(i).getPhoneNumberOne());
            boolean matchFound=matcher.find();
            if(matchFound){
                searchResult.add(company.get(i));
            }

        }
        return searchResult;
    }

}