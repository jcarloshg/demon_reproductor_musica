package com.example.demonreproductormusica;

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

import com.example.demonreproductormusica.db.DBSong;
import com.example.demonreproductormusica.entidades.ListItem;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReproductorFragment} factory method to
 * create an instance of this fragment.
 */
public class ReproductorFragment extends Fragment {

    ReproductorFragmentArgs reproductorFragmentArgs;
    ListItem song;

    // VIEW
    TextView tView_name, tView_artist, tview_time_start, tview_time_end;
    ImageView iView_play_pause, iView_play_next, iView_play_previous, iView_pause;
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

        iView_play_pause = view.findViewById(R.id.iView_play_pause);
        iView_pause = view.findViewById(R.id.iView_pause);
        iView_play_next = view.findViewById(R.id.iView_play_next);
        iView_play_previous = view.findViewById(R.id.iView_play_previous);

        tview_time_start = view.findViewById(R.id.tview_time_start);
        tview_time_end = view.findViewById(R.id.tview_time_end);
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

        DBSong dbSong = new DBSong(view.getContext());
        String uri_song = dbSong.get_uri(song.getId());

        mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(uri_song));

        iView_play_pause.setVisibility(View.VISIBLE);
        iView_pause.setVisibility(View.GONE);

        iView_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                iView_play_pause.setVisibility(View.GONE);
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


        iView_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                iView_play_pause.setVisibility(View.VISIBLE);
                iView_pause.setVisibility(View.GONE);
            }

        });

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