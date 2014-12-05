package com.pdj.client.screen.restaurateur;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pdj.client.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurateurFragment extends Fragment {

    private View fragmentView;

    public RestaurateurFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView= inflater.inflate(R.layout.fragment_restaurateur, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button restaurateurBtn = (Button) fragmentView.findViewById(R.id.goToRestaurateur);
        restaurateurBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://pdjprod.parseapp.com/r/inscrire");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
        );

    }
}
