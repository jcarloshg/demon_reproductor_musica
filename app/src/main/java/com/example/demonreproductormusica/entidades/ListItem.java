package com.example.demonreproductormusica.entidades;

public class ListItem {

    private int id;
    private String title;
    private String subtitle;

    public ListItem(int _id, String _title, String _subtitle){
        this.id = _id;
        this.title = _title;
        this.subtitle = _subtitle;
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
}
