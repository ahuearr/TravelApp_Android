package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudQuery;

public class TripAccommodation extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPACCOMMODATIONMODEL;

	public TripAccommodation() {
		super(TABLENAME);
	}

	public String getDateFrom() {
		return getString(Constants.DATEFROM);
	}

	public String getDateTo() {
		return getString(Constants.DATETO);
	}

	public Date getDateFromDate() throws ParseException {
		return getDate(getDateFrom());
	}

	public Date getDateToDate() throws ParseException {
		return getDate(getDateTo());
	}

	public String getPlace() {
		return getString(Constants.PLACE);
	}

	public String getCity() {
		return getString(Constants.CITY);
	}

	public String getAddress() {
		return getString(Constants.ADDRESS);
	}

	public Integer getNumRooms() {
		return getInteger(Constants.NUMROOMS);
	}

	public Double getPrize() { return getDouble(Constants.PRIZE);}

	public Double getLatitude() { return getDouble(Constants.LATITUDE);}

	public Double getLongitude() { return getDouble(Constants.LONGITUDE);}

	public static void findTripAccommodationInBackground(String objectId, final ITObjectCallback<TripAccommodation> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new ITObjectCallback<TripAccommodation>(){
			@Override
			public void done(TripAccommodation tripAccommodation, CloudException e) {
				if(tripAccommodation != null){
					callback.done(tripAccommodation, null);
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

	public static void findTripAccommodationListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<TripAccommodation> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<TripAccommodation>() {
			@Override
			public void done(TripAccommodation[] tripAccommodationList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripAccommodationList, null);
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
