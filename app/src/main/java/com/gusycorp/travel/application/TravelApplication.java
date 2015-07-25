package com.gusycorp.travel.application;

import android.app.Application;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.gusycorp.travel.util.Utils;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelApplication extends Application {

	private static final String TAG = Constants.TAG_TRAVELAPPLICACION;

	public void onCreate() {
		ParseObject.registerSubclass(Trip.class);
		ParseObject.registerSubclass(TripTransport.class);
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, Utils.APPLICATION_ID, Utils.PARSE_KEY);
	}

	private List<Integer> menus = new ArrayList<Integer>(
			Arrays.asList(new Integer[]{R.id.transporte}));

	public List<Integer> getMenus() {
		return menus;
	}

	public void setMenus(final List<Integer> menus) {
		this.menus = menus;
	}

}
