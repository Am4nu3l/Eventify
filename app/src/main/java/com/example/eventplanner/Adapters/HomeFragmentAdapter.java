package com.example.eventplanner.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.User.User_HHP.Halls;
import com.example.eventplanner.User.User_HHP.Hotels;
import com.example.eventplanner.User.User_HHP.Parks;

public class HomeFragmentAdapter extends FragmentStateAdapter {

    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
        return new Halls();
            case 2:
        return new Parks();
    }
    return new Hotels();
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
