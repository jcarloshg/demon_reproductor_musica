package com.example.demonreproductormusica;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demonreproductormusica.db.*;
import com.example.demonreproductormusica.entidades.*;
import com.example.demonreproductormusica.adaptadores.ListItemAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiblotecaFragment} factory method to
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
    Button get_songs, button_playlist;

    // elements of view Library
    RecyclerView recyclerViewItems;

    // to search
    public static final String SONG = "SONG";
    public static final String PLAYLIST = "PLAYLIST";
    public static final String ALBUM = "ALBUM";
    public static final String ARTIST = "ARTIST";
    String search_by = PLAYLIST;

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
        recyclerViewItems = view.findViewById(R.id.recycler_view_playlist);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(null));

        // ===================================================================================
        // llenamos con las playlist
        ArrayList<ListItem> list = new DBPlaylist(view.getContext()).get_all_laylist();
        for (ListItem item : list) item.setTYPE(ListItem.ITEM_PLAYLIST_LIST);
        recyclerViewItems.setAdapter(new ListItemAdapter(list));

        // ===================================================================================
        // create searchView and listener searchView
        searchView = view.findViewById(R.id.search__playlist);
        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListItemAdapter listItemAdapter = search(view, s);
                recyclerViewItems.setAdapter(listItemAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ListItemAdapter listItemAdapter = search(view, s);
                recyclerViewItems.setAdapter(listItemAdapter);
                return false;
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

        // ===================================================================================
        // get songs
        get_songs = view.findViewById(R.id.get_songs);
        get_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_by = SONG;
                searchView.setQueryHint("Buscar canciones");
                ListItemAdapter listItemAdapter = search_song(v, "");
                recyclerViewItems.setAdapter(listItemAdapter);
            }
        });

        button_playlist = view.findViewById(R.id.button_playlist);
        button_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_by = PLAYLIST;
                searchView.setQueryHint("Buscar playlist");
                ListItemAdapter listItemAdapter = search_playlists(v, "");
                recyclerViewItems.setAdapter(listItemAdapter);
            }
        });

        return view;
    }

    private ListItemAdapter search(View view, String s){
        ListItemAdapter listItemAdapter = null;
        if (search_by.equals(SONG)){
            listItemAdapter = search_song(view, s);
        }
        if (search_by.equals(PLAYLIST)){
            listItemAdapter = search_playlists(view, s);
        }

        return listItemAdapter;
    }

    private ListItemAdapter search_song(View view, String s) {
        DBSong dbSong = new DBSong(view.getContext());
        ArrayList<ListItem> songs = s.equals("")
                ? dbSong.getAllFilesMp3()
                : dbSong.get_song_by_name(s);
        return new ListItemAdapter(songs);
    }

    private ListItemAdapter search_playlists(View view, String s) {
        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext()); //creamos el objeto de la consulta
        ArrayList<ListItem> arrayList = s.equals("")
                ? dbPlaylist.get_all_laylist() // ejecutamos query y btenemos resultado
                : dbPlaylist.get_playlist_for_name(s);

        return new ListItemAdapter(arrayList);
    }


    private void show_bottom_sheet_create_list(View view) {
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

                if (id_new_playlist != -1) {
                    Toast.makeText(v.getContext(), "Playlist " + new_playlist_name + " creada!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(v.getContext(), "Err al crear Playlist", Toast.LENGTH_LONG).show();

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


}