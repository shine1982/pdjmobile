package com.pdj.client.screen.ardoise;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
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

    private ImageButton favoriteRestoButton;
    private ImageButton callButton;
    private ImageButton openMapButton;

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

        setFavoriteButton();
        setCallButton();
        setOpenMapButton();
    }
    private void setFavoriteButton(){
        final FavRestoManager favRestoManager =  FavRestoManager.getInstance(getActivity());
        favoriteRestoButton = (ImageButton) view.findViewById(R.id.favoriteRestoButton);
        boolean ifFav = favRestoManager.isRestoFav(restaurant.getObjectId());
        favoriteRestoButton.setImageResource(ifFav?R.drawable.ic_like_50:R.drawable.ic_like_outline_50);

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

                favoriteRestoButton.setImageResource(isFavNow?R.drawable.ic_like_50:R.drawable.ic_like_outline_50);

            }

        });
    }

    private void setCallButton(){
        callButton = (ImageButton) view.findViewById(R.id.callResto);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+restaurant.getTelephone()));
                startActivity(callIntent);
            }
        });
    }

    private void setOpenMapButton(){
        openMapButton =(ImageButton) view.findViewById(R.id.mapToResto);
        openMapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String encodedString = Uri.encode(restaurant.getCompleteAddress());
                Uri geoLocation =Uri.parse("geo:0,0?q="+encodedString);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);
                startActivity(intent);
            }
        });

    }
}
