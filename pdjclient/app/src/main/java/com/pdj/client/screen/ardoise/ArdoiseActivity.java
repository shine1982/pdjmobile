package com.pdj.client.screen.ardoise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.pdj.client.R;
import com.pdj.client.model.RestaurantBO;
import com.pdj.client.screen.search.RestosFragment;


public class ArdoiseActivity extends ActionBarActivity implements RestaurantBoHelper {

    private RestaurantBO restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ardoise);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        restaurant = (RestaurantBO) intent.getSerializableExtra(RestosFragment.RESTO);

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
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public RestaurantBO getResto() {
        return restaurant;
    }
}
