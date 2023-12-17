package com.example.eventplanner.User.User_Landing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eventplanner.Adapters.ImageSliderAdapter;
import com.example.eventplanner.R;
import com.example.eventplanner.User.User_HHP.UserHHP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends Fragment {
    private static final long SLIDER_DELAY = 3000; // Delay between slides (in milliseconds)
    private ViewPager viewPager;
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 500; // Delay before starting the auto slide (in milliseconds)
    private final long PERIOD_MS = 3000; // Interval for auto sliding (in milliseconds)

    private int[] sliderImages = {
            R.drawable.wedding,
            R.drawable.party,
            R.drawable.meeting,
            R.drawable.birthday
    };
    ArrayList<String> sliderImageUrl=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        viewPager =view.findViewById(R.id.viewPager);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
    Button btnWedding=view.findViewById(R.id.imgButtonWedding);
       Button btnBirthDay=view.findViewById(R.id.imgButtonBirthDay);
       Button btnParty=view.findViewById(R.id.imgButtonParty);
       Button btnMeeting=view.findViewById(R.id.imgButtonMeeting);
        //----------------------------------------start slider Code----------------------------------
        // Auto sliding
        db.collection("Banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.get("to").equals("person")) {
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
 //----------------------------------------end slider Code----------------------------------
btnWedding.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
goToUserHHP("Wedding");
    }
});
        btnMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
goToUserHHP("Meeting");
            }
        });
        btnBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
goToUserHHP("Birthday");
            }
        });
        btnParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
goToUserHHP("Party");
            }
        });
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
    public void goToUserHHP(String title){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("title",title);
        editor.commit();
        Intent intent=new Intent(getActivity(), UserHHP.class);
        startActivity(intent);
        getActivity().finish();
    }

}