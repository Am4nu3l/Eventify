package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.Admin.AdminMain;
import com.example.eventplanner.Company.Company_Main;
import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.User.EventDescriptionAndBooking;
import com.example.eventplanner.User.User_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookedDescription extends AppCompatActivity {
TextView eventSide,eventProvider,eventSize,eventDescription,eventDate,eventTime,eventType,eventFoods
        ,eventDrinks,eventEquipment,phoneNumber,whereToBeHeld;
String eventId;
    ArrayList<String> eEquipments;
    ArrayList<String> eDrinks;
    ArrayList<String> eFoods;
    ArrayList<String> eTypes;
    ImageView imageView;
    Uri imageUri;
    String eventProviderId;
    private ArrayList<String> decors;
    ImageView decorImage;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_booked_description);
      if(Build.VERSION.SDK_INT>=23){
          View decore= BookedDescription.this.getWindow().getDecorView();
          if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
              decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
          }
          else{
              decore.setSystemUiVisibility(0);
          }
      }
      SharedPreferences sharedPreferences = getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
    ImageButton backButton=findViewById(R.id.backButton);
    Button cancelButton=findViewById(R.id.cancelEvent);
    imageView=findViewById(R.id.imageView);
    eventId=getIntent().getStringExtra("eventId");
    String filter=getIntent().getStringExtra("filter");
    eventSide=findViewById(R.id.eventBookedBy);
    eventProvider=findViewById(R.id.eventProviderTextView);
    eventSize=findViewById(R.id.eventSizeTextView);
    whereToBeHeld=findViewById(R.id.eventWhereToBeHeld);
    eventDescription=findViewById(R.id.eventDescriptionTextView);
    eventDate=findViewById(R.id.eventDateTextView);
    eventTime=findViewById(R.id.eventTimeTextView);
    eventType=findViewById(R.id.eventTypeTextView);
    eventFoods=findViewById(R.id.foodList);
    eventDrinks=findViewById(R.id.drinkList);
    eventEquipment=findViewById(R.id.equipmentList);
    phoneNumber=findViewById(R.id.phoneNumber);
    Button call=findViewById(R.id.callButton);
    decorImage=findViewById(R.id.decorImage);
    ProgressBar progressBar=findViewById(R.id.spin_kit);
    progressBar.setVisibility(View.VISIBLE);
    FirebaseFirestore db=FirebaseFirestore.getInstance();
      if(sharedPreferences.getString("user_type",null).equals("COMPANY")){
          cancelButton.setVisibility(View.GONE);
      }
      else if(sharedPreferences.getString("user_type",null).equals("USER")) {
          cancelButton.setVisibility(View.VISIBLE);
      }
      else {
          cancelButton.setVisibility(View.GONE);
      }
    db.collection("BookedEvent").document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        DocumentSnapshot snapshot=task.getResult();
        if (task.isSuccessful()){
              eventDate.setText(snapshot.get("e_availableAt").toString());
              eventProvider.setText(snapshot.get("e_providerName").toString());
              eventSide.setText(snapshot.get("e_booked_by").toString());
            eventProviderId=snapshot.get("e_providerId").toString();
            imageUri= Uri.parse(snapshot.get("e_image_url").toString());
              eventSize.setText(snapshot.get("e_booked_size").toString());
              if(sharedPreferences.getString("user_type",null).equals("COMPANY")){
                  phoneNumber.setText(snapshot.get("e_reservePhoneNumber").toString());
              }
              else if(sharedPreferences.getString("user_type",null).equals("USER")) {
                  phoneNumber.setText(snapshot.get("e_provider_pn_number").toString());
              }
             else {
                 phoneNumber.setText("Person PN:\t"+snapshot.get("e_reservePhoneNumber").toString()+"\t"+"\n"+
                         "Company PN:\t"+snapshot.get("e_provider_pn_number").toString()  );
                 call.setVisibility(View.GONE);
              }
              eventDescription.setText(snapshot.get("e_description").toString());
              eventTime.setText(snapshot.get("e_hourOfTheDay").toString());
              whereToBeHeld.setText(snapshot.get("e_where_to_be_held").toString());
            Picasso.get().load(snapshot.get("e_image_url").toString()).into(imageView);
            Picasso.get().load(snapshot.get("e_chosen_decor").toString()).into(decorImage);
           eTypes = (ArrayList<String>) snapshot.get("e_type");
              if (eTypes != null) {
                for (String eType : eTypes) {
                  eventType.setText(""+eType+"\n");
                }
              }
              eFoods = (ArrayList<String>) snapshot.get("e_foods");
              if (eTypes != null) {
                for (String efoods : eFoods) {
                  eventFoods.setText(""+efoods+"\n");
                }
              }
               eDrinks = (ArrayList<String>) snapshot.get("e_drinks");
              if (eTypes != null) {
                for (String edrinks : eDrinks) {
                  eventDrinks.setText(""+edrinks+"\n");
                }
              }
             eEquipments = (ArrayList<String>) snapshot.get("e_equipments");
              if (eTypes != null) {
                for (String equipments : eEquipments) {
                  eventEquipment.setText(""+equipments+"\n");
            }
          }
            progressBar.setVisibility(View.GONE);
        }
        else {

        }
      }
    });
      call.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
callPhoneNumber(phoneNumber.getText().toString().trim());
          }
      });
    backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(sharedPreferences.getString("user_type",null).equals("COMPANY")){
                Intent intent=new Intent(BookedDescription.this, Company_Main.class);
                startActivity(intent);
                finish();
            }
            else if(sharedPreferences.getString("user_type",null).equals("USER")){
                Intent intent=new Intent(BookedDescription.this, User_Main.class);
                startActivity(intent);
                finish();
            }
           else {
                Intent intent=new Intent(BookedDescription.this, AdminMain.class);
                startActivity(intent);
                finish();
            }
        }
    });
cancelButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference sourceDocRef = db.collection("BookedEvent").document(eventId);
        sourceDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Event event = new Event(eTypes,
                                    eventProvider.getText().toString(),phoneNumber.getText().toString(), eventDescription.getText().toString()
                                    ,eventDate.getText().toString(), eventTime.getText().toString(),
                                    imageUri, eventSize.getText().toString(), eventId, eventProviderId, (ArrayList<String>) document.get("services"));
                            Map<String, Object> data = new HashMap<>();
                            data.put("e_type", event.getEventType());
                            data.put("e_description", event.getEventDescription());
                            data.put("e_availableAt", event.getEventAvailableAt());
                            data.put("e_hourOfTheDay", event.getEventHourOfTheDay());
                            data.put("e_id", event.getEventId());
                            data.put("e_providerId", event.getEventProviderId());
                            data.put("e_providerName", event.getEventProviderName());
                            data.put("e_provider_pn_number", event.getEventProviderPhoneNumber());
                            data.put("e_size", document.get("e_created_size"));
                            data.put("e_image_url", event.getImageUrl());
                            data.put("services", event.getServiceProvided());
                            data.put("e_decor",(ArrayList<String>) document.get("e_decor") );
                            data.put("e_foods", eFoods);
                            data.put("e_drinks", eDrinks);
                            data.put("e_equipments",eEquipments);
                            DocumentReference targetDocRef = db.collection("Event").document(eventId);
                            targetDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Delete the document from the source collection
                                    sourceDocRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document successfully moved and deleted
                                            Toast.makeText(BookedDescription.this, "Event Canceled", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Failed to delete the document from the source collection
                                            Toast.makeText(BookedDescription.this, "Failed to cancel the Event", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to add the document to the target collection
                                    Toast.makeText(BookedDescription.this, "Failed to cancel the Event", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            // Event document not found
                            Toast.makeText(BookedDescription.this, "Event document not found", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        // Failed to fetch the document
                        Toast.makeText(BookedDescription.this, "Failed to fetch the document", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    // Exception occurred
                    Toast.makeText(BookedDescription.this, "Failed to cancel the Event", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
});

  }
    public void callPhoneNumber(String phoneNumber) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
