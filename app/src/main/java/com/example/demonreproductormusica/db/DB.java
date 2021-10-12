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
    private static final String DATABASE_NAME = "demon.db";
    public static final String TABLE_PLAYLIST = "table_playlist";
    public static final String CURRENT_PLAYLIST = "CURRENT_PLAYLIST";
    public static final String TABLE_SONGS = "table_song";
    public static final String TABLE_PLAYLIST_SONG = "table_playlist_song";

    // COLUMN NAME <TABLE_PLAYLIST>
    public static final String PLAYLIST_COLUMN_ID = "id";
    public static final String PLAYLIST_COLUMN_NAME = "name";


    // COLUMN NAME <CURRENT_PLAYLIST>
    public static final String CURRENT_PLAYLIST_ID_SONG = "CURRENT_PLAYLIST_ID_SONG";
    public static final String CURRENT_PLAYLIST_ID__SONG_MEDIAPLAYER = "CURRENT_PLAYLIST_ID__SONG_MEDIAPLAYER";

    // COLUMN NAME <TABLE_SONGS>
    public static final String SONGS_COLUMN_ID = "id";
    public static final String SONGS_COLUMN_NAME = "name";
    public static final String SONGS_COLUMN_ARTIST = "artist";
    public static final String SONGS_COLUMN_ALBUM = "album";
    public static final String SONGS_COLUMN_URI = "uri";

    // COLUMN NAME <TABLE_PLAYLIST_SONG>
    public static final String PLAYLIST_SONG_COLUMN_ID_PLAYLIST = "id_playlist";
    public static final String PLAYLIST_SONG_COLUMN_ID_SONG = "id_song";

    // check exist dataBase
    private static String DATABASE_PATH;

    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.d("[DATA_BASE]", "path: " + DATABASE_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_PLAYLIST +
                        " (" +
                        PLAYLIST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PLAYLIST_COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + CURRENT_PLAYLIST +
                        " (" +
                        CURRENT_PLAYLIST_ID_SONG + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CURRENT_PLAYLIST_ID__SONG_MEDIAPLAYER + " INTEGER NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SONGS +
                        "(" +
                        SONGS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SONGS_COLUMN_NAME + " TEXT NOT NULL, " +
                        SONGS_COLUMN_ARTIST + " TEXT NOT NULL, " +
                        SONGS_COLUMN_ALBUM + " TEXT NOT NULL, " +
                        SONGS_COLUMN_URI + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_PLAYLIST_SONG +
                        "(" +
                        PLAYLIST_SONG_COLUMN_ID_PLAYLIST + " INTEGER NOT NULL," +
                        PLAYLIST_SONG_COLUMN_ID_SONG + " INTEGER NOT NULL" +
                        ")"
        );
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
