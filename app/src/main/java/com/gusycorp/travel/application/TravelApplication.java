package com.gusycorp.travel.application;

import android.app.Application;

import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;
import com.gusycorp.travel.util.Utils;
import com.parse.Parse;
import com.parse.ParseObject;

public class TravelApplication extends Application {

	private static final String TAG = Constants.TAG_TRAVELAPPLICACION;

	public void onCreate() {
		ParseObject.registerSubclass(Trip.class);
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, Utils.APPLICATION_ID, Utils.PARSE_KEY);
	}
}
