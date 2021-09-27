package com.example.demonreproductormusica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment_reproductor, fragment_bibloteca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_reproductor = new ReproductorFragment();
        fragment_bibloteca = new BiblotecaFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_reproductor);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_reproductor).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bibloteca:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_bibloteca).commit();
                        // Navigation.findNavController(view).navigate(R.id.fragment_bibloteca);
                        break;
                    case R.id.nav_reproductor:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_reproductor).commit();
                        break;
                }
                return true;
            }
        });

    }
}