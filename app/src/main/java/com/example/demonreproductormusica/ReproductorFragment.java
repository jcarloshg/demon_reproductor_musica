package com.example.demonreproductormusica;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.demonreproductormusica.db.DBCurrentPlaylist;
import com.example.demonreproductormusica.db.DBSong;
import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReproductorFragment} factory method to
 * create an instance of this fragment.
 */
public class ReproductorFragment extends Fragment {

    // shared preferences
    public static final String SP_REPRODUCTOR_NAME = "SP_REPRODUCTOR_NAME";
    public static final String SP_REPRODUCTOR_ID_SONG = "SP_REPRODUCTOR_ID_SONG";
    public static final String SP_REPRODUCTOR_ID_PLAYLIST = "SP_REPRODUCTOR_ID_PLAYLIST";

    // ReproductorFragmentArgs reproductorFragmentArgs;
    ListItem song;

    // VIEW
    TextView tView_name, tView_artist, tview_time_start, tview_time_end;
    ImageView iView_play, iView_next, iView_previous, iView_pause;
    private SeekBar seekBar;
    MediaPlayer mediaPlayer;

    private Handler myHandler = new Handler();

    boolean is_play_song = false;

    public static int oneTimeOnly = 0;
    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private TextView tx1, tx2, tx3;

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

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setClickable(false);

        iView_play = view.findViewById(R.id.iView_play);
        iView_pause = view.findViewById(R.id.iView_pause);
        iView_next = view.findViewById(R.id.iView_next);
        iView_previous = view.findViewById(R.id.iView_play_previous);

        tview_time_start = view.findViewById(R.id.tview_time_start);
        tview_time_end = view.findViewById(R.id.tview_time_end);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reproductor, container, false);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(
                ReproductorFragment.SP_REPRODUCTOR_NAME, Context.MODE_PRIVATE
        );


        DBSong dbSong = new DBSong(view.getContext());

        init_song(sharedPreferences, view);
        init_components(view);
        init_view(view);

        DBCurrentPlaylist DBCurrentPlaylist = new DBCurrentPlaylist(view.getContext());
        ArrayList<Integer> id_meadiaplayer_songs = dbCurrentPlaylist.get_id_meadiaplayer_songs();
        // =====================================================================================


        ArrayList<Uri> arrayList_uris = new ArrayList<>();

        for (Integer id_song: id_meadiaplayer_songs) {
            String uri_song = dbSong.get_uri(id_song); // get path each song
            arrayList_uris.add(Uri.parse(uri_song)); // get uri each song && put thi in array_list
        }

        int id_current_song = DBCurrentPlaylist.get_IDSONG_on_CURRENTPLAYLIST_by_IDSONGMEDIAPLAYER(song.getId());
        

        // =====================================================================================


        String uri_song = dbSong.get_uri(song.getId());

        mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(uri_song));

        init_control_play(view);
        init_control_pause(view);
        init_control_netxt(view);



        return view;
    }

    private void init_control_netxt(View view) {
        iView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private void init_control_play(View view) {
        iView_play.setVisibility(View.VISIBLE);
        iView_pause.setVisibility(View.GONE);

        iView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                iView_play.setVisibility(View.GONE);
                iView_pause.setVisibility(View.VISIBLE);

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
        });
    }

    private void init_song(SharedPreferences sharedPreferences,  View view) {

        String song_id = sharedPreferences.getString("song_id", "");
        String playlist_id = sharedPreferences.getString("playlist_id", "");

        DBSong dbSong = new DBSong(view.getContext());
        song = dbSong.get_song_by_id(Integer.valueOf(song_id));
        song.setId_auxiliary(Integer.valueOf(playlist_id));
        // missing assign the TYPE to
    }

    private void init_view(View view) {
        tView_name.setText(song.getTitle());
        tView_artist.setText(song.getSubtitle());
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tview_time_start.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}