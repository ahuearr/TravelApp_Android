package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParseClassName("TripAccommodation")
public class TripAccommodation extends ParseObject {

	private static String TAG = Constants.TAG_TRIPACCOMMODATIONMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public String getDateFrom() {
		if(getDate(Constants.DATEFROM)==null){
			return null;
		}
		return df.format(getDate(Constants.DATEFROM));
	}

	public String getDateTo() {

		if(getDate(Constants.DATETO)==null){
			return null;
		}
		return df.format(getDate(Constants.DATETO));
	}

	public Date getDateFromDate() {
		return getDate(Constants.DATEFROM);
	}

	public Date getDateToDate() {
		return getDate(Constants.DATETO);
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
		return getInt(Constants.NUMROOMS);
	}

	public Double getPrize() { return getDouble(Constants.PRIZE);}

	public Double getLatitude() { return getDouble(Constants.LATITUDE);}

	public Double getLongtiude() { return getDouble(Constants.LONGITUDE);}

	public static void findTripAccommodationInBackground(String objectId,
			final GetCallback<TripAccommodation> callback) {
		ParseQuery<TripAccommodation> TripAccommodationQuery = ParseQuery.getQuery(TripAccommodation.class);
		TripAccommodationQuery.whereEqualTo(Constants.OBJECTID, objectId);
		TripAccommodationQuery.getFirstInBackground(new GetCallback<TripAccommodation>() {
			@Override
			public void done(TripAccommodation tripAccommodation, ParseException e) {

				if (e == null) {
					callback.done(tripAccommodation, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	public static void findTripAccommodationListByFieldsInBackground(
			Map<String, Object> filter, final FindCallback<TripAccommodation> callback) {
		ParseQuery<TripAccommodation> tripAccomodationQuery = ParseQuery.getQuery(TripAccommodation.class);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			tripAccomodationQuery.whereEqualTo((String) e.getKey(), e.getValue());
		}
		tripAccomodationQuery.findInBackground(new FindCallback<TripAccommodation>() {
			@Override
			public void done(List<TripAccommodation> tripAccommodationList, ParseException e) {
				if (e == null) {
					callback.done(tripAccommodationList, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
