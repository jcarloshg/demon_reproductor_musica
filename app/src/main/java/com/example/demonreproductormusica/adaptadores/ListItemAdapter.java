package com.example.demonreproductormusica.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demonreproductormusica.R;
import com.example.demonreproductormusica.db.DBPlaylist;
import com.example.demonreproductormusica.entidades.ListItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemHolder> {

    ArrayList<ListItem> lista_listItem;

    public ListItemAdapter(ArrayList<ListItem> listItem) {
        this.lista_listItem = listItem;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_items, null, false
        );
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        holder.textview_title.setText(lista_listItem.get(position).getTitle());
        holder.textview_subtitle.setText(lista_listItem.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return lista_listItem.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {

        TextView textview_title, textview_subtitle;

        BottomSheetDialog bottomSheetDialog;
        SearchView searchView;
        RecyclerView recyclerViewItems;

        public ListItemHolder(@NonNull final View itemView) {
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
                    navigate_to_reproductor(v);
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

        // add song to playlist
        private void show_bottomSheet_to_add_song(final View itemView) {
            bottomSheetDialog = new BottomSheetDialog(itemView.getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_playlists);
            bottomSheetDialog.setCanceledOnTouchOutside(true);

            recyclerViewItems = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_recyclerview);
            searchView = bottomSheetDialog.findViewById(R.id.bottom_sheet_playlist_searchView);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ListItemAdapter listItemAdapter = (s.equals(""))
                            ? get_all_items(itemView)
                            : search_playlists(itemView, s);
                    recyclerViewItems.setAdapter(listItemAdapter);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    ListItemAdapter listItemAdapter = (s.equals(""))
                            ? get_all_items(itemView)
                            : search_playlists(itemView, s);
                    recyclerViewItems.setAdapter(listItemAdapter);
                    return false;
                }
            });
            bottomSheetDialog.show();
        }

        private ListItemAdapter search_playlists(View view, String s) {
            DBPlaylist dbPlaylist = new DBPlaylist(view.getContext()); //creamos el objeto de la consulta
            ArrayList<ListItem> arrayList = dbPlaylist.get_playlist_for_name(s); // ejecutamos query y btenemos resultado
            return new ListItemAdapter(arrayList);
        }

        private ListItemAdapter get_all_items(View view) {
            DBPlaylist dbPlaylist = new DBPlaylist(view.getContext());
            ArrayList<ListItem> arrayList = dbPlaylist.get_all_laylist();
            return new ListItemAdapter(arrayList);
        }
    }

    // ================================================================================
    // methods action
    // ================================================================================

    // ================================================================================
    // navigate_to_reproductor
    public void navigate_to_reproductor(View v) {
        final NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.action_nav_bibloteca_to_playlistFragment);
    }

    // ================================================================================
    // select_option_popmenu
    /*
    public boolean select_option_popmenu(MenuItem item, View itemView) {
        switch (item.getItemId()) {
            case R.id.popup_song_add:
                show_bottomSheet_to_add_song(itemView);
                break;
            case R.id.popup_song_delete:
                Log.d("[popupMenu]", "popup_song_delete" + item);
        }
        return false;
    }
    */


}
