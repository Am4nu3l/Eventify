package com.example.eventplanner.Company;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.eventplanner.Adapters.BookedEventRowAdapter;
import com.example.eventplanner.BookedDescription;
import com.example.eventplanner.Objects.BookedEvent;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Booked_Event extends Fragment {
    private   BookedEventRowAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_booked__event, container, false);
        ListView listView = view.findViewById(R.id.listView);
        ArrayList<BookedEvent> eventToBeShown = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_IDENTITY", Context.MODE_PRIVATE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        progressBar=view.findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);
        database.collection("BookedEvent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        try {
                            Log.d("TAG", "onComplete: "+sharedPreferences.getString("c_id", null));
                            if (sharedPreferences.getString("c_id", null).equals(snapshot.get("e_providerId"))) {
                                BookedEvent event = new BookedEvent(snapshot.get("e_booked_by").toString(),
                                        snapshot.get("e_booked_size").toString(),
                                        snapshot.get("e_availableAt").toString(),
                                        snapshot.get("e_booked_date").toString(),
                                        Uri.parse(String.valueOf(snapshot.get("e_image_url"))),snapshot.get("e_id").toString());
                                eventToBeShown.add(event);
                            }
                        } catch (Exception e) {
                            // Handle exception when retrieving data from the document snapshot
                            e.printStackTrace();
                        }
                    }
                    adapter = new BookedEventRowAdapter(eventToBeShown);
                    listView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Handle exception when task is not successful
                    Exception exception = task.getException();
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure when retrieving the collection
                Log.d("ERR", "onFailure: "+e.getMessage());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), BookedDescription.class);
                intent.putExtra("eventId",adapter.getData().get(i).getEventId());
                intent.putExtra("filter","company");
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}