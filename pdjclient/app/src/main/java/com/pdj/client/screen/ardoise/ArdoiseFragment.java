package com.pdj.client.screen.ardoise;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.model.ardoise.Ardoise;
import com.pdj.client.model.ardoise.ArdoiseItem;
import com.pdj.client.util.StringUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArdoiseFragment extends Fragment {

    private Restaurant restaurant;
    private View view;
    private Ardoise ardoise;
    private ListView listView;

    public ArdoiseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ardoise, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(restaurant==null){
            final String idResto = getActivity().getIntent().getExtras().getString("idResto");
            restaurant = Restaurant.newRestaurantWithoutData(idResto);
        }
        ParseQuery<Ardoise> query = Ardoise.getFullQuery(restaurant, null);
        query.findInBackground(new FindCallback<Ardoise>() {
            public void done(List<Ardoise> ardoises, ParseException e) {
                if (e == null) {
                    Log.d("ardoises", "Retrieved " + ardoises.size() + " ardoises");
                    if(ardoises.size()>0) {
                        ardoise = ardoises.get(0);
                        updateUI();
                    }
                } else {
                    Log.d("ardoise", "Error: " + e.getMessage());
                }
            }
        });

    }
    private void updateUI(){

            TextView titleTextView = (TextView) view.findViewById(R.id.ardoiseTitle);
            SpannableString ardoiseTitle = new SpannableString(ardoise.getTitle());
            ardoiseTitle.setSpan(new UnderlineSpan(), 0, ardoiseTitle.length(), 0);
            titleTextView.setText(ardoiseTitle);

            ArdoiseItemAdapter ardoiseItemAdapter=new ArdoiseItemAdapter(
                    getActivity(),
                    ardoise.getAllDatas());

            listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(ardoiseItemAdapter);

    }
}

class ArdoiseItemAdapter extends ArrayAdapter<ArdoiseItem> {

    private Context context;
    private List<ArdoiseItem> ardoiseItems;

    public ArdoiseItemAdapter(Context context, List<ArdoiseItem> ardoiseItems) {

        super(context, R.layout.oneline_ardoise_item, ardoiseItems);
        this.context = context;
        this.ardoiseItems = ardoiseItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
            View v = convertView;
            if(v==null){
                v = inflater.inflate(R.layout.oneline_ardoise_item, parent, false);
            }

            ArdoiseItem ardoiseItem = this.ardoiseItems.get(position);
            TextView t1 = (TextView) v.findViewById(R.id.ardoiseItem1);

            if(ardoiseItem.isShouldUnderline()){
                SpannableString ardoiseDishesBlocTitle= new SpannableString(ardoiseItem.getLabel());
                ardoiseDishesBlocTitle.setSpan(new UnderlineSpan(), 0, ardoiseItem.getLabel().length(), 0);
                t1.setText(ardoiseDishesBlocTitle);
            }else{
                t1.setText(ardoiseItem.getLabel());
            }

            TextView t2 = (TextView) v.findViewById(R.id.ardoiseItem2);
            if(!StringUtils.isEmpty(ardoiseItem.getPrice())){
                t2.setText(ardoiseItem.getPrice()+" €");
            }else {
                t2.setText("");
            }
            return v;
    }
}
