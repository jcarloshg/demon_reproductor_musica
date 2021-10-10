package com.example.demonreproductormusica.entidades;

public class ListItem {

    public static final String ITEM_PLAYLIST_VIEW = "ITEM_PLAYLIST_VIEW";
    public static final String ITEM_PLAYLIST_LIST = "ITEM_PLAYLIST_LIST";
    public static final String ITEM_SONG_VIEW = "ITEM_SONG_VIEW";
    public static final String ITEM_SONG_LIST = "ITEM_SONG_LIST";

    private int id;
    private String title;
    private String subtitle;

    /*
    - el id_auxiliary sirve para cuando queremos hacer una accion que requiera dos id.
    Ejemplo, insertar una canción a una playlist. Abrimos menu contextual && selecionamos agregar a playlist
    Desde la vista del elmento del cancion decimos que queremos ver las playlist a las que posiblemente
    agreguemos la cancion. La vista nos arroja las playlist con sus propios id. De alguna forma debemos
    pasar el id de la canción para agregar esta a una playlist.
    En este caso
        id_auxiliary -> id cancion
        id  -> id playlist
     */
    private int id_auxiliary;

    // its to activate actions on view fragments/bottomSheet
    // ej. if this item is show on bottomSheet_playlist, action -> add song to playlist
    // ej. if this item is show on Bibloteca(show playlist), action -> go to reproductor
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

    public void setId_auxiliary(int id_auxiliary) {
        this.id_auxiliary = id_auxiliary;
    }

    public int getId_auxiliary() {
        return id_auxiliary;
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
