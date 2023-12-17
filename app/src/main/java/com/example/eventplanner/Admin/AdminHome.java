package com.example.eventplanner.Admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.eventplanner.Adapters.ImageSliderAdapter;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminHome extends Fragment {
    private static final long SLIDER_DELAY = 3000; // Delay between slides (in milliseconds)
    private ViewPager viewPager;
    private int currentPage = 0;
    private Timer timer;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private final long DELAY_MS = 500; // Delay before starting the auto slide (in milliseconds)
    private final long PERIOD_MS = 3000; // Interval for auto sliding (in milliseconds)
    private interface UploadCallback {
        void onSuccess(List<String> downloadUrls);
        void onFailure(Exception e);
        void onProgress(int progress);
    }
    List<Uri> imageUris;
    private int[] sliderImages = {
            R.drawable.wedding,
            R.drawable.party,
            R.drawable.meeting,
            R.drawable.birthday
    };
    int id=0;
    ImageView one,two,three,four,five,six;
    ArrayList<String>sliderImageUrl=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin_home, container, false);
        viewPager =view.findViewById(R.id.viewPager);
        Button uploadButton=view.findViewById(R.id.postBanners);
        one=view.findViewById(R.id.imageOne);
        two=view.findViewById(R.id.imageTwo);
        three=view.findViewById(R.id.imageThree);
        four=view.findViewById(R.id.imageFour);
        imageUris = new ArrayList<>();
        five=view.findViewById(R.id.imageFive);
        six=view.findViewById(R.id.imageSix);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        ProgressBar progressBar=view.findViewById(R.id.spin_kit);
        RadioButton company=view.findViewById(R.id.company);
        RadioButton person=view.findViewById(R.id.person);
        //----------------------------------------start slider Code----------------------------------
        // Auto sliding
        db.collection("Banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ArrayList<String> images = (ArrayList<String>) snapshot.get("banner");
                            if (images != null) {
                                for (String eType : images) {
                                    sliderImageUrl.add(eType);
                                }
                                ImageSliderAdapter adapter = new ImageSliderAdapter(getActivity(), sliderImageUrl);
                                viewPager.setAdapter(adapter);
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
                if (currentPage == sliderImages.length) {
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
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=view.getId();
                checkPermissionsAndOpenGallery();
            }
        });



uploadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uploadImages(imageUris, new UploadCallback() {
                @Override
                public void onSuccess(List<String> downloadUrls) {
                    HashMap<String,Object>banner=new HashMap<>();
                    progressBar.setVisibility(View.GONE);
                    banner.put("banner",downloadUrls);
                    if (company.isChecked()){
                        banner.put("to","company");
                    }
                     if (person.isChecked()){
                        banner.put("to","person");
                    }
                   if (!person.isChecked()&&!company.isChecked()){
                        Toast.makeText(getActivity(), "Please Select One of the Two Destinations", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    db.collection("Banner").add(banner).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
if (task.isSuccessful()){
    Toast.makeText(getActivity(), "Banner Posted", Toast.LENGTH_SHORT).show();
}
                        }
                    });}
                }
                @Override
                public void onFailure(Exception e) {
                    // Handle the failure or display an error message
                }
                @Override
                public void onProgress(int progress) {
                   progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    });

        return view;
    }
    private void uploadImages(List<Uri> imageUris, UploadCallback callback) {
        List<String> downloadUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(imageUris.size());
        for (Uri imageUri : imageUris) {
            String imageName = UUID.randomUUID().toString(); // Generate a unique name for each image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("bannerImages/" + imageName);
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
           imageUris.add(imageUri);
           switch (id){
               case R.id.imageOne:
                   one.setImageURI(imageUri);
                   break;
               case R.id.imageTwo:
                   two.setImageURI(imageUri);
                   break;
               case R.id.imageThree:
                   three.setImageURI(imageUri);
                   break;
               case  R.id.imageFour:
                   four.setImageURI(imageUri);
                   break;
               case  R.id.imageFive:
                   five.setImageURI(imageUri);
                   break;
               case  R.id.imageSix:
                   six.setImageURI(imageUri);
                   break;

            }

        }
    }

}