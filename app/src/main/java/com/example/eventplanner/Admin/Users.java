package com.example.eventplanner.Admin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.Adapters.AdminHomeFragmentAdapter;
import com.example.eventplanner.R;
import com.google.android.material.tabs.TabLayout;

public class Users extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 slideHome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        slideHome = view.findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    AdminHomeFragmentAdapter adapter =
        new AdminHomeFragmentAdapter(fragmentManager, getLifecycle());
        slideHome.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.company));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                slideHome.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        slideHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });






        return view;
    }
}