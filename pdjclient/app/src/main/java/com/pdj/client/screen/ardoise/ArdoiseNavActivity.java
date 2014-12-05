/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pdj.client.screen.ardoise;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pdj.client.R;
import com.pdj.client.model.Restaurant;
import com.pdj.client.model.RestaurantBO;
import com.pdj.client.model.ardoise.Ardoise;
import com.pdj.client.screen.search.RestosFragment;

import java.util.List;

public class ArdoiseNavActivity extends FragmentActivity implements ArdoiseNavFragment.ArdoiseManager,
RestaurantBoHelper{

    ArdoiseNavPagerAdapter mArdoiseNavPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    private RestaurantBO restaurant;
    private List<Ardoise> ardoises;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ardoise_nav);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);

        if(restaurant==null){
            restaurant = (RestaurantBO)this.getIntent().getSerializableExtra(RestosFragment.RESTO);
        }
        actionBar.setTitle(restaurant.getName());
        final Restaurant resto = Restaurant.newRestaurantWithoutData(restaurant.getId());
        ParseQuery<Ardoise> query = Ardoise.getFullQuery(resto, null,20);
        query.findInBackground(new FindCallback<Ardoise>() {
            @Override
            public void done(List<Ardoise> ardoises, ParseException e) {
                if(e==null){

                    setArdoises(ardoises);

                    if(ardoises.size()==0){
                        msgZeroArdoise();
                    }else{
                        mArdoiseNavPagerAdapter = new ArdoiseNavPagerAdapter(getSupportFragmentManager(),ardoises);
                        mViewPager.setAdapter(mArdoiseNavPagerAdapter);
                    }

                }else{
                    showErrorTechnique(e);
                }

            }
        });


    }

    private void msgZeroArdoise() {
        Toast.makeText(this, "Il n'existe pas d'ardoise définie pour ce restaurant :-(", Toast.LENGTH_LONG).show();
    }

    private void showErrorTechnique( ParseException e) {
        Toast.makeText(this, "Erreur technique ["+e.toString()+"]", Toast.LENGTH_LONG).show();
    }


    private void setArdoises(List<Ardoise> ardoises){
        this.ardoises=ardoises;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ardoise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent =NavUtils.getParentActivityIntent(this);
                intent.putExtra(RestosFragment.RESTO,this.restaurant);
                NavUtils.navigateUpTo(this,intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Ardoise getByPosition(int i) {
        return this.ardoises.get(i);
    }

    @Override
    public RestaurantBO getResto() {
        return restaurant;
    }


    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class ArdoiseNavPagerAdapter extends FragmentStatePagerAdapter {
        private List<Ardoise> ardoises;
        public ArdoiseNavPagerAdapter(FragmentManager fm, List<Ardoise> ardoises) {
            super(fm);
            this.ardoises=ardoises;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ArdoiseNavFragment();
            Bundle args = new Bundle();
            args.putInt("position", i); // Our object is just an integer :-P
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return this.ardoises.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.ardoises.get(position).getFormattedShortDate();
        }


    }


}
