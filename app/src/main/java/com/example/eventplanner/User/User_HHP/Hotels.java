package com.example.eventplanner.User.User_HHP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eventplanner.Adapters.AvailableEventAdapter;
import com.example.eventplanner.Adapters.EventAdapter;
import com.example.eventplanner.Adapters.RecyclerTouchListener;
import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.R;
import com.example.eventplanner.User.EventDescriptionAndBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hotels extends Fragment {
private EventAdapter adapter;
private EditText searchInput;
private ImageButton searchButton;
  private   ArrayList<Event>filteredEvents;
    private ArrayList<String> decors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_hotels, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView myEvents = view.findViewById(R.id.eventRv);
        ProgressBar progressBar = view.findViewById(R.id.spin_kit);
        Drawable drawable=getContext().getDrawable(R.drawable.line);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myEvents.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        myEvents.setLayoutManager(layoutManager);
        myEvents.addItemDecoration(dividerItemDecoration);
        myEvents.setNestedScrollingEnabled(false);
        ArrayList<Event> singleEventContainer=new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        searchButton=view.findViewById(R.id.btSsearch);
        searchInput=view.findViewById(R.id.inpSearch);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);
        String filter=sharedPreferences.getString("title",null);
        database.collection("Event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        //get the event types
                            ArrayList<String> eTypes = (ArrayList<String>) snapshot.get("e_type");
                            Log.d("SIZE", "onComplete: "+eTypes.size());
                            if (eTypes != null) {
                                ArrayList<String> eventTypes=new ArrayList<>();
                              //  eventTypes.clear();
                                for (int i=0; i<eTypes.size(); i++) {
                                    eventTypes.add(eTypes.get(i));
                                    Log.d("DATA", "onComplete: "+eventTypes.get(i));
                                }
                                ArrayList<String> decor = (ArrayList<String>) snapshot.get("e_decor");
                                //load decor images
                                if (decor != null) {
                                    decors=new ArrayList<>();
                                    //  eventTypes.clear();
                                    for (int i=0; i<decor.size(); i++) {
                                        decors.add(decor.get(i));
                                    }
                                }
                                Log.d("DATA", "-----------------------");
                                //create event object
                                Event event=new Event(eventTypes,snapshot.get("e_providerName").toString(),
                                        snapshot.get("e_provider_pn_number").toString() , snapshot.get("e_description").toString(),snapshot.get("e_availableAt").toString()
                                        ,snapshot.get("e_hourOfTheDay").toString(), Uri.parse(snapshot.get("e_image_url").toString()),snapshot.get("e_size").toString()
                                        ,snapshot.get("e_id").toString(),snapshot.get("e_providerId").toString(), (ArrayList<String>) snapshot.get("services"));
                                singleEventContainer.add(event);

                            }
                    }
                    ArrayList<Event>filteredServiceEvents=new ArrayList<>();
//service filter loop
                    for (int i=0; i<singleEventContainer.size(); i++){
                       for(int j=0; j<singleEventContainer.get(i).getServiceProvided().size(); j++){
                           if(singleEventContainer.get(i).getServiceProvided().get(j).equals("Hotel")){
                               filteredServiceEvents.add(singleEventContainer.get(i));
                           }
                       }
                    }
                    Log.d("FIL", "onComplete: "+filter);
                    filteredEvents=new ArrayList<>();
                    for (int i=0; i<filteredServiceEvents.size(); i++){
                        for(int j=0; j<filteredServiceEvents.get(i).getEventType().size(); j++){
                            if(filteredServiceEvents.get(i).getEventType().get(j).equals(filter)){
                                filteredEvents.add(filteredServiceEvents.get(i));
                            }
                            else{
                                Log.d("TAD", "onComplete: "+"no data loaded");
                            }
                        }
                    }
                    adapter = new EventAdapter(filteredEvents);
                    myEvents.setAdapter(adapter);
                    adapter.notifyItemInserted(singleEventContainer.size() - 1);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                   // eventTypes.clear();
                }
                else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchInput.getText().toString().contains("/")){
                    adapter = new EventAdapter(searchEvents(searchInput.getText().toString()));
                }
                else {
                    adapter = new EventAdapter(searchEventsByName(searchInput.getText().toString()));
                }
                myEvents.setAdapter(adapter);
                adapter.notifyItemInserted(singleEventContainer.size() - 1);
                adapter.notifyDataSetChanged();
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        adapter = new EventAdapter(filteredEvents);
        myEvents.setAdapter(adapter);
        adapter.notifyItemInserted(singleEventContainer.size() - 1);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
});
        myEvents.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                myEvents,new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getActivity(), EventDescriptionAndBooking.class);
                intent.putExtra("EventProviderName",adapter.getItems().get(position).getEventProviderName());
                intent.putExtra("EventProviderPhoneNumber",adapter.getItems().get(position).getEventProviderPhoneNumber());
                intent.putExtra("EventDate",adapter.getItems().get(position).getEventAvailableAt());
                intent.putExtra("EventTime",adapter.getItems().get(position).getEventHourOfTheDay());
                intent.putExtra("EventSize",adapter.getItems().get(position).getEventSizeInRange());
                intent.putExtra("EventType",sharedPreferences.getString("title",null));
                intent.putExtra("EventImage",String.valueOf(adapter.getItems().get(position).getImageUrl()));
                intent.putExtra("DecorImageOne",String.valueOf(decors.get(0)));
                intent.putExtra("DecorImageTwo",String.valueOf(decors.get(1)));
                intent.putExtra("DecorImageThree",String.valueOf(decors.get(2)));
                intent.putExtra("EventDescription",adapter.getItems().get(position).getEventDescription());
                intent.putExtra("EventId",adapter.getItems().get(position).getEventId());
                intent.putExtra("EventProviderId",adapter.getItems().get(position).getEventProviderId());
                intent.putExtra("EventWhereToBeHeld","Hotel");
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }
    //searh algorithm
public ArrayList<Event> searchEvents(String dateString)  {
        ArrayList<Event>searchResult=new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    try{
        Date date = sdf.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i=0; i<filteredEvents.size(); i++){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date eventDate = simpleDateFormat.parse(filteredEvents.get(i).getEventAvailableAt());
            Calendar eventCalendar = Calendar.getInstance();
            eventCalendar.setTime(eventDate);
            if(calendar.get(Calendar.YEAR)==eventCalendar.get(Calendar.YEAR)&&
                    calendar.get(Calendar.MONTH)==eventCalendar.get(Calendar.MONTH)&&
                    calendar.get(Calendar.DAY_OF_MONTH)==eventCalendar.get(Calendar.DAY_OF_MONTH)){
                searchResult.add(filteredEvents.get(i));
                Log.d("ser", "searchEvents: "+filteredEvents.get(i));
            }
        }
    }
   catch (ParseException e){

   }
return searchResult;
}
    public ArrayList<Event> searchEventsByName(String searchString)  {
        ArrayList<Event>searchResult=new ArrayList<>();
        Pattern pattern=Pattern.compile(searchString,Pattern.CASE_INSENSITIVE);
        for (int i=0; i<filteredEvents.size(); i++){
            Matcher matcher=pattern.matcher(filteredEvents.get(i).getEventProviderName());
            boolean matchFound=matcher.find();
            if(matchFound){
                searchResult.add(filteredEvents.get(i));
            }

        }
        return searchResult;
    }
}