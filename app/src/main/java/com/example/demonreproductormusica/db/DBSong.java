package com.example.demonreproductormusica.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBSong extends DB {
    Context context;

    public DBSong(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public ArrayList<ListItem> getAllFilesMp3() {

        ArrayList<ListItem> list = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            Toast.makeText(null, "algo salio mal :(", Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(null, "no hay musica", Toast.LENGTH_SHORT).show();
        } else {



            int column_id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int column_title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int column_album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int column_artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int column_album_id = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do {
                int id = cursor.getInt(column_id);
                String name = cursor.getString(column_title);
                String album = cursor.getString(column_album);
                String artist = cursor.getString(column_artist);
                Long album_id = cursor.getLong(column_album_id);
                Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                Log.e("[ID_ALMBUM]", "getAllFilesMp3: "+ album_id + " path " + img_path.toString() );

                ListItem listItem = new ListItem();
                listItem.setId(id);
                listItem.setTitle(name);
                listItem.setSubtitle(album + " - " + artist);
                listItem.setImg_id(album_id);
                listItem.setImg_path(img_path.toString());

                list.add(listItem);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ListItem get_song_by_id(int id_song) {

        ListItem song = null;

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        final String[] column_projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,

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
                Long album_id = cursor_song.getLong(4);
                Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                ListItem listItem = new ListItem();
                listItem.setId(id);
                listItem.setTitle(name);
                listItem.setSubtitle(album + " | " + artist);
                listItem.setImg_id(album_id);
                listItem.setImg_path(img_path.toString());
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


    public ArrayList<ListItem> get_song_by_name(String s) {

        ArrayList<ListItem> list = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] column_projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String selection_column = MediaStore.Audio.Media.TITLE + " LIKE ?";
            final String[] selection_agr = new String[]{"%"+s+"%"};

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
                    Long album_id = cursor_song.getLong(4);
                    Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                    Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                    ListItem listItem = new ListItem();
                    listItem.setId(id);
                    listItem.setTitle(name);
                    listItem.setSubtitle(album + " | " + artist);
                    listItem.setImg_id(album_id);
                    listItem.setImg_path(img_path.toString());
                    ListItem song = listItem;

                    list.add(song);

                } while (cursor_song.moveToNext());
            }
        }catch (Exception ex) {
            Log.e("[get_song_by_name]", ex.toString());
        }

        return list;
    }

    public ArrayList<ListItem> get_song_by_album() {

        ArrayList<ListItem> list = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] column_projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String selection_column = MediaStore.Audio.Media.ALBUM + " LIKE ?";
            final String[] selection_agr = new String[]{"%%"};

            Cursor cursor_song = contentResolver.query(
                    uri,
                    column_projection,
                    selection_column,
                    selection_agr,
                    "" +MediaStore.Audio.Media.ALBUM);

            if (cursor_song == null) {
                Toast.makeText(context, "algo salio mal :(", Toast.LENGTH_SHORT).show();
            } else if (!cursor_song.moveToFirst()) {
                Toast.makeText(context, "No existen albums con ese nombre :(", Toast.LENGTH_SHORT).show();
            } else {
                do {
                    int id = cursor_song.getInt(0);
                    String name = cursor_song.getString(1);
                    String album = cursor_song.getString(2);
                    String artist = cursor_song.getString(3);
                    Long album_id = cursor_song.getLong(4);
                    Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                    Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                    ListItem listItem = new ListItem();
                    listItem.setId(id);
                    listItem.setTitle(name);
                    listItem.setSubtitle(album + " | " + artist);
                    listItem.setImg_id(album_id);
                    listItem.setImg_path(img_path.toString());
                    ListItem song = listItem;

                    list.add(song);

                } while (cursor_song.moveToNext());
            }
        }catch (Exception ex) {
            Log.e("[get_song_by_name]", ex.toString());
        }

        return list;
    }

    public ArrayList<ListItem> get_song_by_albumName(String s) {

        ArrayList<ListItem> list = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] column_projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String selection_column = MediaStore.Audio.Media.ALBUM + " LIKE ?";
            final String[] selection_agr = new String[]{"%"+s+"%"};

            Cursor cursor_song = contentResolver.query(
                    uri,
                    column_projection,
                    selection_column,
                    selection_agr,
                    "" +MediaStore.Audio.Media.ALBUM);

            if (cursor_song == null) {
                Toast.makeText(context, "algo salio mal :(", Toast.LENGTH_SHORT).show();
            } else if (!cursor_song.moveToFirst()) {
                Toast.makeText(context, "No existen albums con ese nombre :(", Toast.LENGTH_SHORT).show();
            } else {
                do {
                    int id = cursor_song.getInt(0);
                    String name = cursor_song.getString(1);
                    String album = cursor_song.getString(2);
                    String artist = cursor_song.getString(3);
                    Long album_id = cursor_song.getLong(4);
                    Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                    Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                    ListItem listItem = new ListItem();
                    listItem.setId(id);
                    listItem.setTitle(name);
                    listItem.setSubtitle(album + " | " + artist);
                    listItem.setImg_id(album_id);
                    listItem.setImg_path(img_path.toString());
                    ListItem song = listItem;

                    list.add(song);

                } while (cursor_song.moveToNext());
            }
        }catch (Exception ex) {
            Log.e("[get_song_by_name]", ex.toString());
        }

        return list;
    }

    public ArrayList<ListItem> get_song_by_artist() {
        ArrayList<ListItem> list = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] column_projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String selection_column = MediaStore.Audio.Media.ARTIST + " LIKE ?";
            final String[] selection_agr = new String[]{"%%"};

            Cursor cursor_song = contentResolver.query(
                    uri,
                    column_projection,
                    selection_column,
                    selection_agr,
                    "" +MediaStore.Audio.Media.ARTIST);

            if (cursor_song == null) {
                Toast.makeText(context, "algo salio mal :(", Toast.LENGTH_SHORT).show();
            } else if (!cursor_song.moveToFirst()) {
                Toast.makeText(context, "No existen artistas con ese nombre :(", Toast.LENGTH_SHORT).show();
            } else {
                do {
                    int id = cursor_song.getInt(0);
                    String name = cursor_song.getString(1);
                    String album = cursor_song.getString(2);
                    String artist = cursor_song.getString(3);
                    Long album_id = cursor_song.getLong(4);
                    Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                    Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                    ListItem listItem = new ListItem();
                    listItem.setId(id);
                    listItem.setTitle(name);
                    listItem.setSubtitle(album + " | " + artist);
                    listItem.setImg_id(album_id);
                    listItem.setImg_path(img_path.toString());
                    ListItem song = listItem;

                    list.add(song);

                } while (cursor_song.moveToNext());
            }
        }catch (Exception ex) {
            Log.e("[get_song_by_artist]", ex.toString());
        }

        return list;

    }

    public ArrayList<ListItem> get_song_by_artistName(String s) {
        ArrayList<ListItem> list = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] column_projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String selection_column = MediaStore.Audio.Media.ARTIST + " LIKE ?";
            final String[] selection_agr = new String[]{"%"+s+"%"};

            Cursor cursor_song = contentResolver.query(
                    uri,
                    column_projection,
                    selection_column,
                    selection_agr,
                    "" +MediaStore.Audio.Media.ARTIST);

            if (cursor_song == null) {
                Toast.makeText(context, "algo salio mal :(", Toast.LENGTH_SHORT).show();
            } else if (!cursor_song.moveToFirst()) {
                Toast.makeText(context, "No existen artistas con ese nombre :(", Toast.LENGTH_SHORT).show();
            } else {
                do {
                    int id = cursor_song.getInt(0);
                    String name = cursor_song.getString(1);
                    String album = cursor_song.getString(2);
                    String artist = cursor_song.getString(3);
                    Long album_id = cursor_song.getLong(4);
                    Uri imgs_paths = Uri.parse("content://media/external/audio/albumart");
                    Uri img_path = ContentUris.withAppendedId(imgs_paths, album_id);

                    ListItem listItem = new ListItem();
                    listItem.setId(id);
                    listItem.setTitle(name);
                    listItem.setSubtitle(album + " | " + artist);
                    listItem.setImg_id(album_id);
                    listItem.setImg_path(img_path.toString());
                    ListItem song = listItem;

                    list.add(song);

                } while (cursor_song.moveToNext());
            }
        }catch (Exception ex) {
            Log.e("[get_song_by_name]", ex.toString());
        }

        return list;
    }
}
