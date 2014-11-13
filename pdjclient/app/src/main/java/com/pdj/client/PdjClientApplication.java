package com.pdj.client;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by fengqin on 14/11/13.
 */
public class PdjClientApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "09qsWVn2x6ItvvCuxaDIOmGDoc7aPbAVGLAPOCbZ", "Dv27lV6YAYMt9VlbHcp8IG3mvh2uKyCtBmezHbpq");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
