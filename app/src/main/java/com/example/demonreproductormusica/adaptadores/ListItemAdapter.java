package com.example.demonreproductormusica.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demonreproductormusica.R;
import com.example.demonreproductormusica.entidades.ListItem;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemHolder> {

    ArrayList<ListItem> lista_listItem;

    public ListItemAdapter(ArrayList<ListItem> listItem){
        this.lista_listItem = listItem;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_items, null, false
        );

        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        holder.textview_title.setText(lista_listItem.get(position).getTitle());
        holder.textview_subtitle.setText(lista_listItem.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return lista_listItem.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {

        TextView textview_title, textview_subtitle;

        public ListItemHolder(@NonNull View itemView) {
            super(itemView);

            textview_title = itemView.findViewById(R.id.textview_title);
            textview_subtitle = itemView.findViewById(R.id.textview_subtitle);
        }
    }
}
