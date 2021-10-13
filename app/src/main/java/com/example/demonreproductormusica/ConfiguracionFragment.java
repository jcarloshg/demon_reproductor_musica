package com.example.demonreproductormusica;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfiguracionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfiguracionFragment extends Fragment {

    LinearLayout link_instagram;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init_components(View view) {
        link_instagram = view.findViewById(R.id.link_instagram);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        init_components(view);

        init_link_instagram_onClick(view);

        return view;
    }

    private void init_link_instagram_onClick(View view) {
        link_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url_instagram = "https://www.instagram.com/hg.jcarlos/";
                    // String url_instagram = "https://translate.google.com/";
                    Uri uri_instagram = Uri.parse(url_instagram);
                    Intent intent_instagram = new Intent(Intent.ACTION_VIEW, uri_instagram);

                    // intent_instagram.setPackage("com.chrome.android");
                    startActivity(intent_instagram);
                }catch (ActivityNotFoundException e) {
                    Log.e("[init_link_instagram_onClick]", e.getMessage());
                }
            }
        });
    }
}