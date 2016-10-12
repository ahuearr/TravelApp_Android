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

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class Trip extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPMODEL;

	public Trip() {
		super(TABLENAME);
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
		return getDate(getDateIni());
	}

	public Date getDateFinDate() throws ParseException {
		return getDate(getDateFin());
	}

	public List<String> getDestinyName() {
		return getListString(Constants.DESTINYNAME);
	}

	public String getStatus() {
		return getString(Constants.STATUS);
	}

	public String getOrganizerId() {
		return getString(Constants.ORGANIZERID);
	}

	public List<TripAccommodation> getTripAccommodationList(){
		Object[] objectArray = getArray(Constants.TRIPACCOMMODATION);
		TripAccommodation[] array = Arrays.copyOf(objectArray, objectArray.length, TripAccommodation[].class);
		return Arrays.asList(array);
	}

	public static void findTripInBackground(String objectId, final ITObjectCallback<Trip> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new ITObjectCallback<Trip>(){
			@Override
			public void done(Trip trip, CloudException e) {
				if(trip != null){
					callback.done(trip, null);
				} else {
					callback.done(null,e);
				}
			}
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					callback.done(obj, null);
				} else {
					callback.done(null,e);
				}
			}
		});

	}

	public static void findTripListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<Trip> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<Trip>() {
			@Override
			public void done(Trip[] tripList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripList, null);
				} else {
					callback.done(null, e);
				}
			}

			@Override
			public void done(CloudObject[] obj, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(obj, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
