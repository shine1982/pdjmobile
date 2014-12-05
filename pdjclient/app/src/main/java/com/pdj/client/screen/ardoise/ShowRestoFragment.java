package com.pdj.client.screen.ardoise;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pdj.client.R;
import com.pdj.client.model.RestaurantBO;
import com.pdj.client.util.FavRestoManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRestoFragment extends Fragment {

    private RestaurantBO restaurant;

    private ImageButton favoriteRestoButton;
    private ImageButton callButton;
    private ImageButton openMapButton;

    private RestaurantBoHelper restaurantBoHelper;

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
        restaurant = restaurantBoHelper.getResto();
        updateUI();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            restaurantBoHelper = (RestaurantBoHelper) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        restaurantBoHelper = null;
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
        boolean ifFav = favRestoManager.isRestoFav(restaurant.getId());
        favoriteRestoButton.setImageResource(ifFav?R.drawable.ic_like_50:R.drawable.ic_like_outline_50);

        favoriteRestoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String idResto = restaurant.getId();
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
