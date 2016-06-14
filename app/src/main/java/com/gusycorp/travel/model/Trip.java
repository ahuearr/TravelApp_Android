package com.gusycorp.travel.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gusycorp.travel.util.Constants;

import io.cloudboost.CloudObject;

public class Trip extends CloudObject {

	private static String TAG = Constants.TAG_TRIPMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public Trip() {
		super(TAG);
	}

	public String getTripName() {
		return getString(Constants.TRIPNAME);
	}

	public String getDateIni() {
		return getString(Constants.DATEINI);
	}

	public String getDateFin() {
		return getString(Constants.DATEFIN);
	}

	public Date getDateIniDate() throws ParseException {
		return df.parse(getDateIni());
	}

	public Date getDateFinDate() throws ParseException {
		return df.parse(getDateFin());
	}

	public List<String> getDestinyName() {
		return new ArrayList<String>(Arrays.<String>asList((String)getArray(Constants.DESTINYNAME)));
	}

	public String getStatus() {
		return getString(Constants.STATUS);
	}

	public String getOrganizerId() {
		return getString(Constants.ORGANIZERID);
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
