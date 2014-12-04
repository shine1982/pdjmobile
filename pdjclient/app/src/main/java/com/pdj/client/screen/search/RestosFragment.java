package com.pdj.client.screen.search;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.screen.ardoise.ArdoiseActivity;
import com.pdj.client.util.LocationUtils;
import com.pdj.client.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RestosFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{


    public final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    private View viewFragement;
    private Location currentLocation;
    private Location lastLocation;
    // Fields for the map radius in feet
    private float radius;
    private float lastRadius;
    /*
 * Constants for handling location results
 */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;

    private ParseQueryAdapter<Restaurant> adapter;


    private ListView restoListView;
    private TextView emptyListTextView;
    private ImageButton geoBtn;
    private EditText searchTextView;
    private ImageButton searchBtn;

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity(), this, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFragement = inflater.inflate(R.layout.restaurants, container,false);
        return viewFragement;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocationClient.connect();


        geoBtn =(ImageButton) viewFragement.findViewById(R.id.geoButton);
        geoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(servicesConnected()){
                    currentLocation = mLocationClient.getLastLocation();
                    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                    if (myLoc != null) {
                        adapter.loadObjects();
                        lastLocation = currentLocation;
                    }
                }

            }
        });

        searchBtn = (ImageButton)viewFragement.findViewById(R.id.searchBtn);
        searchTextView = (EditText)viewFragement.findViewById(R.id.searchText);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchStr = searchTextView.getText().toString();
                if(StringUtils.isEmpty(searchStr)){
                    Toast.makeText(getActivity(), "Qu'est-ce que vous voulez chercher?", Toast.LENGTH_SHORT).show();
                }else{
                    ParseQueryAdapter.QueryFactory<Restaurant> factory =
                        new ParseQueryAdapter.QueryFactory<Restaurant>() {
                            public ParseQuery<Restaurant> create() {


                                ParseQuery<Restaurant> restaurantQuery = Restaurant.getQueryFromStr(searchStr);
                                /*
                                ParseGeoPoint geoPoint = getLocationFromAddress(getActivity(),searchStr);
                                if(geoPoint!=null){
                                    currentLocation = new Location("dummyprovider");
                                    currentLocation.setLatitude(geoPoint.getLatitude());
                                    currentLocation.setLongitude(geoPoint.getLongitude());
                                }*/
                                restaurantQuery.setLimit(MAX_POST_SEARCH_RESULTS);
                                return restaurantQuery;
                            }
                        };

                    restoListView.setAdapter(new RestaurantParseQueryAdapter(getActivity(),factory));


                }
            }
        });


        ParseQueryAdapter.QueryFactory<Restaurant> factory =
                new ParseQueryAdapter.QueryFactory<Restaurant>() {
                    public ParseQuery<Restaurant> create() {
                        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                        ParseQuery<Restaurant> query = Restaurant.getQuery();

                        query.whereNear("location", geoPointFromLocation(myLoc));

                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        return query;
                    }
                };

        adapter = new RestaurantParseQueryAdapter(getActivity(),factory);
        adapter.setAutoload(false);
        adapter.setPaginationEnabled(true);


        restoListView = (ListView) viewFragement.findViewById(R.id.restosList);
        restoListView.setAdapter(adapter);
        restoListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final Restaurant resto = adapter.getItem(i);
                        Intent intent = new Intent(getActivity(), ArdoiseActivity.class);
                        intent.putExtra("idResto", resto.getObjectId());
                        startActivity(intent);
                    }
                }
        );
    }
    class RestaurantParseQueryAdapter extends ParseQueryAdapter<Restaurant>{


        public RestaurantParseQueryAdapter(Context context, QueryFactory<Restaurant> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Restaurant restaurant, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.one_resto_line, null);
            }

            TextView restoName = (TextView) view.findViewById(R.id.restoNameTextView);

            restoName.setText(restaurant.getName());
            TextView distanceResto = (TextView) view.findViewById(R.id.distanceResto);
            Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
            if(myLoc!=null){
                distanceResto.setText(restaurant.getDistanceLabel(geoPointFromLocation(myLoc)));
            }
            return view;
        }
    }
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
       // super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //TODO
            //mListener.onFragmentInteraction(data.get(position).get("id"));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
       // Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        /*
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
 /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        getActivity(),
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                getActivity(),
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getActivity().getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    private ParseGeoPoint getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context, Locale.FRANCE);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null || address.size()==0) {
                return null;
            }
            final Address location = address.get(0);

            return new ParseGeoPoint(location.getLatitude() ,location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getActivity().getSupportFragmentManager(),
                        "Location Updates");

            }
        }
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
class RestoLineAdapter extends SimpleAdapter {

    private Context context;

    public RestoLineAdapter(Context context,
                            List<? extends Map<String, ?>> data,
                            int resource,
                            String[] from,
                            int[] to) {


        super(context, data, resource, from, to);
        this.context = context;
    }
}

class QueryRestaurantResult extends FindCallback<Restaurant>{

    @Override
    public void done(List<Restaurant> restaurants, ParseException e) {

    }
}

