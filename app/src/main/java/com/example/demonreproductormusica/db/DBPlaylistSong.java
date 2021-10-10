package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

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
