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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParseClassName("TripAccommodation")
public class TripAccommodation extends ParseObject {

	private static String TAG = Constants.TAG_TRIPACCOMMODATIONMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public String getDateFrom() {
		if(getDate(Constants.TRIPTRANSPORT_DATEFROM)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIPTRANSPORT_DATEFROM));
	}

	public String getDateTo() {

		if(getDate(Constants.TRIPTRANSPORT_DATETO)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIPTRANSPORT_DATETO));
	}

	public String getPlace() {
		return getString(Constants.TRIPACCOMMODATION_PLACE);
	}

	public String getCity() {
		return getString(Constants.TRIPACCOMMODATION_CITY);
	}

	public String getAddress() {
		return getString(Constants.TRIPACCOMMODATION_ADDRESS);
	}

	public Integer getNumRooms() {
		return getInt(Constants.TRIPACCOMMODATION_NUMROOMS);
	}

	public Double getPrize() { return getDouble(Constants.TRIPTRANSPORT_PRIZE);}

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
