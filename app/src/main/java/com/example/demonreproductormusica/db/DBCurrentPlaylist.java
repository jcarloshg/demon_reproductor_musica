package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

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
                    "SELECT * FROM " + DB.CURRENT_PLAYLIST,
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

    public int get_IDSONG_on_CURRENTPLAYLIST_by_IDSONGMEDIAPLAYER(Integer ID_SONG_MEDIAPLAYER) {
        int id_song = -1;

        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT *" +
                            " FROM " + DB.CURRENT_PLAYLIST +
                            " WHERE " + DB.CURRENT_PLAYLIST_ID__SONG_MEDIAPLAYER + " = " + ID_SONG_MEDIAPLAYER,
                    null);


            if (cursor.moveToFirst()) {
                id_song = cursor.getInt(0); // get id of song on table
            }

        } catch (Exception ex) {
            Log.e("[get_CURRENT_PLAYLIST_ID_SONG]", ex.toString());
        }

        return id_song;
    }

    public int get_IDSONGMEDIAPLAYER_by_IDSONG(int ID_SONG) {
        int ID_SONG_MEDIA_PLAYER = -1;

        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT *" +
                            " FROM " + DB.CURRENT_PLAYLIST +
                            " WHERE " + DB.CURRENT_PLAYLIST_ID_SONG + " = " + ID_SONG,
                    null);

            if (cursor.moveToFirst()) {
                ID_SONG_MEDIA_PLAYER = cursor.getInt(1); // get id of song on table // get id of song on table
            }
        } catch (Exception ex) {
            Log.e("[get_IDSONGMEDIAPLAYER_by_IDSONG]", ex.toString());
        }

        return ID_SONG_MEDIA_PLAYER;
    }
}
