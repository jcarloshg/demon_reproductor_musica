package com.example.demonreproductormusica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demonreproductormusica.entidades.*;

import androidx.annotation.Nullable;

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
            contentValues.put(DB.PLAYLIST_COLUMN_NAME, name);

            id_playlist = sqLiteDatabase.insert(DB.TABLE_PLAYLIST, null, contentValues);
        } catch (Exception ex) {
            Log.e("[ERROR_DB]", ex.toString());
        }

        return id_playlist;
    }

    public ArrayList<ListItem> get_playlist_for_name(String name_list) {
        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        ArrayList<ListItem> list = new ArrayList<>();

        String[] parameters = {name_list};
        String[] camps = {DB.PLAYLIST_COLUMN_ID, DB.PLAYLIST_COLUMN_NAME};

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * " +
                        "FROM " + DB.TABLE_PLAYLIST +
                        " WHERE " + DB.PLAYLIST_COLUMN_NAME + " LIKE '%" + name_list + "%';"
                , null);

        if (cursor.moveToFirst()) {
            do {
                ListItem listItem = new ListItem();
                listItem.setId(cursor.getInt(0));
                listItem.setTitle(cursor.getString(1));
                listItem.setSubtitle(null);
                list.add(listItem);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<ListItem> get_all_laylist() {
        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        ArrayList<ListItem> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB.TABLE_PLAYLIST, null);

        if (cursor.moveToFirst()) {
            do {
                ListItem listItem = new ListItem();
                listItem.setId(cursor.getInt(0));
                listItem.setTitle(cursor.getString(1));
                listItem.setSubtitle(null);
                list.add(listItem);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ListItem get_playlist_info(String id) {
        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ListItem playlist = new ListItem();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT *" +
                        " FROM " + DB.TABLE_PLAYLIST +
                        " WHERE " + DB.PLAYLIST_COLUMN_ID + " = " + id,
                null);

        if (cursor.moveToFirst()) {
            do {
                playlist.setId(cursor.getInt(0));
                playlist.setTitle(cursor.getString(1));
                playlist.setSubtitle(null);
            } while (cursor.moveToNext());
        }

        return playlist;
    }

    public int get_id_playlist_by_name(String name) {
        int id_playlist = -1;
        try {
            DB db = new DB(this.context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT * " +
                            " FROM " + DB.TABLE_PLAYLIST +
                            " WHERE " + DB.PLAYLIST_COLUMN_NAME + "= '" + name + "'",
                    null);

            if (cursor.moveToFirst()) {
                id_playlist = cursor.getInt(0);
            }
        } catch (Exception ex) {
            Log.e("[get_id_playlist_by_name]", ex.toString());
        }

        return id_playlist;
    }

    public boolean delete_playlist(int id_playlist){
        boolean is_remove = false;

        DB db = new DB(this.context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        try {
            String whereClause = DB.PLAYLIST_COLUMN_ID + " = ?";
            String[] whereArgs = new String[] { String.valueOf(id_playlist) };
            sqLiteDatabase.delete(DB.TABLE_PLAYLIST, whereClause, whereArgs);
            is_remove = true;
        } catch (Exception ex) {
            Log.e("[remove_row]", ex.toString());
        }
        return is_remove;
    }
}
