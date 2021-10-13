package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                DBSong dbSong = new DBSong(this.context);
                playlist.add(dbSong.get_song_by_id(id_song));
            } while (cursor_TABLE_PLAYLIST_SONG.moveToNext());
        }

        return playlist;
    }

    public ArrayList<Integer> get_id_songs_of_playlist(int id_playlist) {

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ArrayList<Integer> id_song_playlist = new ArrayList<>();

        try {
            Cursor cursor_TABLE_PLAYLIST_SONG = sqLiteDatabase.rawQuery(
                    "SELECT *" +
                            " FROM " + DB.TABLE_PLAYLIST_SONG +
                            " WHERE " + DB.PLAYLIST_SONG_COLUMN_ID_PLAYLIST + " = " + Integer.toString(id_playlist),
                    null);

            if (cursor_TABLE_PLAYLIST_SONG.moveToFirst()) {
                do {
                    int id_song = cursor_TABLE_PLAYLIST_SONG.getInt(1);
                    id_song_playlist.add(id_song);
                } while (cursor_TABLE_PLAYLIST_SONG.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("[get_uri_songs_of_playlist DBPlaylistSong] ", ex.toString());
        }

        return id_song_playlist;
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

    public boolean check_exist_song_on_playlist(int id_song, int id_playlist) {
        boolean exist_song = false;

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        try {
            Cursor cursor_TABLE_PLAYLIST_SONG = sqLiteDatabase.rawQuery(
                    "SELECT *" +
                            " FROM " + DB.TABLE_PLAYLIST_SONG +
                            " WHERE " + DB.PLAYLIST_SONG_COLUMN_ID_PLAYLIST + " = " + Integer.toString(id_playlist),
                    null);

            if (cursor_TABLE_PLAYLIST_SONG.moveToFirst()) {
                do {
                    int id_song_table = cursor_TABLE_PLAYLIST_SONG.getInt(1);
                    exist_song = (id_song_table == id_song) ? true : false;
                } while (cursor_TABLE_PLAYLIST_SONG.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("[check_exist_song_on_playlist] ", ex.toString());
        }

        return exist_song;
    }

    public boolean remove_row(int id){
        boolean is_remove = false;

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        try {
            String whereClause = DB.PLAYLIST_SONG_ID + " = ?";
            String[] whereArgs = new String[] { String.valueOf(id) };
            sqLiteDatabase.delete(DB.TABLE_PLAYLIST_SONG, whereClause, whereArgs);
            is_remove = true;
        } catch (Exception ex) {
            Log.e("[remove_row]", ex.toString());
        }
        return is_remove;
    }

    public boolean remove_song_from_playlist(int id_playlist_favorite, int id) {
        boolean remove = true;

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        try {

            Cursor cursor_TABLE_PLAYLIST_SONG = sqLiteDatabase.rawQuery(
                    "SELECT * " +
                            " FROM " + DB.TABLE_PLAYLIST_SONG + " " +
                            " WHERE " + DB.PLAYLIST_SONG_COLUMN_ID_PLAYLIST + " = " + Integer.toString(id_playlist_favorite) +
                            " AND " + DB.PLAYLIST_SONG_COLUMN_ID_SONG + " = " + Integer.toString(id),
                    null);

            if (cursor_TABLE_PLAYLIST_SONG.moveToFirst()){
                remove = remove_row(cursor_TABLE_PLAYLIST_SONG.getInt(2));
            }
        } catch (Exception ex) {
            Log.e("[remove_song_from_playlist]", ex.toString());
        }
        return remove;
    }
}
