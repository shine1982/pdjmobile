package com.pdj.client.screen.ardoise;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.model.ardoise.Ardoise;

/**
 * Created by fengqin on 14/12/4.
 */
public class ArdoiseNavFragment  extends Fragment {

    private Restaurant restaurant;
    private View view;
    private ListView listView;
    private ImageButton calendarBtn;
    private Ardoise ardoise;
    private ArdoiseManager ardoiseManager;
    public ArdoiseNavFragment(){}



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

        int position = getArguments().getInt("position");
        this.ardoise = ardoiseManager.getByPosition(position);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        deleteCalendarBtn();
        updateUI();
    }

    private void updateUI(){


        TextView dateTextView = (TextView) view.findViewById(R.id.ardoiseDate);
        dateTextView.setText(ardoise.getFormattedDate());

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

    private void deleteCalendarBtn(){
        calendarBtn = (ImageButton) view.findViewById(R.id.ardoiseCalendarBtn);
       if(calendarBtn!=null){
           calendarBtn.setVisibility(View.GONE);
       }

    }
    public interface ArdoiseManager{
        Ardoise getByPosition(int i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ardoiseManager = (ArdoiseManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ardoiseManager = null;
    }
}


