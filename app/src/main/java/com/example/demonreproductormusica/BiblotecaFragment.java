package com.example.demonreproductormusica;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demonreproductormusica.adaptadores.ListItemAdapter;
import com.example.demonreproductormusica.db.DBPlaylist;
import com.example.demonreproductormusica.entidades.ListItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    String[] myStringArray = new String[]{};
    ArrayList<String> arrayList;
    Uri uri;


    Button btn_create_list;
    BottomSheetDialog bottomSheetDialog;
    EditText editText_name;

    // pruebas
    Button btn_getmusic;

    // elements of view Library
    RecyclerView recyclerViewItems;
    ArrayList<ListItem> arrayList_item;

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

        // ===================================================================================
        // create new list
        btn_create_list = view.findViewById(R.id.button_crate_list);
        btn_create_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_crate_list);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                editText_name = bottomSheetDialog.findViewById(R.id.editText_name);
                Button button_submit = bottomSheetDialog.findViewById(R.id.button_submit);

                button_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new_playlist_name = editText_name.getText().toString();

                        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext());
                        long id_new_playlist = dbPlaylist.insert_name_playlist(new_playlist_name);

                        if (id_new_playlist != -1)
                            Toast.makeText(view.getContext(), "PlaylistFragment " + new_playlist_name + " creada!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(view.getContext(), "Err al crear PlaylistFragment", Toast.LENGTH_LONG).show();
                    }
                });

                bottomSheetDialog.show();
            }
        });


        // iniciañizate recyclerViewItems
        recyclerViewItems = view.findViewById(R.id.recycler_view);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(null));

        arrayList_item = new DBPlaylist(view.getContext()).get_all_laylist();
        ListItemAdapter listItemAdapter = new ListItemAdapter(arrayList_item);
        recyclerViewItems.setAdapter(listItemAdapter);

        return view;
    }


    // ========================================================
    // metodos de prueba
    public void getAllFilesMp3(View view) {
        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            Toast.makeText(null, "algo salio mal :(", Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(null, "no hay musica", Toast.LENGTH_SHORT).show();
        } else {
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                String name = cursor.getString(title);
                arrayList.add(name);
            } while (cursor.moveToNext());
        }

        for (String item : arrayList) {
            Log.e("[PRUEBLA]", item);
        }
    }

    public ArrayList<ListItem> createList(int num_items) {
        ArrayList<ListItem> items = new ArrayList<>();

        for (int i = 0; i < num_items; i++) {
            items.add(new ListItem(i, Integer.toString(i), Integer.toString(i)));
        }

        return items;
    }

}