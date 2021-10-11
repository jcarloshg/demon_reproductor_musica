package com.example.demonreproductormusica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demonreproductormusica.db.DBPlaylist;
import com.example.demonreproductormusica.db.DBPlaylistSong;
import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {

    // types of view of fragment
    public static final String TYPE_PLAYLIST = "TYPE_PLAYLIST";
    public static final String TYPE_ALBUM = "TYPE_ALBUM";
    public static final String TYPE_ARTIST = "TYPE_ARTIST";

    //atributes
    String id_playlist;
    ArrayList<ListItem> playlist; // this save songs

    TextView playlist_title;
    TextView textView_subtitle;
    RecyclerView playlist_recyclerView;

    PlaylistFragmentArgs playlistFragmentArgs;
    ListItem playlist_info = null;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init_components(View view) {
        playlist_title = view.findViewById(R.id.playlist_title);
        textView_subtitle = view.findViewById(R.id.textView_subtitle);

        // add this to fix -> "E/RecyclerView: No adapter attached; skipping layout"
        playlist_recyclerView = view.findViewById(R.id.playlist_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        playlist_recyclerView.setLayoutManager(manager);
        playlist_recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        init_components(view);
        playlistFragmentArgs = PlaylistFragmentArgs.fromBundle(getArguments());
        init_view(view);
        return view;
    }

    private void init_view(View view) {
        id_playlist = playlistFragmentArgs.getIdPlaylist();

        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext());
        playlist_info = dbPlaylist.get_playlist_info(id_playlist);

        playlist_title.setText(playlist_info.getTitle());
        if (playlist_info.getSubtitle() == null)
            textView_subtitle.setText("");
        else
            textView_subtitle.setText(playlist_info.getSubtitle());

        DBPlaylistSong dbPlaylistSong = new DBPlaylistSong(view.getContext());
        playlist = dbPlaylistSong.get_playlist(playlist_info.getId());

        for (ListItem item: playlist) {
            Log.i("[NAME_SONG]", "init_view: " + item.getTitle());
        }
        
    }

}