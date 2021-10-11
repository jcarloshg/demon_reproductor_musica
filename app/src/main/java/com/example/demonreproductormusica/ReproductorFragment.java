package com.example.demonreproductormusica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demonreproductormusica.db.DB;
import com.example.demonreproductormusica.db.DBSong;
import com.example.demonreproductormusica.entidades.ListItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReproductorFragment} factory method to
 * create an instance of this fragment.
 */
public class ReproductorFragment extends Fragment {

    ReproductorFragmentArgs reproductorFragmentArgs;
    ListItem song;

    // VIEW
    TextView tView_name, tView_artist;

    public ReproductorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init_components(View view) {
        tView_name = view.findViewById(R.id.tView_name);
        tView_artist = view.findViewById(R.id.tView_artist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reproductor, container, false);

        reproductorFragmentArgs = ReproductorFragmentArgs.fromBundle(getArguments());
        init_song(reproductorFragmentArgs, view);
        init_components(view);
        init_view(view);


        return view;
    }

    private void init_song(ReproductorFragmentArgs reproductorFragmentArgs, View view) {
        DBSong dbSong = new DBSong(view.getContext());
        song = dbSong.get_song_by_id(Integer.valueOf(reproductorFragmentArgs.getIdSong()));
        song.setId_auxiliary(Integer.valueOf(reproductorFragmentArgs.getIdPlaylist()));
        // missing assign the TYPE to
    }

    private void init_view(View view) {
        tView_name.setText(song.getTitle());
        tView_artist.setText(song.getSubtitle());
    }

}