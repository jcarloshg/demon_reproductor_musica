package com.example.demonreproductormusica;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demonreproductormusica.db.DB;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ConfiguracionFragment extends Fragment {

    LinearLayout link_instagram;
    TextView tView_delete_data;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init_components(View view) {
        link_instagram = view.findViewById(R.id.link_instagram);
        tView_delete_data = view.findViewById(R.id.tView_delete_data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        init_components(view);

        init_link_instagram_onClick(view);
        init_tView_delete_data(view);

        return view;
    }

    private void init_tView_delete_data(View view) {
        tView_delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertb = new AlertDialog.Builder(v.getContext());
                alertb.setTitle("Borrar información");
                alertb.setMessage("Unicamente las playlist serán borradas, archivos de audio se conservan en el dispositivo");
                alertb.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drop_data_of_DB(v);
                        Toast.makeText(v.getContext(), "Datos borrados correctamente.", Toast.LENGTH_LONG).show();
                    }
                });
                alertb.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = alertb.create();
                alertDialog.show();
            }
        });
    }

    private void drop_data_of_DB(View v){
        DB db = new DB(v.getContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        db.onUpgrade(sqLiteDatabase, db.DATABASE_VERSION, db.DATABASE_VERSION +1);

    }

    private void init_link_instagram_onClick(View view) {
        link_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url_instagram = "https://www.instagram.com/hg.jcarlos/";
                    // String url_instagram = "https://translate.google.com/";
                    Uri uri_instagram = Uri.parse(url_instagram);
                    Intent intent_instagram = new Intent(Intent.ACTION_VIEW, uri_instagram);

                    // intent_instagram.setPackage("com.chrome.android");
                    startActivity(intent_instagram);
                }catch (ActivityNotFoundException e) {
                    Log.e("[init_link_instagram_onClick]", e.getMessage());
                }
            }
        });
    }
}