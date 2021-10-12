package com.example.demonreproductormusica.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

public class DBSong extends DB {
    Context context;

    public DBSong(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public ListItem get_song_by_id(int id_song) {

        ListItem song = null;

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        final String[] column_projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST
        };
        final String selection_column = MediaStore.Audio.Media._ID + " = ?";
        final String[] selection_agr = new String[]{Integer.toString(id_song)};

        Cursor cursor_song = contentResolver.query(uri, column_projection, selection_column, selection_agr, null);

        if (cursor_song == null) {
            Toast.makeText(context, "algo salio mal :(", Toast.LENGTH_SHORT).show();
        } else if (!cursor_song.moveToFirst()) {
            Toast.makeText(context, "no hay musica", Toast.LENGTH_SHORT).show();
        } else {
            do {
                int id = cursor_song.getInt(0);
                String name = cursor_song.getString(1);
                String album = cursor_song.getString(2);
                String artist = cursor_song.getString(3);

                ListItem listItem = new ListItem();
                listItem.setId(id);
                listItem.setTitle(name);
                listItem.setSubtitle(album + " | " + artist);
                song = listItem;

            } while (cursor_song.moveToNext());
        }

        return song;
    }

    public String get_uri(int id_song) {
        String filePath = null;

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        if (uri != null && "content".equals(uri.getScheme())) {

            final String[] column_projection = new String[]{MediaStore.Audio.Media.DATA};
            final String selection_column = MediaStore.Audio.Media._ID + " = ?";
            final String[] selection_agr = new String[]{Integer.toString(id_song)};

            Cursor cursor = contentResolver.query(uri, column_projection, selection_column, selection_agr, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }

        return filePath;
    }
}
