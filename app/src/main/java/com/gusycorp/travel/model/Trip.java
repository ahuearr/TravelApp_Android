package com.gusycorp.travel.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Trip")
public class Trip extends ParseObject {

	private static String TAG = Constants.TAG_TRIPMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public String getTripName() {
		return getString(Constants.TRIP_TRIPNAME);
	}

	public String getDateIni() {
		if(getDate(Constants.TRIP_DATEINI)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIP_DATEINI));
	}

	public String getDateFin() {

		if(getDate(Constants.TRIP_DATEFIN)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIP_DATEFIN));
	}

	public List<String> getDestinyName() {
		return getList(Constants.TRIP_DESTINYNAME);
	}

	public String getStatus() {
		return getString(Constants.TRIP_STATUS);
	}

	public String getOrganizerId() {
		return getString(Constants.TRIP_ORGANIZERID);
	}

	public static void findTripInBackground(String objectId,
			final GetCallback<Trip> callback) {
		ParseQuery<Trip> TripQuery = ParseQuery.getQuery(Trip.class);
		TripQuery.whereEqualTo(Constants.OBJECTID, objectId);
		TripQuery.getFirstInBackground(new GetCallback<Trip>() {
			@Override
			public void done(Trip trip, ParseException e) {
				if (e == null) {
					callback.done(trip, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	public static void findTripListByFieldsInBackground(
			Map<String, Object> filter, final FindCallback<Trip> callback) {
		ParseQuery<Trip> TripQuery = ParseQuery.getQuery(Trip.class);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			TripQuery.whereEqualTo((String) e.getKey(), e.getValue());
		}
		TripQuery.findInBackground(new FindCallback<Trip>() {
			@Override
			public void done(List<Trip> tripList, ParseException e) {
				if (e == null) {
					callback.done(tripList, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
