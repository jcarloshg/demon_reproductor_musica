package com.example.demonreproductormusica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.demonreproductormusica.db.DB;
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

        bottomNavigationView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_reproductor, R.id.nav_bibloteca, R.id.nav_configuration)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // add this line to see fragment name in ActionBar
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setSelectedItemId(R.id.nav_reproductor);

        /*
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bibloteca:
                        break;
                    case R.id.nav_reproductor:
                        break;
                }
                return true;
            }
        });
         */


        //Create DB
        DB db = new DB(this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        if (sqLiteDatabase != null)
            Toast.makeText(this, "DB created", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "DB not created", Toast.LENGTH_LONG).show();
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                } else {
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

        switch (requestCode) {
            case PERMIT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ;
            }
        }

    }


}