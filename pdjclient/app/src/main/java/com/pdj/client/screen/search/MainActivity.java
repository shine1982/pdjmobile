package com.pdj.client.screen.search;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.pdj.client.screen.ardoise.ArdoiseActivity;
import com.pdj.client.R;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        RestosFragment.OnFragmentInteractionListener
{

    ViewPager viewPager=null;
    ActionBar actionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.mainpager);

        viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
            }
        });

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        final String[] tabTitles={"Les restaurants", "Mes favoris"};
        for(int i=0;i <tabTitles.length; i++){
            ActionBar.Tab tab =actionBar.newTab();
            tab.setText(tabTitles[i]);
            tab.setTabListener(this);
            actionBar.addTab(tab);
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
    public void onFragmentInteraction(String id) {
        final String idResto = id;
        Intent intent = new Intent(this, ArdoiseActivity.class);
        intent.putExtra("idResto", idResto);
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
            case 1: return new RestosFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }




}