package com.example.demonreproductormusica;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.demonreproductormusica.db.*;
import com.example.demonreproductormusica.db.DBSong;

import com.example.demonreproductormusica.entidades.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ReproductorFragment extends Fragment {

    // shared preferences
    public static final String SP_REPRODUCTOR_NAME = "SP_REPRODUCTOR_NAME";
    public static final String SP_REPRODUCTOR_ID_SONG = "SP_REPRODUCTOR_ID_SONG";
    public static final String SP_REPRODUCTOR_ID_PLAYLIST = "SP_REPRODUCTOR_ID_PLAYLIST";

    // ReproductorFragmentArgs reproductorFragmentArgs;
    ListItem song;

    ArrayList<Uri> arrayList_uris = new ArrayList<>(); // playlist on reproductor

    // VIEW
    TextView tView_name, tView_artist, tview_time_start, tview_time_end;
    ImageView iView_play, iView_next, iView_previous, iView_pause, iView_current_playlist,
            iView_favorite;
    private SeekBar seekBar;
    MediaPlayer mediaPlayer;

    private Handler myHandler = new Handler();

    public static int oneTimeOnly = 0;
    private double startTime = 0;
    private double finalTime = 0;

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
        iView_favorite = view.findViewById(R.id.iView_favorite);

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setClickable(false);

        iView_play = view.findViewById(R.id.iView_play);
        iView_pause = view.findViewById(R.id.iView_pause);
        iView_next = view.findViewById(R.id.iView_next);
        iView_previous = view.findViewById(R.id.iView_play_previous);

        tview_time_start = view.findViewById(R.id.tview_time_start);
        tview_time_end = view.findViewById(R.id.tview_time_end);

        iView_current_playlist = view.findViewById(R.id.iView_current_playlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reproductor, container, false);

        init_song(view);

        DBSong dbSong = new DBSong(view.getContext());
        String uri_song = dbSong.get_uri(song.getId());
        mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(uri_song));

        init_components(view);
        update_view(view);

        init_control_play(view);
        init_control_pause(view);
        init_control_netxt(view);
        init_control_previous(view);

        check_song_is_on_favorites(view);

        init_iView_current_playlist_setOnClick();

        init_addRemove_from_favorits();

        return view;
    }

    private void init_addRemove_from_favorits() {
        iView_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exist_song_on_favorites = is_on_favorites(v, song.getId());

                if (exist_song_on_favorites)
                    remove_to_favorite(v, song.getId());
                else
                    add_to_favorites(v, song.getId());

                check_song_is_on_favorites(v);
            }
        });
    }

    private void add_to_favorites(View v, int id) {
        DBPlaylist dbPlaylist = new DBPlaylist(v.getContext());
        int id_playlist_favorite = dbPlaylist.get_id_playlist_by_name(DB.TABLE_NAME_FAVORITES);

        DBPlaylistSong dbPlaylistSong = new DBPlaylistSong(v.getContext());
        long id_insert = dbPlaylistSong.insert_song_to_playlist(id_playlist_favorite, id);

        String msg = (id_insert > -1)
                ? song.getTitle() + " agregda a favoritos"
                : song.getTitle() + " no se pudo agregar a favoritos";

        Toast.makeText(v.getContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    private void remove_to_favorite(View v, int id) {
        DBPlaylist dbPlaylist = new DBPlaylist(v.getContext());
        int id_playlist_favorite = dbPlaylist.get_id_playlist_by_name(DB.TABLE_NAME_FAVORITES);

        DBPlaylistSong dbPlaylistSong = new DBPlaylistSong(v.getContext());
        boolean is_remove_song = dbPlaylistSong.remove_song_from_playlist(id_playlist_favorite, id);

        String msg = (is_remove_song)
                ? song.getTitle() + " eliminada de favoritos"
                : song.getTitle() + " no se pudo quitar de favoritos";

        Toast.makeText(v.getContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    private boolean is_on_favorites(View view, int id_song) {
        DBPlaylist dbPlaylist = new DBPlaylist(view.getContext());
        int id_playlist_favorite = dbPlaylist.get_id_playlist_by_name(DB.TABLE_NAME_FAVORITES);

        DBPlaylistSong dbPlaylistSong = new DBPlaylistSong(view.getContext());
        boolean exist_song_on_favorites = dbPlaylistSong.check_exist_song_on_playlist(id_song, id_playlist_favorite);
        return exist_song_on_favorites;
    }

    private void check_song_is_on_favorites(View view) {

        boolean exist_song_on_favorites = is_on_favorites(view, song.getId());

        int id_resource_favorite = (exist_song_on_favorites)
                ? R.drawable.ic_favorite
                : R.drawable.ic_no_favorite;

        iView_favorite.setImageResource(id_resource_favorite);
    }

    private void init_iView_current_playlist_setOnClick() {
        iView_current_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections = ReproductorFragmentDirections.actionNavReproductorToPlaylistFragment(
                        Integer.toString(song.getId_auxiliary()),
                        PlaylistFragment.TYPE_ARTIST
                );
                Navigation.findNavController(view).navigate(navDirections);

                Log.i("[init_iView_current_playlist_setOnClick]", "ñsldfjsñldfj");
            }
        });
    }

    private void set_sharedPreferences(View view, int id, int id_auxiliary) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(
                ReproductorFragment.SP_REPRODUCTOR_NAME, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("song_id", "" + id);
        editor.putString("playlist_id", "" + id_auxiliary);
        editor.apply();
    }

    // if is_pass is +1 past to next song
    // if is_pass is -1 past to previous song
    private void change_song(View view, int is_pass) {
        DBSong dbSong = new DBSong(view.getContext());
        DBCurrentPlaylist dbCurrentPlaylist = new DBCurrentPlaylist(view.getContext());

        int id_current_song = dbCurrentPlaylist.get_IDSONG_on_CURRENTPLAYLIST_by_IDSONGMEDIAPLAYER(song.getId());
        int id_sig_idSongMediaPlayer = dbCurrentPlaylist.get_IDSONGMEDIAPLAYER_by_IDSONG(id_current_song + is_pass);

        if (id_sig_idSongMediaPlayer > -1) {
            String uri_song = dbSong.get_uri(id_sig_idSongMediaPlayer); // get path each song

            // establecemos que canción es la actua y que playlist es la actual
            set_sharedPreferences(view, id_sig_idSongMediaPlayer, song.getId_auxiliary());

            // update current song
            song = get_song_by_idmediaplayer(view, id_sig_idSongMediaPlayer, song.getId_auxiliary());

            // update media player
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(uri_song));
            mediaPlayer.start();

            update_view(view); // update_view
        }

    }

    private void init_control_previous(View view) {
        iView_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_song(view, -1);
            }
        });
    }

    private void init_control_netxt(final View view) {
        iView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_song(view, 1);
            }
        });
    }

    private void init_control_pause(View view) {
        iView_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                iView_play.setVisibility(View.VISIBLE);
                iView_pause.setVisibility(View.GONE);
            }
        });
    }

    private void init_control_play(final View view) {
        iView_play.setVisibility(View.VISIBLE);
        iView_pause.setVisibility(View.GONE);

        iView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                iView_play.setVisibility(View.GONE);
                iView_pause.setVisibility(View.VISIBLE);

                update_view(view);
            }
        });
    }

    private void init_song(View view) {

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(
                ReproductorFragment.SP_REPRODUCTOR_NAME, Context.MODE_PRIVATE
        );

        String song_id = sharedPreferences.getString("song_id", "");
        String playlist_id = sharedPreferences.getString("playlist_id", "");

        song = get_song_by_idmediaplayer(view, Integer.valueOf(song_id), Integer.valueOf(playlist_id));
    }

    private ListItem get_song_by_idmediaplayer(View view, Integer song_id, Integer playlist_id) {
        DBSong dbSong = new DBSong(view.getContext());
        ListItem aux_song = dbSong.get_song_by_id(song_id);
        aux_song.setId_auxiliary(Integer.valueOf(playlist_id));
        // missing assign the TYPE to

        return aux_song;
    }

    private void update_view(View view) {

        // update name && artist
        tView_name.setText(song.getTitle());
        tView_artist.setText(song.getSubtitle());

        // update seekBar
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        tview_time_end.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tview_time_start.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            finalTime = mediaPlayer.getDuration();

            tview_time_start.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);


            // show button play when the song is end
            if (startTime == finalTime) {
                iView_play.setVisibility(View.VISIBLE);
                iView_pause.setVisibility(View.GONE);
            }

            myHandler.postDelayed(this, 100);

        }
    };

}