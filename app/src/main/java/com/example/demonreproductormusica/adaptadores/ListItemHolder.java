package com.example.demonreproductormusica.adaptadores;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demonreproductormusica.R;
import com.example.demonreproductormusica.db.DBPlaylist;
import com.example.demonreproductormusica.entidades.ListItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListItemHolder extends RecyclerView.ViewHolder {

    String state_item;

    // view lista_listItem's attributes
    TextView textview_title, textview_subtitle;

    // add song to playlist
    BottomSheetDialog bottomSheetDialog;
    SearchView searchView;
    RecyclerView recyclerViewItems;

    // dependiendo del estado podremos hacer o no, ciertas acciones
    public void setState_item(String state_item) {
        this.state_item = state_item;
    }

    public ListItemHolder(final View itemView) {
        super(itemView);

        textview_title = itemView.findViewById(R.id.textview_title);
        textview_subtitle = itemView.findViewById(R.id.textview_subtitle);

        // menu flotante
        final Button button_menu = itemView.findViewById(R.id.button_menu);
        final PopupMenu popupMenu = new PopupMenu(itemView.getContext(), button_menu);

        // ==================================================================================
        // mnavigate to fragment REPRODUCTOR
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[STATE_ITEM]", state_item);
                // navigate_to_reproductor(v);
            }
        });

        // ==================================================================================
        // menu flotante
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_add:
                        show_bottomSheet_to_add_song(itemView);
                        break;
                    case R.id.popup_song_delete:
                        Log.d("[popupMenu]", "popup_song_delete" + item);
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

    // methods action ================================================================================

    // navigate_to_reproductor // ================================================================================
    public void navigate_to_reproductor(View v) {
        final NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.action_nav_bibloteca_to_playlistFragment);
    }

    // add song to playlist // ================================================================================
    private void show_bottomSheet_to_add_song(final View itemView) {
        bottomSheetDialog = new BottomSheetDialog(itemView.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_playlists);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        searchView = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_searchView);
        recyclerViewItems = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_recyclerview);
        // add this to fix --->
        LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext());
        recyclerViewItems.setLayoutManager(manager);
        recyclerViewItems.setHasFixedSize(true);

        //llenamos con las playlist el modal
        recyclerViewItems.setAdapter(
                new ListItemAdapter(
                        new DBPlaylist(itemView.getContext()).
                                get_all_laylist()
                )
        );

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

        return new ListItemAdapter(arrayList);
    }
}