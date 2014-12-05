package com.pdj.client;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pdj.client.model.Restaurant;
import com.pdj.client.model.ardoise.Ardoise;

/**
 * Created by fengqin on 14/11/13.
 */
public class PdjClientApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Restaurant.class);
        ParseObject.registerSubclass(Ardoise.class);
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "ZLxMvsT2sDIrF2U2hOGCH9hjTmHbDWWOBerBe4Wm", "FdwOP8l78j8ziIyRTOFcv9eWNUCuipCQyVLIoEER");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);


    }
}
