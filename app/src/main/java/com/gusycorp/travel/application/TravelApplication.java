package com.gusycorp.travel.application;

import android.app.Application;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;
import com.gusycorp.travel.util.Utils;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TravelApplication extends Application {

	private static final String TAG = Constants.TAG_TRAVELAPPLICACION;
	private List<Integer> menus = new ArrayList<Integer>();
	private List<TypeTransport> transportstype = new ArrayList<TypeTransport>();

	public void onCreate() {
		ParseObject.registerSubclass(Trip.class);
		ParseObject.registerSubclass(TripTransport.class);
		ParseObject.registerSubclass(TypeTransport.class);
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, Utils.APPLICATION_ID, Utils.PARSE_KEY);

		getListsSpinners();
	}

	public List<Integer> getMenus() {
		return menus;
	}

	public List<TypeTransport> getTransportTypes() {
		return transportstype;
	}

	public void setMenus(final List<Integer> menus) {
		this.menus = menus;
	}

	void getListsSpinners(){
		HashMap<String, Object> filter = new HashMap();
		TypeTransport.findTripListByFieldsInBackground(filter,new FindCallback<TypeTransport>() {

			@Override
			public void done(List<TypeTransport> list, ParseException e) {
				transportstype.addAll(list);
			}
		});
	}

}
