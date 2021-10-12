package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

public class DBCurrentPlaylist extends DB {

    private final Context context;

    public DBCurrentPlaylist(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public void delete_from_currentplaylist() {
        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            sqLiteDatabase.delete(
                    "" + DB.CURRENT_PLAYLIST,
                    null,
                    null);
        } catch (Exception ex) {
            Log.e("[delete_from_currentplaylist]", ex.toString());
        }
    }

    public ArrayList<Integer> get_id_meadiaplayer_songs() {

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ArrayList<Integer> id_song_playlist = new ArrayList<>();

        try {
            Cursor cursor_TABLE_PLAYLIST_SONG = sqLiteDatabase.rawQuery(
                    "SELECT * FROM " + DB.CURRENT_PLAYLIST ,
                    null);

            if (cursor_TABLE_PLAYLIST_SONG.moveToFirst()) {
                do {
                    int id_song = cursor_TABLE_PLAYLIST_SONG.getInt(1);
                    id_song_playlist.add(id_song);

                    Log.i("[get_id_meadiaplayer_songs]", "get_id_meadiaplayer_songs: " + id_song);

                } while (cursor_TABLE_PLAYLIST_SONG.moveToNext());
            }

        } catch (Exception ex) {
            Log.e("[get_id_meadiaplayer_songs]", ex.toString());
        }

        return id_song_playlist;

    }

    public void insert_id_mediaplayer_song(int id_song) {
        long id_insert = -1;

        try {
            SQLiteDatabase sqLiteDatabase = new DB(this.context).getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DB.CURRENT_PLAYLIST_ID__SONG_MEDIAPLAYER, Integer.toString(id_song));

            id_insert = sqLiteDatabase.insert(DB.CURRENT_PLAYLIST, null, contentValues);


        } catch (Exception ex) {
            Log.e("[insert_id_song]", ex.toString());
        }
    }

}
