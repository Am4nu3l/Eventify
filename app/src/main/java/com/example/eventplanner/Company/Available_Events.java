package com.example.eventplanner.Company;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventplanner.Adapters.AvailableEventAdapter;
import com.example.eventplanner.Adapters.ImageSliderAdapter;
import com.example.eventplanner.Adapters.PriceSetter;
import com.example.eventplanner.Admin.AdminHome;
import com.example.eventplanner.Entrance.Login_Page;
import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Available_Events extends Fragment implements CompanyDatabaseActivity.UploadCallback{
    RecyclerView myEvents;
    EditText buffe,ethFood,fastingBuffe,birthdayCake,iceCream,cookies,
            weddingCake,beer,water,softDrink,hotDrinks,decor,band,stage,speaker;

    AvailableEventAdapter adapter;
    ArrayList<Event>eventList=new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    ProgressBar  pd;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private ImageView imageView;
    private Uri selectedImageUri;
  private   Event singleEvent;
    private static final long SLIDER_DELAY = 3000; // Delay between slides (in milliseconds)
    private ViewPager viewPager;
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 300; // Delay before starting the auto slide (in milliseconds)
    private final long PERIOD_MS = 2500; // Interval for auto sliding (in milliseconds)
  private   ProgressBar progressBar;
  private String imageUrl;
    HashMap<String,Object>eventToBePosted=new HashMap<>();
    Event singleEventWithItems;
    ArrayList<String> sliderImageUrl=new ArrayList<>();
    EditText pricePerPlate;
    ArrayList<String> services = new ArrayList<>();
    int id;
    List<Uri> imageUris;
    ImageView one,two,three;
    ArrayList<String>decorImages=new ArrayList<>();
    private interface UploadCallback {
        void onSuccess(List<String> downloadUrls);
        void onFailure(Exception e);
        void onProgress(int progress);
    }
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_available__events, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        myEvents=view.findViewById(R.id.availableEventRv);
        Drawable drawable=getContext().getDrawable(R.drawable.divider_vertical);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myEvents.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
        myEvents.addItemDecoration(dividerItemDecoration);
        myEvents.setLayoutManager(layoutManager);
        FloatingActionButton floatingActionButton=view.findViewById(R.id.fab);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.create_event_dialog);
        dialog
                .getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        imageView = dialog.findViewById(R.id.imageView);
        Button cancel=dialog.findViewById(R.id.cancelButton);
        Button create=dialog.findViewById(R.id.createButton);
        Button addImage=dialog.findViewById(R.id.chooseImageButton);
         progressBar=view.findViewById(R.id.spin_kit);
        EditText size=dialog.findViewById(R.id.sizeEditText);
        DatePicker datePicker=dialog.findViewById(R.id.datePicker);
        EditText description=dialog.findViewById(R.id.descriptionEditText);
        pricePerPlate=dialog.findViewById(R.id.pricePerPlate);
        EditText fromHour=dialog.findViewById(R.id.hourfrom);
        EditText toHour=dialog.findViewById(R.id.hourto);
        CheckBox weddingCheck=dialog.findViewById(R.id.weddingCheck);
        CheckBox birthdayCheck=dialog.findViewById(R.id.birthdayCheck);
        CheckBox partyCheck=dialog.findViewById(R.id.partyCheck);
        CheckBox meetingCheck=dialog.findViewById(R.id.meetingCheck);
        RadioButton radioAm=dialog.findViewById(R.id.radioAM);
        RadioButton radioPm=dialog.findViewById(R.id.radioPM);
        sharedPreferences=getActivity().getSharedPreferences("USER_IDENTITY", Context.MODE_PRIVATE);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        ArrayList<Event> singleEventContainer=new ArrayList<>();
        RadioGroup foodRadioGroupOne=dialog.findViewById(R.id.checkboxFoodColumn1);
        RadioGroup foodRadioGroupTwo=dialog.findViewById(R.id.checkboxFoodColumn2);
        RadioGroup drinkRadioGroupOne=dialog.findViewById(R.id.checkboxDrinksColumn1);
        RadioGroup drinkRadioGroupTwo=dialog.findViewById(R.id.checkboxDrinksColumn2);
        RadioGroup equipmentRadioGroupOne=dialog.findViewById(R.id.checkboxEquipmentsColumn1);
        RadioGroup equipmentRadioGroupTwo=dialog.findViewById(R.id.checkboxEquipmentsColumn2);
        CheckBox foodNone=dialog.findViewById(R.id.checkFoodNone);
        CheckBox drinkNone=dialog.findViewById(R.id.checkDrinkNone);
        CheckBox equipmentNone=dialog.findViewById(R.id.checkEquipmentNone);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        viewPager =view.findViewById(R.id.viewPager);
        pd = view.findViewById(R.id.spin_kit);
    Circle circle = new Circle();
         one=dialog.findViewById(R.id.img1);
        two=dialog.findViewById(R.id.img2);
         three=dialog.findViewById(R.id.img3);
         imageUris=new ArrayList<>();
        progressBar.setIndeterminateDrawable(circle);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        //loading prices
        LoadPrice();
        //loading banners
        db.collection("Banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.get("to").equals("company")) {
                                ArrayList<String> images = (ArrayList<String>) snapshot.get("banner");
                                if (images != null) {
                                    for (String eType : images) {
                                        sliderImageUrl.add(eType);
                                    }
                                    ImageSliderAdapter adapter = new ImageSliderAdapter(getActivity(), sliderImageUrl);
                                    viewPager.setAdapter(adapter);
                                }
                            }
                        }
                    } else {
                        // Handle unsuccessful task
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Handle specific exception types
                            Log.e("TAG", "Error: " + exception.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == sliderImageUrl.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, DELAY_MS, PERIOD_MS);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Optional: Add a click listener to handle item clicks
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                Toast.makeText(getActivity(), "Clicked item " + currentItem, Toast.LENGTH_SHORT).show();
            }
        });
//        Button singOut=view.findViewById(R.id.btnSignOut);
//        singOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                Intent intent=new Intent(getActivity(), Login_Page.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });
    create.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String amOrPm=null;
            Log.d("URI", "onClick: " + selectedImageUri);
            if(size.getText().toString().isEmpty()||toHour.getText().toString().isEmpty()
            ||fromHour.getText().toString().isEmpty()||description.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please Fill all the fields", Toast.LENGTH_SHORT).show();
            }
            else {
            if (weddingCheck.isChecked()) {
                services.add(weddingCheck.getText().toString());
            }
              if (birthdayCheck.isChecked()) {
                  services.add(birthdayCheck.getText().toString());
              }
              if (partyCheck.isChecked()) {
                  services.add(partyCheck.getText().toString());
              }
              if (meetingCheck.isChecked()) {
                  services.add(meetingCheck.getText().toString());
              }
              if (radioAm.isChecked()){
                  amOrPm=radioAm.getText().toString();
              }
              if (radioPm.isChecked()){
                  amOrPm=radioPm.getText().toString();
              }
              Gson gson=new Gson();
              ArrayList<String>serviceProvided=new ArrayList<>();
              String savedJson = sharedPreferences.getString("services", null);
              if (savedJson != null) {
                  Type type = new TypeToken<ArrayList<String>>() {}.getType();
                  serviceProvided = gson.fromJson(savedJson, type);
              }
              //-----------------------food Choice Start----------------------
              ArrayList<String> foodCheckedCheckbox = new ArrayList<>();
              if(!foodNone.isChecked()){
              for (int i = 0; i < foodRadioGroupOne.getChildCount(); i++) {
                  View checkView = foodRadioGroupOne.getChildAt(i);
                  if (checkView instanceof CheckBox) {
                      CheckBox checkBox = (CheckBox) checkView;
                      if (checkBox.isChecked()) {
                          foodCheckedCheckbox.add(checkBox.getText().toString());
                      }
                  }
              }
              for (int i = 0; i < foodRadioGroupTwo.getChildCount(); i++) {
                  View checkView = foodRadioGroupTwo.getChildAt(i);
                  if (checkView instanceof CheckBox) {
                      CheckBox checkBox = (CheckBox) checkView;
                      if (checkBox.isChecked()) {
                          foodCheckedCheckbox.add(checkBox.getText().toString());
                      }
                  }
              }
              }
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
                      }
                  }
              }
              for (int i = 0; i < drinkRadioGroupTwo.getChildCount(); i++) {
                  View checkView = drinkRadioGroupTwo.getChildAt(i);
                  if (checkView instanceof CheckBox) {
                      CheckBox checkBox = (CheckBox) checkView;
                      if (checkBox.isChecked()) {
                          drinkCheckedCheckbox.add(checkBox.getText().toString());
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
                      }
                  }
              }
              for (int i = 0; i < equipmentRadioGroupTwo.getChildCount(); i++) {
                  View checkView = equipmentRadioGroupTwo.getChildAt(i);
                  if (checkView instanceof CheckBox) {
                      CheckBox checkBox = (CheckBox) checkView;
                      if (checkBox.isChecked()) {
                          equipmentCheckedCheckbox.add(checkBox.getText().toString());
                      }
                  }
              }}
              else{
                  equipmentCheckedCheckbox.add("None");
              }
              //----------------------------equipment Choice End------------------------------------
     singleEventWithItems=new Event(foodCheckedCheckbox,drinkCheckedCheckbox,equipmentCheckedCheckbox);
                 singleEvent =
                new Event(
                    services,
                    sharedPreferences.getString("c_name",null),  sharedPreferences.getString("c_pn1",null),
                    description.getText().toString(),
                    datePicker.getYear()
                        + "/"
                        + datePicker.getMonth()
                        + "/"
                        + datePicker.getDayOfMonth(),
                    fromHour.getText() + "-" + toHour.getText()+"\t\t"+amOrPm,
                    selectedImageUri,
                    size.getText().toString(),
                    String.valueOf(UUID.randomUUID()),
                        sharedPreferences.getString("c_id",null),serviceProvided);
                      singleEventContainer.add(singleEvent);
           //   singleEventContainer.add(singleEventWithItems);
            adapter = new AvailableEventAdapter(singleEventContainer);
            myEvents.setAdapter(adapter);
            adapter.notifyItemInserted(singleEventContainer.size() - 1);
                uploadImages(imageUris, new Available_Events.UploadCallback() {
                    @Override
                    public void onSuccess(List<String> downloadUrls) {
                        for (int i=0; i<downloadUrls.size(); i++){
                            decorImages.add(downloadUrls.get(i));
                            Log.d("DEC", "onSuccess: "+downloadUrls.get(i));
                        }
                        progressBar.setVisibility(View.GONE);
                        CompanyDatabaseActivity.uploadImage(selectedImageUri, Available_Events.this);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the failure or display an error message
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onProgress(int progress) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            dialog.dismiss();
          }}
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndOpenGallery();
            }
        });
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
dialog.show();
      }
    });
    //----------------------------Start Loading My Events-----------------------------------------
    //start the progress bar and later hide it after the loading progress in done
    progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> eventTypes=new ArrayList<>();
        database.collection("Event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Log.d("TAG", "onCreateView: "+sharedPreferences.getString("c_id",null)+"\t\t");

                        if(snapshot.get("e_providerId").equals(sharedPreferences.getString("c_id",null))){
                            Log.d("LAG", "onCreateView: "+sharedPreferences.getString("c_id",null)+"\t\t"+snapshot.getId());
                        //get the event types
                        if (snapshot.exists()) {
                            ArrayList<String> eTypes = (ArrayList<String>) snapshot.get("e_type");
                            if (eTypes != null) {
                                for (String eType : eTypes) {
                                    eventTypes.add(eType);
                                }
                            }
                        }
                        //create event object
                        Event event=new Event(eventTypes,snapshot.get("e_providerName").toString()
                             , snapshot.get("e_provider_pn_number").toString(),snapshot.get("e_description").toString(),snapshot.get("e_availableAt").toString()
                                ,snapshot.get("e_hourOfTheDay").toString(),Uri.parse(snapshot.get("e_image_url").toString()),snapshot.get("e_size").toString()
                                ,snapshot.getId(),snapshot.get("e_providerId").toString(), (ArrayList<String>) snapshot.get("services"));
                        singleEventContainer.add(event);
                        }
                    }
                    adapter = new AvailableEventAdapter(singleEventContainer);
                    myEvents.setAdapter(adapter);
                    adapter.notifyItemInserted(singleEventContainer.size() - 1);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
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
        Button editPrice=view.findViewById(R.id.editPrice);
        Button savePrice=view.findViewById(R.id.savePrice);

        init(view);
        editPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buffe.setEnabled(true);  iceCream.setEnabled(true);  water.setEnabled(true);
                ethFood.setEnabled(true);  cookies.setEnabled(true);  softDrink.setEnabled(true);
                fastingBuffe.setEnabled(true);  weddingCake.setEnabled(true);  hotDrinks.setEnabled(true);
                birthdayCake.setEnabled(true);  beer.setEnabled(true);  decor.setEnabled(true);
                band.setEnabled(true);  speaker.setEnabled(true);  stage.setEnabled(true);
                savePrice.setVisibility(View.VISIBLE);
            }
        });


        savePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buffe.getText().toString().isEmpty()||iceCream.getText().toString().isEmpty()||water.getText().toString().isEmpty()||
                        ethFood.getText().toString().isEmpty()||cookies.getText().toString().isEmpty()||softDrink.getText().toString().isEmpty()||
                        fastingBuffe.getText().toString().isEmpty()||weddingCheck.getText().toString().isEmpty()||hotDrinks.getText().toString().isEmpty()||
                        birthdayCheck.getText().toString().isEmpty()||beer.getText().toString().isEmpty()||decor.getText().toString().isEmpty()||
                        band.getText().toString().isEmpty()||speaker.getText().toString().isEmpty()||stage.getText().toString().isEmpty()){
                    buffe.setEnabled(false);  iceCream.setEnabled(false);  water.setEnabled(false);
                    ethFood.setEnabled(false);  cookies.setEnabled(false);  softDrink.setEnabled(false);
                    fastingBuffe.setEnabled(false);  weddingCake.setEnabled(false);  hotDrinks.setEnabled(false);
                    birthdayCake.setEnabled(false);  beer.setEnabled(false);  decor.setEnabled(false);
                    band.setEnabled(false);  speaker.setEnabled(false);  stage.setEnabled(false);
                    savePrice.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Please Don't left fields empty if you don't provide the services Enter 0 as a price ", Toast.LENGTH_LONG).show();
                }
                else {
                   HashMap<String,String>prices=new HashMap<>();
                    prices.put("buffe",buffe.getText().toString());
                    prices.put("ethFood",ethFood.getText().toString());
                    prices.put("fastingBuffe",fastingBuffe.getText().toString());
                    prices.put("birthdayCake",birthdayCake.getText().toString());
                    prices.put("iceCream",iceCream.getText().toString());
                    prices.put("cookies",cookies.getText().toString());
                    prices.put("weddingCake",weddingCake.getText().toString());
                    prices.put("beer",beer.getText().toString());
                    prices.put("water",water.getText().toString());
                    prices.put("softDrink",softDrink.getText().toString());
                    prices.put("hotDrinks",hotDrinks.getText().toString());
                    prices.put("decor",decor.getText().toString());
                    prices.put("stage",stage.getText().toString());
                    prices.put("band",band.getText().toString());
                    prices.put("speaker",speaker.getText().toString());
                    prices.put("c_id",sharedPreferences.getString("c_id",null));
                    progressBar.setVisibility(View.VISIBLE);
                    database.collection("Price").document(sharedPreferences.getString("c_id",null)).set(prices).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                buffe.setEnabled(false);  iceCream.setEnabled(false);  water.setEnabled(false);
                                ethFood.setEnabled(false);  cookies.setEnabled(false);  softDrink.setEnabled(false);
                                fastingBuffe.setEnabled(false);  weddingCake.setEnabled(false);  hotDrinks.setEnabled(false);
                                birthdayCake.setEnabled(false);  beer.setEnabled(false);  decor.setEnabled(false);
                                band.setEnabled(false);  speaker.setEnabled(false);  stage.setEnabled(false);
                                savePrice.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Price Updated Successfully!", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getActivity(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
             }
            }
        });
        //----------------------------End Load-----------------------------------------
        return view;
    }
    private void checkPermissionsAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            selectedImageUri=imageUri;
            imageUris.add(imageUri);
            switch (id){
                case R.id.img1:
                    one.setImageURI(imageUri);
                    break;
                case R.id.img2:
                    two.setImageURI(imageUri);
                    break;
                case R.id.img3:
                    three.setImageURI(imageUri);
                    break;
                case R.id.imageView:
                    imageView.setImageURI(imageUri);
                    imageView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    @Override
    public void onProgress(int progress) {
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setClickable(false);
    }
    private void uploadImages(List<Uri> imageUris, Available_Events.UploadCallback callback) {
        List<String> downloadUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(imageUris.size());
        for (Uri imageUri : imageUris) {
            String imageName = UUID.randomUUID().toString(); // Generate a unique name for each image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("decoreImages/" + imageName);
            UploadTask uploadTask = storageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image upload is successful
                // Get the download URL of the uploaded image
                storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    downloadUrls.add(imageUrl);
                    int count = uploadCount.decrementAndGet();
                    if (count == 0) {
                        // All images have been uploaded successfully
                        callback.onSuccess(downloadUrls);
                    }
                });
            }).addOnFailureListener(e -> {
                // Image upload failed
                // Handle the failure or display an error message
                callback.onFailure(e);
            }).addOnProgressListener(taskSnapshot -> {
                // Calculate and display the upload progress
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                callback.onProgress((int) progress);
                // Update your progress UI accordingly
            });
        }
    }
    @Override
    public void onSuccess(String downloadUrl) {
            imageUrl=downloadUrl;
            eventToBePosted.put("e_type", singleEvent.getEventType());
            eventToBePosted.put("e_providerName", singleEvent.getEventProviderName());
            eventToBePosted.put("e_providerId", singleEvent.getEventProviderId());
            eventToBePosted.put("e_description", singleEvent.getEventDescription());
            eventToBePosted.put("e_availableAt", singleEvent.getEventAvailableAt());
            eventToBePosted.put("e_hourOfTheDay", singleEvent.getEventHourOfTheDay());
            eventToBePosted.put("e_id", singleEvent.getEventId());
            eventToBePosted.put("e_provider_pn_number", singleEvent.getEventProviderPhoneNumber());
            eventToBePosted.put("e_size", singleEvent.getEventSizeInRange());
            eventToBePosted.put("e_image_url", imageUrl);
            eventToBePosted.put("services", singleEvent.getServiceProvided());
            eventToBePosted.put("e_foods", singleEventWithItems.getFoods());
            if(pricePerPlate.getText().toString().isEmpty()){
                eventToBePosted.put("e_price_per_plate", "0");
            }
            else{
                eventToBePosted.put("e_price_per_plate", pricePerPlate.getText().toString());
            }
            eventToBePosted.put("e_drinks", singleEventWithItems.getDrinks());
            eventToBePosted.put("e_equipments", singleEventWithItems.getEquipments());
            eventToBePosted.put("e_decor", decorImages);
            CompanyDatabaseActivity.postEvent(eventToBePosted);
            progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Your Event Has Been Posted", Toast.LENGTH_LONG).show();
        services.clear();
        eventToBePosted.clear();
    }
    @Override
    public void onFailure(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Uploading Failed !! Please Try again", Toast.LENGTH_SHORT).show();
    }


    public void init(View view){
        buffe=view.findViewById(R.id.priceBuffe);
        ethFood=view.findViewById(R.id.priceEthiopianFood);
        fastingBuffe=view.findViewById(R.id.priceBuffeFasting);
        birthdayCake=view.findViewById(R.id.priceBirthDaycake);
        iceCream=view.findViewById(R.id.priceIceCream);
        cookies=view.findViewById(R.id.priceCookies);
        weddingCake=view.findViewById(R.id.priceWeddingCake);
        beer=view.findViewById(R.id.priceBeer);
        water=view.findViewById(R.id.priceWater);
        softDrink=view.findViewById(R.id.priceSoftDrinks);
        hotDrinks=view.findViewById(R.id.priceHotDrinks);
        decor=view.findViewById(R.id.priceDecor);
        band=view.findViewById(R.id.priceBand);
        stage=view.findViewById(R.id.priceStage);
        speaker=view.findViewById(R.id.priceSpeaker);
    }
    public void LoadPrice(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Price").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.exists()) {
                                if (snapshot.get("c_id").equals(sharedPreferences.getString("c_id", null))) {
                                    buffe.setText(snapshot.get("buffe").toString());
                                    iceCream.setText(snapshot.get("iceCream").toString());
                                    water.setText(snapshot.get("water").toString());
                                    ethFood.setText(snapshot.get("ethFood").toString());
                                    cookies.setText(snapshot.get("cookies").toString());
                                    softDrink.setText(snapshot.get("softDrink").toString());
                                    fastingBuffe.setText(snapshot.get("fastingBuffe").toString());
                                    weddingCake.setText(snapshot.get("weddingCake").toString());
                                    hotDrinks.setText(snapshot.get("hotDrinks").toString());
                                    birthdayCake.setText(snapshot.get("birthdayCake").toString());
                                    beer.setText(snapshot.get("beer").toString());
                                    decor.setText(snapshot.get("decor").toString());
                                    band.setText(snapshot.get("band").toString());
                                    speaker.setText(snapshot.get("speaker").toString());
                                    stage.setText(snapshot.get("stage").toString());
                                }
                            } else {
                                Toast.makeText(getActivity(), "No price has been set", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Handle the exception here
                    Toast.makeText(getActivity(), "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}