package com.example.demonreproductormusica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    // permmission to get files audio at device
    public static final int PERMIT = 1;
    Context context;

    // navigation
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment_reproductor, fragment_bibloteca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // permission
        getPermission();
        context = getApplicationContext();

        // navigation
        fragment_reproductor = new ReproductorFragment();
        fragment_bibloteca = new BiblotecaFragment();
        ((BiblotecaFragment) fragment_bibloteca).setContext(context);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_reproductor);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_bibloteca).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bibloteca:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_bibloteca).commit();
                        //Navigation.findNavController(view).navigate(R.id.fragment_bibloteca);
                        break;
                    case R.id.nav_reproductor:
                        // Navigation.findNavController(view).navigate(R.id.fragment_reproductor);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, fragment_reproductor).commit();
                        break;
                }
                return true;
            }
        });

    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    AlertDialog.Builder alertb = new AlertDialog.Builder(MainActivity.this);
                    alertb.setTitle("Por favor consede los permisos");
                    alertb.setMessage("Permisos para el acceso a los archivos");
                    alertb.setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    PERMIT);
                        }
                    });
                    alertb.setNegativeButton("Cancelar", null);
                    AlertDialog alertDialog = alertb.create();
                    alertDialog.show();
                }else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMIT);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMIT:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            }
        }

    }


}