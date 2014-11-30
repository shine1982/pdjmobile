package com.pdj.client.screen.ardoise;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.util.FavRestoManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRestoFragment extends Fragment {

    private static final String ANONYMOUS_USER="anonymous";
    private static final String FAVORITE_RESTOS="favoriteRestos";

    private Restaurant restaurant;
    private View view;
    public ShowRestoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_show_resto, container, false);
       return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String idResto = getActivity().getIntent().getExtras().getString("idResto");

        //trouvé le resto:
        ParseQuery<Restaurant> query = Restaurant.getQuery();

        query.getInBackground(idResto, new GetCallback<Restaurant>() {
            public void done(Restaurant resto, ParseException e) {
                if (e == null) {
                    restaurant = resto;
                    updateUI();

                } else {
                    // something went wrong
                    Log.d("resto","non réussi de récpuerer le resto avec l'id"+idResto);
                }
            }
        });

    }

    public void updateUI(){
        TextView restoNameTV = (TextView) view.findViewById(R.id.restoNameLabel);
        TextView restoAddressTV = (TextView) view.findViewById(R.id.restoAddress);
        restoNameTV.setText(restaurant.getName());
        restoAddressTV.setText(restaurant.getAddress() +", "+restaurant.getPostalCode()+", "+restaurant.getCity());
        final FavRestoManager favRestoManager =  FavRestoManager.getInstance(getActivity());




        final Button favoriteRestoButton = (Button) view.findViewById(R.id.favoriteRestoButton);
        boolean ifFav = favRestoManager.isRestoFav(restaurant.getObjectId());
        favoriteRestoButton.setText((ifFav?"-":"+")+"Favori");

        favoriteRestoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String idResto = restaurant.getObjectId();
                boolean isFavNow = false;
                if(favRestoManager.isRestoFav(idResto)){
                    favRestoManager.removeFavResto(idResto);
                }else{
                    favRestoManager.addFavResto(idResto);
                    isFavNow = true;
                }

                favoriteRestoButton.setText((isFavNow?"-":"+")+"Favori");
            }

        });
    }
}
