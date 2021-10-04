package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

public class DBPlaylist extends DB {

    Context context;

    public DBPlaylist(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insert_name_playlist(String name) {
        long id_playlist = -1;
        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);

            id_playlist = sqLiteDatabase.insert(this.TABLE_PLAYLIST, null, contentValues);
        } catch (Exception ex) {
            Log.e("[ERROR_DB]", ex.toString());
        }

        return id_playlist;
    }

    public ArrayList<ListItem> get_all_laylist(){
        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

    }

}
