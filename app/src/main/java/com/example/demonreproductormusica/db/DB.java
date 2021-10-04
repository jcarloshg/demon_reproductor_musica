package com.example.demonreproductormusica.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "demon";
    protected static final String TABLE_PLAYLIST = "table_playlist";
    protected static final String TABLE_SONGS = "table_song";
    protected static final String TABLE_PLAYLIST_SONG = "table_playlist_song";

    // check exist dataBase
    private static String DATABASE_PATH;

    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DATABASE_PATH = "/data/data/" + context.getOpPackageName() + "/databases/";
        Log.e("[DATA_BASE]", "path: " + DATABASE_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (checkExistDataBase()) {

        } else {
            db.execSQL(
                    "CREATE TABLE " + TABLE_PLAYLIST + " ( " +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL)"
            );

            db.execSQL(
                    "CREATE TABLE " + TABLE_SONGS + " ( " +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL," +
                            "artist TEXT," +
                            "album TEXT," +
                            "ubicaion NOT NULL)"
            );

            db.execSQL(
                    "CREATE TABLE " + TABLE_PLAYLIST_SONG + " ( " +
                            "id_playlist INTEGER NOT NULL," +
                            "id_song INTEGER NOT NULL)"
            );
        }
    }

    private boolean checkExistDataBase() {
        SQLiteDatabase flag_exits = null;
        try {
            String path_data_base = DATABASE_PATH + DATABASE_NAME;
            flag_exits = SQLiteDatabase.openDatabase(path_data_base, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            flag_exits.close();
        }

        return flag_exits != null ? true : false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_PLAYLIST_SONG);
        db.execSQL("DROP TABLE " + TABLE_SONGS);
        db.execSQL("DROP TABLE " + TABLE_PLAYLIST);

        onCreate(db);

    }
}
