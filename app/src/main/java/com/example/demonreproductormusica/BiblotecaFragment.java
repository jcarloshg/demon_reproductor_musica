package com.example.demonreproductormusica;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.demonreproductormusica.adaptadores.ListItemAdapter;
import com.example.demonreproductormusica.db.DBPlaylist;
import com.example.demonreproductormusica.entidades.ListItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    SearchView searchView;

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
        context = view.getContext();
        arrayList = new ArrayList<>(Arrays.asList(myStringArray));

        // ===================================================================================
        // initialize recyclerViewItems
        recyclerViewItems = view.findViewById(R.id.recycler_view);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(null));
        this.get_all_items(view); // llenamos la list

        // ===================================================================================
        // create searchView and listener searchView
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return search_playlists(view, s);
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return search_playlists(view, s);
            }
        });

        // ===================================================================================
        // create new list
        btn_create_list = view.findViewById(R.id.button_crate_list);
        btn_create_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_bottom_sheet_create_list(v);
            }
        });


        // ==========================
        // pruebasSSSSSSSSS
        btn_getmusic = view.findViewById(R.id.btn_prueba);
        btn_getmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllFilesMp3(v);
            }
        });

        return view;
    }

    private boolean search_playlists(View view, String s){
        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext()); //creamos el objeto de la consulta
        ArrayList<ListItem> arrayList =  dbPlaylist.get_playlist_for_name(s); // ejecutamos query y btenemos resultado
        recyclerViewItems.setAdapter(new ListItemAdapter(arrayList)); // ponemos el resultado en el componete lista

        if (s.equals("")) get_all_items(view);
        return  false;
    }

    private void get_all_items(View view){
        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext());
        arrayList_item = dbPlaylist.get_all_laylist();
        ListItemAdapter listItemAdapter = new ListItemAdapter(arrayList_item);
        recyclerViewItems.setAdapter(listItemAdapter);
    }

    private void show_bottom_sheet_create_list(View view){
        bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_crate_list);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        editText_name = bottomSheetDialog.findViewById(R.id.editText_name);
        Button button_submit = bottomSheetDialog.findViewById(R.id.button_submit);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_playlist_name = editText_name.getText().toString();

                DBPlaylist dbPlaylist = new DBPlaylist(v.getContext());
                long id_new_playlist = dbPlaylist.insert_name_playlist(new_playlist_name);

                if (id_new_playlist != -1){
                    Toast.makeText(v.getContext(), "Playlist " + new_playlist_name + " creada!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(v.getContext(), "Err al crear Playlist", Toast.LENGTH_LONG).show();

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
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

        for (int i = 0; i < num_items; i++)
            items.add(new ListItem(i, Integer.toString(i), Integer.toString(i)));

        return items;
    }

}