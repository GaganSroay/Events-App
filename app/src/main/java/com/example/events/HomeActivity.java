package com.example.events;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.events.Components.C;
import com.example.events.Form.CreateForm;
import com.example.events.authentication.Login;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.ui.HomeActivity.SectionsPagerAdapter;
import com.example.events.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

private ActivityHomeBinding binding;

    private static final int TAB_SELECTED_COLOR = R.color.cyan_1;
    private static final int TAB_DESELECTED_COLOR = R.color.cyan_2;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        System.out.println("NUMBER IS "+ C.getNumber(this));
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);

        binding.tabs.setupWithViewPager(binding.viewPager);
        binding.tabs.getTabAt(0).setIcon(R.drawable.icon_joined_events).getIcon().setColorFilter(getResources().getColor(TAB_SELECTED_COLOR), PorterDuff.Mode.SRC_IN);;
        binding.tabs.getTabAt(1).setIcon(R.drawable.icon_organised).getIcon().setColorFilter(getResources().getColor(TAB_DESELECTED_COLOR), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(2).setIcon(R.drawable.icon_invitations).getIcon().setColorFilter(getResources().getColor(TAB_DESELECTED_COLOR), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(3).setIcon(R.drawable.icon_profile).getIcon().setColorFilter(getResources().getColor(TAB_DESELECTED_COLOR), PorterDuff.Mode.SRC_IN);
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) { tab.getIcon().setColorFilter(getResources().getColor(TAB_SELECTED_COLOR), PorterDuff.Mode.SRC_IN); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { tab.getIcon().setColorFilter(getResources().getColor(TAB_DESELECTED_COLOR), PorterDuff.Mode.SRC_IN); }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
        System.out.println("NUMBER IS "+ C.getNumber(this));

        startActivity(new Intent(this, CreateForm.class));

    }
}