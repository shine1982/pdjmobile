package com.pdj.client.screen.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.model.RestaurantBO;
import com.pdj.client.util.FavRestoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FavorateRestosFragment extends ListFragment {



    private List<Map<String,String>> data;

    private Map<String, RestaurantBO> restoMap= new HashMap<String, RestaurantBO>();

    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavorateRestosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.fragment_favorate_restos, container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ParseQuery<Restaurant> query = Restaurant.getQuery();
        query.whereContainedIn("objectId", FavRestoManager.getInstance(getActivity()).getFavoriteRestoIds());
        data  = new ArrayList<Map<String,String>>();
        query.findInBackground(new FindCallback<Restaurant>() {
            public void done(List<Restaurant> restos, ParseException e) {
                if (e == null) {
                    Log.d("restos", "Retrieved " + restos.size() + " restaurants");
                    restoMap.clear();
                    for(final Restaurant resto:restos){
                        Map m = new HashMap();
                        m.put("name", resto.getName());
                        m.put("id", resto.getObjectId());
                        data.add(m);
                        restoMap.put(resto.getObjectId(),new RestaurantBO(resto));
                    }
                    setListAdapter(new RestoLineAdapter(
                            getActivity(),
                            data,
                            R.layout.one_resto_line,
                            new String[]{"name"},
                            new int[]{R.id.restoNameTextView}
                    ));
                } else {
                    Log.d("resto error", "Error: " + e.getMessage());
                }
            }
        });

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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(restoMap.get(data.get(position).get("id")));
        }
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
        public void onFragmentInteraction(RestaurantBO restoBo);
    }

}



