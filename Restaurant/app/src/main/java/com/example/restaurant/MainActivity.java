package com.example.restaurant;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        /*findViewById(R.id.button13).setOnClickListener(v -> replaceFragment(new FragmentGestion1()));
        findViewById(R.id.button14).setOnClickListener(v -> replaceFragment(new FragmentGestion2()));
        findViewById(R.id.button15).setOnClickListener(v -> replaceFragment(new FragmentGestion3()));
        findViewById(R.id.button16).setOnClickListener(v -> replaceFragment(new FragmentGestion4()));
        findViewById(R.id.button17).setOnClickListener(v -> replaceFragment(new FragmentGestion5()));*/
        findViewById(R.id.button18).setOnClickListener(v -> replaceFragment(new FragmentGestion6()));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}