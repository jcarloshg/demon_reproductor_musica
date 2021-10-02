package com.example.demonreproductormusica;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiblotecaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiblotecaFragment extends Fragment {

    // permmission to get files audio at device
    ContentResolver contentResolver;
    Cursor cursor;
    Context context;
    String [] myStringArray = new String[] {};
    ArrayList <String> arrayList;
    Uri uri;

    Button btn_getmusic;

    public void setContext(Context context){
        this.context = context;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BiblotecaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BiblotecaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BiblotecaFragment newInstance(String param1, String param2) {
        BiblotecaFragment fragment = new BiblotecaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_bibloteca, container, false);

        // permissions
        arrayList = new ArrayList<>(Arrays.asList(myStringArray));

        //listener on clicks
        btn_getmusic = view.findViewById(R.id.btn_prueba);
        btn_getmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllFilesMp3(v);
            }
        });

        return view;
    }

    public void getAllFilesMp3(View view){
        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null){
            Toast.makeText( null, "algo salio mal :(", Toast.LENGTH_SHORT).show();
        }else if(!cursor.moveToFirst()) {
            Toast.makeText( null, "no hay musica", Toast.LENGTH_SHORT).show();
        } else {
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                String name = cursor.getString(title);
                arrayList.add(name);
            }while (cursor.moveToNext());
        }

        for (String item: arrayList) {
            Log.e("[PRUEBLA]", item);
        }
    }

}