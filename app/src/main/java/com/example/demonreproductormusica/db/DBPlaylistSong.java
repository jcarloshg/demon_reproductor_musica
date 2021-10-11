package com.example.demonreproductormusica.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

public class DBPlaylistSong extends DB {
    Context context;

    public DBPlaylistSong(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insert_song_to_playlist(int id_playlist, int id_song) {
        long id_insert = -1;
        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DB.PLAYLIST_SONG_COLUMN_ID_PLAYLIST, Integer.toString(id_playlist));
            contentValues.put(DB.PLAYLIST_SONG_COLUMN_ID_SONG, Integer.toString(id_song));

            id_insert = sqLiteDatabase.insert(DB.TABLE_PLAYLIST_SONG, null, contentValues);
        } catch (Exception ex) {
            Log.e("[insert_song_to_playlist]", ex.toString());
        }

        return id_insert;
    }

    public ArrayList<ListItem> get_playlist(int id_playlist) {

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ArrayList<ListItem> playlist = new ArrayList<>();

        Cursor cursor_TABLE_PLAYLIST_SONG = sqLiteDatabase.rawQuery(
                "SELECT *" +
                        " FROM " + DB.TABLE_PLAYLIST_SONG +
                        " WHERE " + DB.PLAYLIST_SONG_COLUMN_ID_PLAYLIST + " = " + Integer.toString(id_playlist),
                null);

        if (cursor_TABLE_PLAYLIST_SONG.moveToFirst()) {
            do {
                int id_song = cursor_TABLE_PLAYLIST_SONG.getInt(1);
                playlist.add(get_song_by_id(id_song));
            } while (cursor_TABLE_PLAYLIST_SONG.moveToNext());
        }

        for (ListItem item : playlist) {
            Log.i("[playlist]", "" + item.getId());
            Log.i("[playlist]", "" + item.getTitle());
            Log.i("[playlist]", "" + item.getSubtitle());
        }

        return playlist;
    }

    private ListItem get_song_by_id(int id_song) {

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
            Toast.makeText(null, "algo salio mal :(", Toast.LENGTH_SHORT).show();
        } else if (!cursor_song.moveToFirst()) {
            Toast.makeText(null, "no hay musica", Toast.LENGTH_SHORT).show();
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

    public void view_all_elements() {
        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT * FROM " + DB.TABLE_PLAYLIST_SONG,
                    null
            );

            if (cursor.moveToFirst()) {
                do {
                    Log.i(
                            "[view_all_elements]",
                            "id_playlist: " + cursor.getInt(0) +
                                    "is_song: " + cursor.getInt(1)
                    );

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("[view_all_elements]", ex.toString());
        }
    }

}
