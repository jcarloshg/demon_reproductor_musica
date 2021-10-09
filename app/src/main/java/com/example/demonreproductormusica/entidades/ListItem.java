package com.example.demonreproductormusica.entidades;

public class ListItem {

    public static final String ITEM_PLAYLIST_VIEW = "ITEM_PLAYLIST_VIEW";
    public static final String ITEM_PLAYLIST_LIST = "ITEM_PLAYLIST_VIEW";
    public static final String ITEM_SONG_VIEW = "ITEM_SONG_VIEW";
    public static final String ITEM_SONG_LIST = "ITEM_SONG_LIST";

    private int id;
    private String title;
    private String subtitle;
    private String TYPE;

    public ListItem(int _id, String _title, String _subtitle, String _TYPE) {
        this.id = _id;
        this.title = _title;
        this.subtitle = _subtitle;
        this.TYPE = _TYPE;
    }

    public ListItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(final String type) {
        this.TYPE = type;
    }
}
