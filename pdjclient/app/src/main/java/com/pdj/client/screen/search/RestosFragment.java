package com.pdj.client.screen.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RestosFragment extends ListFragment {

    private ListView restoListView;
    private TextView emptyListTextView;
    private Button geoBtn;
    private TextView searchTextView;
    private Button searchBtn;


    private List<Map<String,String>> data;


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


        ParseQuery<Restaurant> query = Restaurant.getQuery();
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1)); //cache qui dure 1 hour
        data  = new ArrayList<Map<String,String>>();
        query.findInBackground(new FindCallback<Restaurant>() {
            public void done(List<Restaurant> restoList, ParseException e) {
                if (e == null) {
                    Log.d("restos", "Retrieved " + restoList.size() + " restaurants");
                    for(final Restaurant resto:restoList){
                        Map m = new HashMap();
                        m.put("name", resto.getName());
                        m.put("id", resto.getObjectId());
                        data.add(m);
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




        //setListAdapter(new RestoLineAdapter(getActivity(),R.layout.one_resto_line, restos));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =
        inflater.inflate(R.layout.restaurants, container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            mListener.onFragmentInteraction(data.get(position).get("id"));
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
        public void onFragmentInteraction(String id);
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
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.one_resto_line, parent, false);

        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(this.restos.get(position).getName());

        return v;
    }*/
}