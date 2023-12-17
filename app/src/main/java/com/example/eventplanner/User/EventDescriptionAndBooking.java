package com.example.eventplanner.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.Payment;
import com.example.eventplanner.R;
import com.example.eventplanner.User.User_HHP.UserHHP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventDescriptionAndBooking extends AppCompatActivity {
private TextView eventDescription,eventType,eventProvider,eventDate,eventTime,eventSize;
private EditText exactEventSize;
  private   Event singleBookedEventWithItems;
private ImageView eventImage;
private  Double price;
private ArrayList<String>decors;
    Dialog dialog;
private ProgressBar progressBar;
private  Double totalPrice;


private String eventDescriptionFromIntent,getEventProviderPhoneNumber,eventTypeFromIntent,eventProviderFromIntent,eventDateFromIntent,eventTimeFromIntent
        ,eventSizeFromIntent,eventImageFromIntent,eventIdFromIntent,
        eventProviderId,eventWhereToBeHeld,DecorImageOne,DecorImageTwo,DecorImageThree;
    ArrayList<Double> priceTag;
    ArrayList<Double> priceTagEquipment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_description_and_booking);
    if(Build.VERSION.SDK_INT>=23){
          View decore=EventDescriptionAndBooking.this.getWindow().getDecorView();
          if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
              decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
          }
          else{
              decore.setSystemUiVisibility(0);
          }
      }
      HashMap<String,String> prices=new HashMap<>();
     priceTag=new ArrayList<>();
     priceTagEquipment=new ArrayList<>();
    Button bookEvent=findViewById(R.id.bookEventButton);
eventDescription=findViewById(R.id.eventDescriptionTextView);
eventType=findViewById(R.id.eventTypeTextView);
eventProvider=findViewById(R.id.eventProviderTextView);
eventDate=findViewById(R.id.eventDateTextView);
eventTime=findViewById(R.id.eventTimeTextView);
eventSize=findViewById(R.id.eventSizeTextView);
exactEventSize=findViewById(R.id.exactEventSize);
eventImage=findViewById(R.id.imageView);
ImageButton back=findViewById(R.id.backButton);
      dialog=new Dialog(EventDescriptionAndBooking.this);
progressBar=dialog.findViewById(R.id.spin_kit);
      TextView pricePlan=findViewById(R.id.pricePerPlate);
      RadioGroup foodRadioGroupOne=findViewById(R.id.checkboxFoodColumn1);
      RadioGroup foodRadioGroupTwo=findViewById(R.id.checkboxFoodColumn2);
      RadioGroup drinkRadioGroupOne=findViewById(R.id.checkboxDrinksColumn1);
      FirebaseFirestore db = FirebaseFirestore.getInstance();
      RadioGroup drinkRadioGroupTwo=findViewById(R.id.checkboxDrinksColumn2);
      RadioGroup equipmentRadioGroupOne=findViewById(R.id.checkboxEquipmentsColumn1);
      RadioGroup equipmentRadioGroupTwo=findViewById(R.id.checkboxEquipmentsColumn2);
      CheckBox foodNone=findViewById(R.id.checkFoodNone);
      CheckBox drinkNone=findViewById(R.id.checkDrinkNone);
      CheckBox equipmentNone=findViewById(R.id.checkEquipmentNone);
eventDescriptionFromIntent=getIntent().getStringExtra("EventDescription");
    eventTypeFromIntent=getIntent().getStringExtra("EventType");
    eventProviderFromIntent=getIntent().getStringExtra("EventProviderName");
    getEventProviderPhoneNumber=getIntent().getStringExtra("EventProviderPhoneNumber");
    eventDateFromIntent=getIntent().getStringExtra("EventDate");
    eventTimeFromIntent=getIntent().getStringExtra("EventTime");
    eventSizeFromIntent=getIntent().getStringExtra("EventSize");
    eventImageFromIntent=getIntent().getStringExtra("EventImage");
    DecorImageOne=getIntent().getStringExtra("DecorImageOne");
    DecorImageTwo=getIntent().getStringExtra("DecorImageTwo");
      DecorImageThree=getIntent().getStringExtra("DecorImageThree");
      eventIdFromIntent=getIntent().getStringExtra("EventId");
      eventProviderId=getIntent().getStringExtra("EventProviderId");
      eventWhereToBeHeld=getIntent().getStringExtra("EventWhereToBeHeld");
      ImageView one=findViewById(R.id.img1);
      ImageView two=findViewById(R.id.img2);
      ImageView three=findViewById(R.id.img3);
      decors=new ArrayList<>();
      decors.add(DecorImageOne);
      decors.add(DecorImageTwo);
      decors.add(DecorImageThree);
    eventDescription.setText(eventDescriptionFromIntent);
    eventType.setText("Event Type:\t"+eventTypeFromIntent);
    eventProvider.setText(eventProviderFromIntent);
    eventDate.setText(eventDateFromIntent);
    eventTime.setText(eventTimeFromIntent);
    RadioButton decor1=findViewById(R.id.decor1);
      RadioButton decor2=findViewById(R.id.decor2);
      RadioButton decor3=findViewById(R.id.decor3);
    eventSize.setText("Event Size:\t"+eventSizeFromIntent);
    Picasso.get().load(eventImageFromIntent).into(eventImage);
      Picasso.get().load(DecorImageOne).into(one);
      Picasso.get().load(DecorImageTwo).into(two);
      Picasso.get().load(DecorImageThree).into(three);
      ArrayList<String> foodCheckedCheckbox = new ArrayList<>();
      ProgressBar progressBar=findViewById(R.id.spin_kit);
      WebView webView=findViewById(R.id.paymentWebView);
      webView.getSettings().setJavaScriptEnabled(true);
      progressBar.setVisibility(View.VISIBLE);
      db.collection("Price")
              .document(eventProviderId)
              .get()
              .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()) {
                          DocumentSnapshot snapshot = task.getResult();
                          if (snapshot != null && snapshot.exists()) {
                              Object bandObj = snapshot.get("c_id");
                              if (bandObj != null && bandObj.toString().equals(eventProviderId)) {
                                  // Retrieve other fields from the snapshot and add them to prices
                                  prices.put("band", snapshot.get("band").toString());
                                  prices.put("birthdayCake", snapshot.get("birthdayCake").toString());
                                  prices.put("beer", snapshot.get("beer").toString());
                                  prices.put("buffe", snapshot.get("buffe").toString());
                                  prices.put("cookies", snapshot.get("cookies").toString());
                                  prices.put("decor", snapshot.get("decor").toString());
                                  prices.put("ethFood", snapshot.get("ethFood").toString());
                                  prices.put("fastingBuffe", snapshot.get("fastingBuffe").toString());
                                  prices.put("hotDrinks", snapshot.get("hotDrinks").toString());
                                  prices.put("iceCream", snapshot.get("iceCream").toString());
                                  prices.put("softDrink", snapshot.get("softDrink").toString());
                                  prices.put("speaker", snapshot.get("speaker").toString());
                                  prices.put("stage", snapshot.get("stage").toString());
                                  prices.put("water", snapshot.get("water").toString());
                                  prices.put("weddingCake", snapshot.get("weddingCake").toString());
                                  progressBar.setVisibility(View.GONE);
                                  Toast.makeText(EventDescriptionAndBooking.this,  snapshot.get("weddingCake").toString(), Toast.LENGTH_SHORT).show();

                              } else {
                                  Toast.makeText(EventDescriptionAndBooking.this, "No Price Uploaded from The user", Toast.LENGTH_SHORT).show();
                              }
                          } else {
                              Toast.makeText(EventDescriptionAndBooking.this, "Snapshot does not exist", Toast.LENGTH_SHORT).show();
                          }
                      } else {
                          Toast.makeText(EventDescriptionAndBooking.this, "Error retrieving document: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
//      db.collection("Event").document(eventIdFromIntent).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//          @Override
//          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//              price=Double.valueOf(task.getResult().get("e_price_per_plate").toString());
//              pricePlan.setText("Price Per Plate =\t"+price);
//          }
//      });
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(EventDescriptionAndBooking.this, UserHHP.class);
        startActivity(intent);
        finish();
    }
});
      db.collection("Event")
        .document(eventIdFromIntent)
        .get()
        .addOnCompleteListener(
            new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (task.isSuccessful()) {
                  if (snapshot.exists()) {
                    ArrayList<String> eTypes = (ArrayList<String>) snapshot.get("e_drinks");
                    if (eTypes != null) {
                      for (String eType : eTypes) {
                        if (!eType.equals("None")) {
                          if (eType.equals("Soft Drinks")) {
                            CheckBox checkBox = findViewById(R.id.checkSoftDrinks);
                            checkBox.setVisibility(View.VISIBLE);
                           checkBox.setText(checkBox.getText() + "\t" + prices.get("softDrink"));
                          } else if (eType.equals("Beer")) {
                            CheckBox checkBox = findViewById(R.id.checkBeer);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("beer") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("beer"));
                             // priceTag.add(Double.valueOf(prices.get("beer")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }

                          } else if (eType.equals("Water")) {
                            CheckBox checkBox = findViewById(R.id.checkWater);
                            checkBox.setVisibility(View.VISIBLE);

                            if (prices.get("water") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("water"));
                             // priceTag.add(Double.valueOf(prices.get("water")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          } else if (eType.equals("Hot Drinks")) {
                            CheckBox checkBox = findViewById(R.id.checkHotDrinks);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("hotDrinks") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("hotDrinks"));
                             // priceTag.add(Double.valueOf(prices.get("hotDrinks")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          } else if (eType.equals("Traditional Drinks")) {
                            CheckBox checkBox = findViewById(R.id.checkTraditionalDrink);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("ethFood") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("ethFood"));
                             // priceTag.add(Double.valueOf(prices.get("ethFood")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          }
                        } else {
                          drinkRadioGroupOne.setVisibility(View.GONE);
                          drinkRadioGroupTwo.setVisibility(View.GONE);
                        }
                      }
                    }
                  }
                  ///// ------------------------------------------
                  if (snapshot.exists()) {
                    ArrayList<String> eTypes = (ArrayList<String>) snapshot.get("e_equipments");
                    if (eTypes != null) {
                      for (String eType : eTypes) {
                        if (!eType.equals("None")) {
                          if (eType.equals("Speaker")) {
                            CheckBox checkBox = findViewById(R.id.checkSpeaker);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("speaker") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("speaker"));
                              //priceTag.add(Double.valueOf(prices.get("speaker")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          } else if (eType.equals("Stage")) {
                            CheckBox checkBox = findViewById(R.id.checkIceStage);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("stage") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("stage"));
                                // priceTag.add(Double.valueOf(prices.get("stage")));
                            } else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          } else if (eType.equals("Band")) {
                            CheckBox checkBox = findViewById(R.id.checkBand);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("band") != null) {
                              checkBox.setText(checkBox.getText() + "\t" + prices.get("band"));
                             // priceTag.add(Double.valueOf(prices.get("band")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          } else if (eType.equals("DJ")) {
                            CheckBox checkBox = findViewById(R.id.checkDJ);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("band")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("band"));
                            //priceTag.add(Double.valueOf(prices.get("band")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          } else if (eType.equals("Traditional Band")) {
                            CheckBox checkBox = findViewById(R.id.checkTraditionalBand);
                            checkBox.setVisibility(View.VISIBLE);
                            if ( prices.get("band")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("band"));
                            //priceTag.add(Double.valueOf(prices.get("band")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          }

                        } else {
                          equipmentRadioGroupOne.setVisibility(View.GONE);
                          equipmentRadioGroupTwo.setVisibility(View.GONE);
                        }
                      }
                    }
                  }
                  //// ------------------------------------------------------
                  if (snapshot.exists()) {
                    ArrayList<String> eTypes = (ArrayList<String>) snapshot.get("e_foods");
                    if (eTypes != null) {
                      for (String eType : eTypes) {
                        if (!eType.equals("None")) {
                          if (eType.equals("Buffe")) {
                            CheckBox checkBox = findViewById(R.id.checkBuffe);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("buffe")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("buffe"));
                           // priceTag.add(Double.valueOf(prices.get("buffe")));
                                }
                            else {
                              checkBox.setText(checkBox.getText() + "\t" + 0.0);
                              priceTag.add(0.0);
                            }
                          } else if (eType.equals("Fast Buffe")) {
                            CheckBox checkBox = findViewById(R.id.checkBuffeFast);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("fastingBuffe")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("fastingBuffe"));
                           // priceTag.add(Double.valueOf(prices.get("fastingBuffe")));
                          }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          }else if (eType.equals("Ice Cream")) {
                            CheckBox checkBox = findViewById(R.id.checkIceCream);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("iceCream")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("iceCream"));
                            //priceTag.add(Double.valueOf(prices.get("iceCream")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          } else if (eType.equals("Wedding Cake")) {
                            CheckBox checkBox = findViewById(R.id.checkWeddingCake);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("weddingCake")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("weddingCake"));
                            //priceTag.add(Double.valueOf(prices.get("weddingCake")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          } else if (eType.equals("Ethiopian Food")) {
                            CheckBox checkBox = findViewById(R.id.checkEthiopianFood);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("ethFood")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("ethFood"));
                            //priceTag.add(Double.valueOf(prices.get("ethFood")));
                                }
                             else {
                                  checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                  priceTag.add(0.0);
                              }
                          } else if (eType.equals("BirthDay Cake")) {
                            CheckBox checkBox = findViewById(R.id.checkBirthDayCake);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("birthdayCake")!=null){
                            checkBox.setText(
                                checkBox.getText() + "\t" + prices.get("birthdayCake"));
                           // priceTag.add(Double.valueOf(prices.get("birthdayCake")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          } else if (eType.equals("Cookies")) {
                            CheckBox checkBox = findViewById(R.id.checkCookies);
                            checkBox.setVisibility(View.VISIBLE);
                            if (prices.get("cookies")!=null){
                            checkBox.setText(checkBox.getText() + "\t" + prices.get("cookies"));
                          //  priceTag.add(Double.valueOf(prices.get("cookies")));
                            }
                            else {
                                checkBox.setText(checkBox.getText() + "\t" + 0.0);
                                priceTag.add(0.0);
                            }
                          }
                        } else {
                          foodRadioGroupOne.setVisibility(View.GONE);
                          foodRadioGroupTwo.setVisibility(View.GONE);
                        }
                      }
                    }
                  }
                }
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {}
            });

      bookEvent.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (exactEventSize.getText().toString().isEmpty()) {
                  Toast.makeText(EventDescriptionAndBooking.this, "Please fill the exact Guest Field Above", Toast.LENGTH_SHORT).show();
              }
              else {
                  webView.setVisibility(View.VISIBLE);
                  webView.loadUrl("https://chapa-pay.onrender.com");
                  ArrayList<String> eventType = new ArrayList<>();
                  eventType.add(eventTypeFromIntent);
                  if(!foodNone.isChecked()){
                      for (int i = 0; i < foodRadioGroupOne.getChildCount(); i++) {
                          View checkView = foodRadioGroupOne.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  foodCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTag.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }
                      for (int i = 0; i < foodRadioGroupTwo.getChildCount(); i++) {
                          View checkView = foodRadioGroupTwo.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  foodCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTag.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }}
                  else{
                      foodCheckedCheckbox.add("None");
                  }
                  //----------------------------food Choice End------------------------------------
                  //----------------------------drink Choice start------------------------------------
                  ArrayList<String> drinkCheckedCheckbox = new ArrayList<>();
                  if(!drinkNone.isChecked()){
                      for (int i = 0; i < drinkRadioGroupOne.getChildCount(); i++) {
                          View checkView = drinkRadioGroupOne.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  drinkCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTag.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }
                      for (int i = 0; i < drinkRadioGroupTwo.getChildCount(); i++) {
                          View checkView = drinkRadioGroupTwo.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  drinkCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTag.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }}
                  else{
                      drinkCheckedCheckbox.add("None");
                  }
                  //----------------------------drink Choice End------------------------------------
                  //----------------------------equipment Choice start------------------------------------
                  ArrayList<String> equipmentCheckedCheckbox = new ArrayList<>();
                  if(!equipmentNone.isChecked()){
                      for (int i = 0; i < equipmentRadioGroupOne.getChildCount(); i++) {
                          View checkView = equipmentRadioGroupOne.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  equipmentCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTagEquipment.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }
                      for (int i = 0; i < equipmentRadioGroupTwo.getChildCount(); i++) {
                          View checkView = equipmentRadioGroupTwo.getChildAt(i);
                          if (checkView instanceof CheckBox) {
                              CheckBox checkBox = (CheckBox) checkView;
                              if (checkBox.isChecked()) {
                                  equipmentCheckedCheckbox.add(checkBox.getText().toString());
                                  priceTagEquipment.add(Double.valueOf(getNumber(checkBox.getText().toString())));
                              }
                          }
                      }}
                  else{
                      drinkCheckedCheckbox.add("None");
                  }
                  webView.loadUrl("https://chapa-pay.onrender.com");
                  webView.setWebViewClient(new WebViewClient() {
                      @Override
                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                          if (url.equals("https://chapa-pay.onrender.com/pay")) {
                              //webView.loadUrl(url);
                              progressBar.setVisibility(View.VISIBLE);
                              singleBookedEventWithItems=new Event(foodCheckedCheckbox,drinkCheckedCheckbox,equipmentCheckedCheckbox);
                              DocumentReference sourceDocRef = db.collection("Event").document(eventIdFromIntent);
                              sourceDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                      try {
                                          if (task.isSuccessful()) {
                                              DocumentSnapshot document = task.getResult();
                                              if (document != null && document.exists()) {
                                                  Event event = new Event(eventType, eventProviderFromIntent,getEventProviderPhoneNumber, eventDescriptionFromIntent, eventDateFromIntent, eventTimeFromIntent, Uri.parse(eventImageFromIntent), eventSizeFromIntent, eventIdFromIntent, eventProviderId, (ArrayList<String>) document.get("services"));
                                                  Map<String, Object> data = new HashMap<>();
                                                  SharedPreferences sharedPreferences = getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
                                                  Date currentDate = new Date();
                                                  data.put("e_type", event.getEventType());
                                                  data.put("e_booked_by", sharedPreferences.getString("usrFName", null) + "\t" + sharedPreferences.getString("usrMName", null));
                                                  data.put("e_description", event.getEventDescription());
                                                  data.put("e_availableAt", event.getEventAvailableAt());
                                                  data.put("e_hourOfTheDay", event.getEventHourOfTheDay());
                                                  data.put("e_id", event.getEventId());
                                                  data.put("e_providerId", event.getEventProviderId());
                                                  data.put("e_providerName", event.getEventProviderName());
                                                  data.put("e_provider_pn_number", event.getEventProviderPhoneNumber());
                                                  data.put("e_reservePhoneNumber", sharedPreferences.getString("usrPhone", null));
                                                  data.put("e_booked_size", exactEventSize.getText().toString());
                                                  data.put("e_reserve_id", sharedPreferences.getString("user_id",null));
                                                  data.put("e_created_size", event.getEventSizeInRange());
                                                  data.put("e_image_url", eventImageFromIntent);
                                                  data.put("services", event.getServiceProvided());
                                                  data.put("e_foods", singleBookedEventWithItems.getFoods());
                                                  data.put("e_decor", decors);
                                                  if(decor1.isChecked()){
                                                      data.put("e_chosen_decor", Uri.parse(DecorImageOne));
                                                  }
                                                  if(decor2.isChecked()){
                                                      data.put("e_chosen_decor", Uri.parse(DecorImageTwo));
                                                  }
                                                  if(decor3.isChecked()){
                                                      data.put("e_chosen_decor", Uri.parse(DecorImageThree));
                                                  }
                                                  data.put("e_drinks", singleBookedEventWithItems.getDrinks());
                                                  data.put("e_where_to_be_held",eventWhereToBeHeld);
                                                  data.put("e_booked_date",currentDate.getYear()+"/"+currentDate.getMonth()+""+currentDate.getDay());
                                                  data.put("e_equipments", singleBookedEventWithItems.getEquipments());
                                                  DocumentReference targetDocRef = db.collection("BookedEvent").document(eventIdFromIntent);
                                                  targetDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void aVoid) {
                                                          // Delete the document from the source collection
                                                          sourceDocRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void aVoid) {
                                                                  // Document successfully moved and deleted
                                                                  Toast.makeText(EventDescriptionAndBooking.this, "Event Booked", Toast.LENGTH_SHORT).show();
                                                                  progressBar.setVisibility(View.GONE);
                                                                  webView.setVisibility(View.GONE);
                                                              }
                                                          }).addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  // Failed to delete the document from the source collection
                                                                  Toast.makeText(EventDescriptionAndBooking.this, "Failed to Book the Event"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                  progressBar.setVisibility(View.GONE);
                                                                  webView.setVisibility(View.GONE);
                                                              }
                                                          });
                                                      }
                                                  }).addOnFailureListener(new OnFailureListener() {
                                                      @Override
                                                      public void onFailure(@NonNull Exception e) {
                                                          // Failed to add the document to the target collection
                                                          Toast.makeText(EventDescriptionAndBooking.this, "Failed to Book the Event 2"+e.getMessage() , Toast.LENGTH_SHORT).show();
                                                          progressBar.setVisibility(View.GONE);
                                                          webView.setVisibility(View.GONE);
                                                      }
                                                  });
                                              }
                                              else {
                                                  // Event document not found
                                                  Toast.makeText(EventDescriptionAndBooking.this, "Event document not found 3"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                  progressBar.setVisibility(View.GONE);
                                                  webView.setVisibility(View.GONE);
                                              }
                                          }
                                          else {
                                              // Failed to fetch the document
                                              Toast.makeText(EventDescriptionAndBooking.this, "Failed to fetch the document", Toast.LENGTH_SHORT).show();
                                              progressBar.setVisibility(View.GONE);
                                              webView.setVisibility(View.GONE);
                                          }
                                      }
                                      catch (Exception e) {
                                          // Exception occurred
                                          Toast.makeText(EventDescriptionAndBooking.this, "Failed to Book the Event"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                          Log.d("log", "onComplete: "+e.getMessage());
                                          progressBar.setVisibility(View.GONE);
                                          webView.setVisibility(View.GONE);
                                      }
                                  }
                              });

                              return true;
                          }
                          else {
                              webView.loadUrl(url);
                          }
                          Log.d("url", "shouldOverrideUrlLoading: "+url);
                          return super.shouldOverrideUrlLoading(view, url);
                      }
                      @Override
                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                          super.onPageStarted(view, url, favicon);
                          progressBar.setVisibility(View.VISIBLE);
                      }
                      @Override
                      public void onPageFinished(WebView view, String url) {
                          super.onPageFinished(view, url);
                          SharedPreferences sharedPreferences =
                                  getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
                          String fname=sharedPreferences.getString("usrFName",null);
                          String mname=sharedPreferences.getString("usrMName",null);
                          String email=sharedPreferences.getString("email",null);
                          //String phone=sharedPreferences.getString("u_phone_number",null);
                          totalPrice =(Double.valueOf(exactEventSize.getText().toString())*totalPriceOfChoices())+totalPriceOfChoicesFixed();
                          String token = UUID.randomUUID().toString().replace("-", "");
//                          String javascript = "document.getElementById('inp_email').value = '" + email + "';";
//                          String javascriptFname = "document.getElementById('inp_fname').value = '" + fname + "';";
//                          String javascriptMname = "document.getElementById('inp_lname').value = '" + mname + "';";
                          String javascript3 = "document.getElementById('inp_amount').value = '" + totalPrice+ "';";
                          String javascript2 = "document.getElementById('tex_ref').value = '" + token + "';";
                          String javascript4 = "document.getElementById('inp_amount').readOnly = true;";
                          webView.evaluateJavascript(javascript4,null);
                          webView.evaluateJavascript(javascript2, null);
                          webView.evaluateJavascript(javascript3, null);
//                          webView.evaluateJavascript(javascript, null);
//                          webView.evaluateJavascript(javascriptFname, null);
//                          webView.evaluateJavascript(javascriptMname, null);
                          progressBar.setVisibility(View.GONE);
                      }
                  });

          }
          }

   });

  }
  //calculate total price
  public Double totalPriceOfChoices(){
      Double sum=0.0;
      for(int i=0; i<priceTag.size(); i++){
          sum=sum+priceTag.get(i);
      }
      return sum;
  }
 //calculate the total price for the fixed equipments...we cant multiply this price by the number of guests
    public Double totalPriceOfChoicesFixed(){
        Double sum=0.0;
        for(int i=0; i<priceTagEquipment.size(); i++){
            sum=sum+priceTagEquipment.get(i);
        }
        return sum;
    }
    //extract the price from the checkbox text
  public Double getNumber( String input){
      String number="";
      // Use regular expression to match numbers (\d+)
      Pattern pattern = Pattern.compile("\\d+");
      Matcher matcher = pattern.matcher(input.trim());
    // Iterate through the matches and extract the numbers
    while (matcher.find()) {
      number = matcher.group();
         }
    return Double.parseDouble(number);
  }
}
