package com.example.demonreproductormusica.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demonreproductormusica.BiblotecaFragmentDirections;
import com.example.demonreproductormusica.*;
import com.example.demonreproductormusica.R;
import com.example.demonreproductormusica.ReproductorFragment;
import com.example.demonreproductormusica.db.DBCurrentPlaylist;
import com.example.demonreproductormusica.db.*;
import com.example.demonreproductormusica.entidades.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListItemHolder extends RecyclerView.ViewHolder {

    // properties auxiliary
    String state_item;
    ListItem listItem;

    // view lista_listItem's attributes
    TextView textview_title, textview_subtitle;

    // add song to playlist
    BottomSheetDialog bottomSheetDialog;
    SearchView searchView;
    RecyclerView recyclerViewItems;

    // menu flotante
    Button button_menu;
    PopupMenu popupMenu;

    public void setListItem(ListItem listItem) {
        this.listItem = listItem;
    }

    // dependiendo del estado podremos hacer o no, ciertas acciones
    public void setState_item(String state_item) {
        this.state_item = state_item;

        // menu flotante -> dependiendo del estado podremos abrir diferentes menus
        // https://stackoverflow.com/questions/27895108/nullpointerexception-attempt-to-invoke-virtual-method-boolean-java-lang-string
        if (this.state_item != null && this.state_item.equals("ITEM_PLAYLIST_LIST"))
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_playlist, popupMenu.getMenu());
        if (this.state_item != null && state_item.equals(ListItem.ITEM_PLAYLIST_VIEW)) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_playlist, popupMenu.getMenu());
            init_state_ITEM_PLAYLIST_VIEW();
        }
        if (this.state_item != null && state_item.equals(ListItem.ITEM_SONG_LIST))
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song, popupMenu.getMenu());
        if (this.state_item != null && state_item.equals(ListItem.ITEM_SONG_VIEW))
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song, popupMenu.getMenu());

    }

    private void init_state_ITEM_PLAYLIST_VIEW() {
        button_menu.setVisibility(View.INVISIBLE);
    }

    private void init(View itemView) {
        // view lista_listItem's attributes
        textview_title = itemView.findViewById(R.id.textview_title);
        textview_subtitle = itemView.findViewById(R.id.textview_subtitle);

        // add song to playlist
        bottomSheetDialog = new BottomSheetDialog(itemView.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_playlists);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        searchView = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_searchView);
        recyclerViewItems = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_recyclerview);

        // menu flotante
        button_menu = itemView.findViewById(R.id.button_menu);
        popupMenu = new PopupMenu(itemView.getContext(), button_menu);
    }

    public ListItemHolder(final View itemView) {
        super(itemView);

        init(itemView); // init widgets of list_items layout

        // setOnClickListener // ==================================================================================
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state_item != null && state_item.equals(ListItem.ITEM_PLAYLIST_LIST))
                    navigate_to_playlist(itemView);

                if (state_item != null && state_item.equals(ListItem.ITEM_PLAYLIST_VIEW))
                    insert_song_to_playlist(v);

                if (state_item != null && state_item.equals(ListItem.ITEM_SONG_LIST))
                    navigate_to_reproductor(itemView);
//
//                if (state_item != null && state_item.equals(ListItem.ITEM_SONG_VIEW))

            }
        });

        // menu flotante // ==================================================================================
        init_meu_float(itemView);
    }

    private void navigate_to_reproductor(View itemView) {

        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(
                ReproductorFragment.SP_REPRODUCTOR_NAME, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // establecemos que canción es la actua y que playlist es la actual
        editor.putString("song_id", Integer.toString(listItem.getId()));
        editor.putString("playlist_id", Integer.toString(listItem.getId_auxiliary()));
        editor.apply();

        //===========================================
        // process to put the new current playlist
        // 1 - get id_sogn of playlist
        DBPlaylistSong  dbPlaylistSong = new DBPlaylistSong(itemView.getContext());
        ArrayList<Integer> id_song_playlist = dbPlaylistSong.get_id_songs_of_playlist(
                listItem.getId_auxiliary() // its the id playlist,
        );
        DBCurrentPlaylist dbCurrentPlaylist = new DBCurrentPlaylist(itemView.getContext());
        // 2 - delete current playlist
        dbCurrentPlaylist.delete_from_currentplaylist();
        // 3 - insert the new current_playlist
        for (Integer id_song_mediaplayer: id_song_playlist) {
            dbCurrentPlaylist.insert_id_mediaplayer_song(id_song_mediaplayer);
        }

        Navigation.findNavController(itemView).navigate(R.id.action_playlistFragment_to_nav_reproductor);
    }

    public void navigate_to_playlist(View v) {
        NavDirections navDirections = BiblotecaFragmentDirections.actionNavBiblotecaToPlaylistFragment(
                Integer.toString(listItem.getId()),
                PlaylistFragment.TYPE_ARTIST
        );
        Navigation.findNavController(v).navigate(navDirections);
    }

    private void insert_song_to_playlist(View view) {
        DBPlaylistSong dbPlaylistSong = new DBPlaylistSong(view.getContext());

        long id_new_insert = dbPlaylistSong.insert_song_to_playlist(listItem.getId(), listItem.getId_auxiliary());

        if (id_new_insert != -1) {
            Toast.makeText(view.getContext(), "Canción agregada correctamente", Toast.LENGTH_LONG).show();
            bottomSheetDialog.dismiss();
        } else
            Toast.makeText(view.getContext(), "Err, no se puede añadir canción", Toast.LENGTH_LONG).show();
    }

    private void init_meu_float(final View itemView) {
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_playlist_delete:
                        Log.d("[popupMenu]", "popup_playlist_delete" + item);
                        break;
                    case R.id.popup_song_add:
                        show_bottomSheet_to_add_song(itemView);
                        break;
                    case R.id.popup_song_delete:
                        Log.d("[popupMenu]", "popup_song_delete" + item);
                        break;
                }
                return false;
            }
        });
        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    private void show_bottomSheet_to_add_song(final View itemView) {
        // add this to fix -> "E/RecyclerView: No adapter attached; skipping layout"
        LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext());
        recyclerViewItems.setLayoutManager(manager);
        recyclerViewItems.setHasFixedSize(true);

        //llenamos con las playlist el modal
        ArrayList<ListItem> arrayList = new DBPlaylist(itemView.getContext()).get_all_laylist();
        for (ListItem item : arrayList) { // sets the TYPE and the id song that called the modal
            item.setTYPE(ListItem.ITEM_PLAYLIST_VIEW);
            // a cada playlist le pasamos el id de la canción que llamo esta accion.
            item.setId_auxiliary(listItem.getId());
        }
        recyclerViewItems.setAdapter(new ListItemAdapter(arrayList));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListItemAdapter listItemAdapter = search_playlists(itemView, s);
                recyclerViewItems.setAdapter(listItemAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ListItemAdapter listItemAdapter = search_playlists(itemView, s);
                recyclerViewItems.setAdapter(listItemAdapter);
                return false;
            }
        });
        bottomSheetDialog.show();
    }

    private ListItemAdapter search_playlists(View view, String s) {

        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext()); //creamos el objeto de la consulta
        ArrayList<ListItem> arrayList = s.equals("")
                ? dbPlaylist.get_all_laylist() // ejecutamos query y btenemos resultado
                : dbPlaylist.get_playlist_for_name(s);

        for (ListItem item : arrayList) item.setTYPE(ListItem.ITEM_PLAYLIST_VIEW);

        return new ListItemAdapter(arrayList);
    }
}