package com.pdj.client.screen.search;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.pdj.client.R;
import com.pdj.client.model.RestaurantBO;
import com.pdj.client.screen.ardoise.ArdoiseActivity;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        FavorateRestosFragment.OnFragmentInteractionListener
{

    ViewPager viewPager=null;
    ActionBar actionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewPager();
        setActionBar();

    }
    private void setViewPager(){
        viewPager = (ViewPager)findViewById(R.id.mainpager);

        viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
            }
        });
    }

    private void setActionBar(){
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        final int[] tabTitles={R.drawable.ic_action_home, R.drawable.ic_action_favorite,R.drawable.ic_action_settings,R.drawable.ic_cook};
        for(int i=0;i <tabTitles.length; i++){
            ActionBar.Tab tab =actionBar.newTab();
            tab.setIcon(tabTitles[i]);
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case RestosFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */

                        break;
                }

        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onFragmentInteraction(RestaurantBO restaurantBO) {
        Intent intent = new Intent(this, ArdoiseActivity.class);
        intent.putExtra(RestosFragment.RESTO, restaurantBO);
        startActivity(intent);
    }
}
class MainPageAdapter extends FragmentPagerAdapter {


    public MainPageAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return new RestosFragment();
            case 1: return new FavorateRestosFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }




}